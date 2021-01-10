package com.sber.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.models.Card;
import com.sber.models.User;
import com.sber.services.NotSavedSubEntityException;
import com.sber.services.UserService;
import com.sber.services.UserServiceImp;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandle implements HttpHandler {
    private static Logger log = Logger.getLogger(UserHandle.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("GET".equals(exchange.getRequestMethod().toUpperCase())) {
            cardsHandleResponse(exchange);
        }
    }

    private void cardsHandleResponse(HttpExchange exchange) {
        ObjectMapper mapper = new ObjectMapper();
        String cardsJSON = "[{\"status\":\"FAIL\"}]";
        String userId = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        try {
            User user = User.builder()
                    .userId(Long.parseLong(userId))
                    .build();
            UserService userServiceImp = new UserServiceImp();
            List<Card> cards = userServiceImp.returnListCards(user);
            if (!cards.isEmpty()) {
                cardsJSON = mapper.writeValueAsString(cards);
                exchange.sendResponseHeaders(200, cardsJSON.length());
            } else {
                exchange.sendResponseHeaders(500, cardsJSON.length());
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(cardsJSON.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
            throw new NotSavedSubEntityException("problems with the cards list");
        }
    }
}
