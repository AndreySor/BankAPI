package com.sber.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import com.sber.services.AccountServiceImp;
import com.sber.services.CardServiceImp;
import com.sber.services.UserServiceImp;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class AccountHandle implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            checkingBalanceHandleResponse(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            balanceReplenishmentHandleResponse(exchange);
        }
    }

    private void checkingBalanceHandleResponse(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String balanceJSON = "[{\"status\":\"fail\"}]";
        String accountNumber = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        try {
            Account account = Account.builder()
                    .accountNumber(accountNumber)
                    .build();
            AccountServiceImp accountServiceImp = new AccountServiceImp();
            Account checkBalance = accountServiceImp.checkingBalance(account);
            if (checkBalance != null) {
                balanceJSON = mapper.writeValueAsString(checkBalance);
                exchange.sendResponseHeaders(200, balanceJSON.length());
            } else {
                balanceJSON = "[{\"status\":\"fail\"}]";
                exchange.sendResponseHeaders(500, balanceJSON.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream os = exchange.getResponseBody();
        os.write(balanceJSON.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
    }

    private void balanceReplenishmentHandleResponse(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream response = exchange.getRequestBody();
        Scanner in = new Scanner(response);
        String stringJSON = in.nextLine();
        try {
            Account account = mapper.readValue(stringJSON, Account.class);
            AccountServiceImp accountServiceImp = new AccountServiceImp();
            String result = accountServiceImp.balanceReplenishment(account);
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
            e.printStackTrace();
        }
    }
}
