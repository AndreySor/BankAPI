package com.sber.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import com.sber.services.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountHandle implements HttpHandler {
    private static Logger log = Logger.getLogger(AccountHandle.class.getName());

    @Override
    public void handle(HttpExchange exchange) {
        if ("GET".equals(exchange.getRequestMethod())) {
            checkingBalanceHandleResponse(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            balanceReplenishmentHandleResponse(exchange);
        }
    }

    private void checkingBalanceHandleResponse(HttpExchange exchange) {
        ObjectMapper mapper = new ObjectMapper();
        String balanceJSON = "[{\"status\":\"fail\"}]";
        String accountId = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        try {
            Account account = Account.builder()
                    .accountId(Long.parseLong(accountId))
                    .build();
            AccountService accountServiceImp = new AccountServiceImp();
            Account checkBalance = accountServiceImp.checkingBalance(account);
            if (checkBalance != null) {
                balanceJSON = mapper.writeValueAsString(checkBalance);
                exchange.sendResponseHeaders(200, balanceJSON.length());
            } else {
                exchange.sendResponseHeaders(500, balanceJSON.length());
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(balanceJSON.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
            throw new NotSavedSubEntityException("failed to check balance");
        }

    }

    private void balanceReplenishmentHandleResponse(HttpExchange exchange) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream response = exchange.getRequestBody();
        String balanceJSON = "[{\"status\":\"FAIL\"}]";
        Scanner in = new Scanner(response);
        String stringJSON = in.nextLine();
        try {
            Account account = mapper.readValue(stringJSON, Account.class);
            AccountServiceImp accountServiceImp = new AccountServiceImp();
            String result = accountServiceImp.balanceReplenishment(account);
            if (result.equals("SUCCESS")) {
                balanceJSON = "[{\"status\":\"SUCCESS\"}]";
                exchange.sendResponseHeaders(200, "".length());
            } else {
                exchange.sendResponseHeaders(500, "".length());
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(balanceJSON.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
            throw new NotSavedSubEntityException("failed to top up your balance");
        }
    }
}
