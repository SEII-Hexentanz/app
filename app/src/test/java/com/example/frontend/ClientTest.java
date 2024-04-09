package com.example.frontend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    private ServerSocket serverSocket;
    private final int testPort = 8080;
    private final String testAddress = "localhost";
    private Thread serverThread;

    @BeforeEach
    void setUp() throws IOException {
        // Mock-Server Setup
        serverSocket = new ServerSocket(testPort);
        serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                    out.println(inputLine);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        serverSocket.close();
        serverThread.interrupt();
    }

    @Test
    void testStartClientWithLocalhost() {
        Client client = new Client() {
            @Override
            protected void handleException(Exception e) {
                throw new RuntimeException(e);
            }
        };

        client.serverAddress = testAddress;
        client.serverPort = testPort;


        client.startClient();

    }
}
