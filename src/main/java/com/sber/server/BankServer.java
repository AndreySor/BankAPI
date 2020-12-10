package com.sber.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import com.sber.services.CardServiceImp;
import com.sber.services.UserServiceImp;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BankServer {

    public static void main(String[] args) {
        HttpServer server;

            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
                server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
                server.createContext("/creating-new-account-card", new CardHandle());
                server.createContext("/return-list-cards", new UserHandle());
                server.createContext("/return-balance", new AccountHandle());
                server.createContext("/replenishment-balance", new AccountHandle());
                server.setExecutor(threadPoolExecutor);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

//    static class CardHandle implements HttpHandler {
//
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            if("POST".equals(exchange.getRequestMethod())) {
//                cardHandleResponse(exchange);
//            }
//        }
//
//        private void cardHandleResponse(HttpExchange exchange) throws IOException {
//            ObjectMapper mapper = new ObjectMapper();
//            InputStream response = exchange.getRequestBody();
//            Scanner in = new Scanner(response);
//            String stringJSON = in.nextLine();
//            try {
//                Account account = mapper.readValue(stringJSON, Account.class);
//                CardServiceImp cardServiceImp = new CardServiceImp();
//                String result = cardServiceImp.addNewCardOnAccountNumber(account);
//                if (result.equals("SUCCESS")) {
//                    exchange.sendResponseHeaders(200, "".length());
//                } else {
//                    exchange.sendResponseHeaders(500, "".length());
//                }
//                OutputStream os = exchange.getResponseBody();
//                os.write("".getBytes());
//                os.flush();
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    static class  UserHandle implements HttpHandler {
//
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            if("GET".equals(exchange.getRequestMethod().toUpperCase())) {
//                cardsHandleResponse(exchange);
//            }
//        }
//
//        private void cardsHandleResponse(HttpExchange exchange) throws IOException {
//            ObjectMapper mapper = new ObjectMapper();
//            String cardsJSON = "[{\"status\":\"fail\"}]";
//            String userId = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
//            try {
//                User user = User.builder()
//                        .userId(Long.parseLong(userId))
//                        .build();
//                UserServiceImp userServiceImp = new UserServiceImp();
//                List<Card> cards = userServiceImp.returnListCards(user);
//                if (!cards.isEmpty()) {
//                    cardsJSON = mapper.writeValueAsString(cards);
//                    exchange.sendResponseHeaders(200, cardsJSON.length());
//                } else {
//                    cardsJSON = "[{\"status\":\"fail\"}]";
//                    exchange.sendResponseHeaders(500, cardsJSON.length());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            OutputStream os = exchange.getResponseBody();
//            os.write(cardsJSON.getBytes(StandardCharsets.UTF_8));
//            os.flush();
//            os.close();
//        }
//    }
}
