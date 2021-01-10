package com.sber.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.models.Account;
import com.sber.services.CardService;
import com.sber.services.CardServiceImp;
import com.sber.services.NotSavedSubEntityException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
        String cardJSON = "[{\"status\":\"FAIL\"}]";
        Scanner in = new Scanner(response);
        String stringJSON = in.nextLine();
        try {
            Account account = mapper.readValue(stringJSON, Account.class);
            CardService cardServiceImp = new CardServiceImp();
            String result = cardServiceImp.addNewCardOnAccountNumber(account);
            if (result.equals("SUCCESS")) {
                cardJSON = "[{\"status\":\"SUCCESS\"}]";
                exchange.sendResponseHeaders(200, "".length());
            } else {
                exchange.sendResponseHeaders(500, "".length());
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(cardJSON.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
            throw new NotSavedSubEntityException("failed to create a new card");
        }
    }
}
