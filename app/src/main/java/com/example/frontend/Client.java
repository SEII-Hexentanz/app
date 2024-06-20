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
    public static final String TAG = "Client";
    private static final String SERVER_ADDRESS = "10.0.2.2";
    private static final int SERVER_PORT = 8080;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static final Object mutex = new Object();
    private static final ResponseHandler responseHandler = new ResponseHandler();

    public synchronized static void setSocket(Socket socket) {
        Client.socket = socket;
    }

    public synchronized static void setOut(ObjectOutputStream out) {
        Client.out = out;
    }

    public synchronized static void setIn(ObjectInputStream in) {
        Client.in = in;
    }

    public static void send(Request request) {
        new Thread(() -> {
            try {
                if (out != null) {
                    out.writeObject(request);
                } else {
                    Log.e(TAG, "ObjectOutputStream is null, unable to send request");
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while sending request to server", e);
            }
        }).start();
    }

    @Override
    public void run() {
        synchronized (mutex) {
            try {
                setSocket(new Socket(SERVER_ADDRESS, SERVER_PORT));
                setOut(new ObjectOutputStream(socket.getOutputStream()));
                setIn(new ObjectInputStream(socket.getInputStream()));

                Log.i(TAG, "Client started");
                while (true) {
                    Response response;
                    try {
                        response = (Response) in.readObject();
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "Error while reading object from server", e);
                        continue;
                    }
                    Log.i(TAG, "Received response from server: " + response);
                    responseHandler.execute(response.responseType(), response.payload(), Game.INSTANCE);
                }
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("Unknown host", e); // we can't work without a server
            } catch (IOException e) {
                Log.e(TAG, "Error while connecting to server", e);
            }
        }
    }
}
