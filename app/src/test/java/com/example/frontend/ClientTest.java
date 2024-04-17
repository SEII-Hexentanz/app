package com.example.frontend;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

import at.aau.models.Request;

public class ClientTest {

    private Socket socketMock;
    private ObjectOutputStream outMock;
    private ObjectInputStream inMock;

    @Before
    public void setUp() throws Exception {
        socketMock = mock(Socket.class);
        outMock = mock(ObjectOutputStream.class);
        inMock = mock(ObjectInputStream.class);

        Field socketField = Client.class.getDeclaredField("socket");
        Field outField = Client.class.getDeclaredField("out");
        Field inField = Client.class.getDeclaredField("in");

        socketField.setAccessible(true);
        outField.setAccessible(true);
        inField.setAccessible(true);


        socketField.set(null, socketMock);
        outField.set(null, outMock);
        inField.set(null, inMock);


        when(socketMock.getOutputStream()).thenReturn(outMock);
        when(socketMock.getInputStream()).thenReturn(inMock);
    }

    @Test
    public void testSend() throws Exception {
        Request request = new Request(at.aau.values.CommandType.DICE_ROLL, new at.aau.payloads.EmptyPayload());


        Client.send(request);


        Thread.sleep(500); // Wait for the thread to process

        verify(outMock).writeObject(request); // Check that writeObject was indeed called with the right argument
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClientConnectionFailure() throws Exception {

        when(new Socket("10.0.2.2", 8080)).thenThrow(new java.io.IOException());


        Client client = new Client();
        client.start();

        client.join(); // Wait for the thread to finish
    }
}
