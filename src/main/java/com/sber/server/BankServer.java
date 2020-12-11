package com.sber.server;

import com.sber.services.AccountServiceImp;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankServer {
    private static Logger log = Logger.getLogger(BankServer.class.getName());

    public static void main(String[] args) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
                HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
                server.createContext("/creating-new-account-card", new CardHandle());
                server.createContext("/return-list-cards", new UserHandle());
                server.createContext("/check-balance", new AccountHandle());
                server.createContext("/replenishment-balance", new AccountHandle());
                server.setExecutor(threadPoolExecutor);
                server.start();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Exception: ", e);
            }
    }
}
