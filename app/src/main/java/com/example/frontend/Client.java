package com.example.frontend;

import android.util.Log;

import com.example.frontend.responseHandler.ResponseHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import at.aau.models.Request;
import at.aau.models.Response;

public class Client extends Thread {
    private static final String serverAddress = "10.0.2.2";
    private static final int serverPort = 8080;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void send(Request request) {
        new Thread(() -> {
            try {
                out.writeObject(request);
            } catch (IOException e) {
                Log.e("Client", "Error while sending request to server", e);
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Log.i("Client", "Client started");
            while (true) {
                Response response;
                try {
                    response = (Response) in.readObject();
                } catch (ClassNotFoundException e) {
                    Log.e("Client", "Error while reading object from server", e);
                    continue;
                }
                Log.i("Client", "Received response from server: " + response);
                ResponseHandler.execute(response.responseType(), response.payload());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unknown host", e); // we can't work without a server
        } catch (IOException e) {
            Log.e("Client", "Error while connecting to server", e);
        }
    }
}
