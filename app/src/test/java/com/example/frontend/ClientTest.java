package com.example.frontend;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import at.aau.models.Request;

public class ClientTest {
    @Mock
    private Socket mockSocket;
    @Mock
    private ObjectOutputStream mockOut;
    @Mock
    private ObjectInputStream mockIn;

    private Client client;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(mockSocket.getOutputStream()).thenReturn(mockOut);
        when(mockSocket.getInputStream()).thenReturn(mockIn);

        Client.setSocket(mockSocket);
        Client.setOut(mockOut);
        Client.setIn(mockIn);

        client = new Client();
    }

    @Test
    public void testSend() throws IOException {
        Request mockRequest = mock(Request.class);
        Client.send(mockRequest);

        verify(mockOut, timeout(100)).writeObject(any(Request.class));
    }

    @Test
    public void testSocketInitialization() throws IOException {
        client.start();

        verify(mockSocket).getOutputStream();
        verify(mockSocket).getInputStream();
    }

    @Test
    public void testRunReceivesResponseAndHandlesIOException() throws IOException, ClassNotFoundException {
        when(mockIn.readObject()).thenThrow(new IOException("Forced IOException for testing"));

        client.start();

        verify(mockOut, never()).writeObject(any()); // Should not write after error
        verify(mockIn, atLeastOnce()).readObject();
    }

    @Test
    public void testRunReceivesResponseAndHandlesClassNotFoundException() throws IOException, ClassNotFoundException {
        when(mockIn.readObject()).thenThrow(new ClassNotFoundException("Forced ClassNotFoundException for testing"));

        client.start();

        verify(mockIn, atLeastOnce()).readObject();
    }

    @Test
    public void testRunHandlesUnknownHostException() {
        Client.setSocket(null);

        try {
            client.start();
        } catch (IllegalArgumentException e) {
            assert e.getCause() instanceof UnknownHostException;
        }
    }
}
