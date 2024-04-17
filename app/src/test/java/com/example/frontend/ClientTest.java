package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
import at.aau.models.Response;

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
        client.start(); // This starts the client in a new thread
    }

    @Test
    public void testSend() throws IOException {
        Request mockRequest = mock(Request.class);
        Client.send(mockRequest);
        verify(mockOut).writeObject(mockRequest);
    }

    @Test
    public void testRunConnectionEstablishment() throws IOException {
        client.run();
        verify(mockSocket).getOutputStream();
        verify(mockSocket).getInputStream();
    }
    @Test
    public void testReceiveResponse() throws IOException, ClassNotFoundException {
        Response response = null;
        Response mockResponse = new Response(response.responseType(), null);
        when(mockIn.readObject()).thenReturn(mockResponse);


        client.run();  // Run the part which reads from the input stream
        verify(mockIn).readObject();
    }


    @Test
    public void testSocketClosedOnIOException() throws IOException {
        when(mockSocket.getOutputStream()).thenThrow(new IOException("Forced IOException for testing"));

        // Expect the exception to propagate up or handle gracefully depending on implementation
        assertThrows(IOException.class, () -> client.run());

        // Verify socket cleanup or similar post-exception handling
        verify(mockSocket).close();
    }
}
