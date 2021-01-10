package com.sber.server;

import com.sber.FullDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankServerTests {

    @BeforeEach
    public void init() throws SQLException {
        new FullDatabase().fullDatabase();
    }

    @Test
    void isCreateNewCard() throws IOException {

        JSONObject jsonObject = new JSONObject("{\"accountNumber\":\"4356 3245 1234 7345\",\"owner\":{\"userId\":5}}");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/creating-new-account-card").openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();
        }

        int status = connection.getResponseCode();
        assertEquals(status, 200);
    }

    @Test
    void isNotCreateNewCard() throws IOException {

        JSONObject jsonObject = new JSONObject("{\"accountNumber\":\"4356 0000 1234 7345\",\"owner\":{\"userId\":5}}");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/creating-new-account-card").openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();
        }
        int status = connection.getResponseCode();
        assertEquals(status, 500);
    }

    @Test
    void isGetListCards() throws IOException {
        String id = "1";

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/return-list-cards?userId=" + id).openConnection();
        connection.setRequestMethod("GET");

        InputStream response = connection.getInputStream();
        Scanner in = new Scanner(response);
        String cards = in.nextLine();
        assertEquals(cards, "[{\"cardId\":2,\"cardNumber\":\"4356 3834 1234 2855\"}]");
    }

    @Test
    void isNotGetListCards() throws IOException {
        String id = "9";

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/return-list-cards?userId=" + id).openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        assertEquals(status, 500);
    }

    @Test
    void isBalanceReplenishmentAndCheckBalance() throws IOException {
        JSONObject jsonObject = new JSONObject("{\"accountNumber\":\"4356 3245 1234 7345\", \"balance\":\"23432.86\"}");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/replenishment-balance").openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();
        }

        int status = connection.getResponseCode();
        assertEquals(status, 200);
        String id = "1";

        HttpURLConnection connection2 = (HttpURLConnection) new URL("http://localhost:8001/check-balance?accountId="+id).openConnection();
        connection2.setRequestMethod("GET");

        InputStream response = connection2.getInputStream();
        Scanner in = new Scanner(response);
        String cards = in.nextLine();
        assertEquals(cards, "{\"accountId\":1,\"accountNumber\":\"4356 3245 1234 7345\",\"balance\":23678.16}");
    }

    @Test
    void isNotBalanceReplenishment() throws IOException {
        JSONObject jsonObject = new JSONObject("{\"accountNumber\":\"4356 3245 1233 7345\", \"balance\":\"23432.86\"}");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/replenishment-balance").openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();
        }

        int status = connection.getResponseCode();
        assertEquals(status, 500);
    }

    @Test
    void isCheckBalance() throws IOException {
        String id = "1";

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/check-balance?accountId="+id).openConnection();
        connection.setRequestMethod("GET");

        InputStream response = connection.getInputStream();
        Scanner in = new Scanner(response);
        String cards = in.nextLine();
        assertEquals(cards, "{\"accountId\":1,\"accountNumber\":\"4356 3245 1234 7345\",\"balance\":245.30}");
    }

    @Test
    void isNotCheckBalance() throws IOException {
        String id = "9";

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8001/check-balance?accountId="+id).openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        assertEquals(status, 500);
    }
}
