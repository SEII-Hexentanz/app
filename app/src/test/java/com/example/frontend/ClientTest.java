package com.example.frontend;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import at.aau.models.Request;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {

    @Test
    public void testSend() {
        // Mock für Socket, ObjectOutputStream und Request erstellen
        Socket mockSocket = mock(Socket.class);
        ObjectOutputStream mockOut = mock(ObjectOutputStream.class);
        ObjectInputStream mockIn = mock(ObjectInputStream.class);
        Request mockRequest = mock(Request.class);

        try {
            // Mock-Verhalten für Socket erstellen
            when(mockSocket.getOutputStream()).thenReturn(mockOut);
            when(mockSocket.getInputStream()).thenReturn(mockIn);

            // Testen, ob die Methode send() richtig aufgerufen wird
            Client.setSocket(mockSocket);
            Client.setOut(mockOut);
            Client.setIn(mockIn);

            Client.send(mockRequest);

            // Überprüfen, ob die writeObject() Methode des ObjectOutputStreams aufgerufen wurde
            Mockito.verify(mockOut).writeObject(mockRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
