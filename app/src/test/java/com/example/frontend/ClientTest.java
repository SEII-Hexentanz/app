package com.example.frontend;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
/*
    @Test
    public void testSend() throws IOException {
        Request mockRequest = mock(Request.class);
        Client.send(mockRequest);

        verify(mockOut, timeout(100)).writeObject(any(Request.class));
    }
    */

}
