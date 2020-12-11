package com.sber.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.models.Account;
import com.sber.services.CardServiceImp;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardHandle implements HttpHandler {
    private static Logger log = Logger.getLogger(CardHandle.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("POST".equals(exchange.getRequestMethod())) {
            cardHandleResponse(exchange);
        }
    }

    private void cardHandleResponse(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream response = exchange.getRequestBody();
        Scanner in = new Scanner(response);
        String stringJSON = in.nextLine();
        try {
            Account account = mapper.readValue(stringJSON, Account.class);
            CardServiceImp cardServiceImp = new CardServiceImp();
            String result = cardServiceImp.addNewCardOnAccountNumber(account);
            if (result.equals("SUCCESS")) {
                exchange.sendResponseHeaders(200, "".length());
            } else {
                exchange.sendResponseHeaders(500, "".length());
            }
            OutputStream os = exchange.getResponseBody();
            os.write("".getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }
    }
}
