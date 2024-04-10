package com.example.frontend;

import java.io.*;
import java.net.*;

public class Client {
    protected String serverAddress = "Server_IP"; // Server-IP ersetzen
    protected int serverPort = 8080;
    private String response;


    public String getServerAddress() {
        return this.serverAddress;
    }
    public int getServerPort() {
        return this.serverPort;
    }

    public void startClient() {

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("Hallo Server!");
             response = in.readLine();
            System.out.println("Server antwortet: " + response);

        } catch (UnknownHostException e) {
            System.err.println("Server nicht gefunden: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Fehler: " + e.getMessage());
        }
    }

    protected void handleException(Exception e) {
        System.err.println("Folgender Fehler: " + e.getMessage());
    }
    public String getResponseFromServer(){
        return response;
    }
}

