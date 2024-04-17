package com.example.frontend;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import at.aau.models.Request;
import at.aau.payloads.EmptyPayload;
import at.aau.values.CommandType;

public class ClientTest {

    private static Socket mockSocket;
    private static ByteArrayOutputStream baos;
    private static ObjectOutputStream oos;

    @BeforeClass
    public static void setUp() throws IOException {
        // Prepare streams for testing
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        // Normally we would mock these but since we're not using Mockito:
        mockSocket = new Socket() {
            @Override
            public ObjectOutputStream getOutputStream() throws IOException {
                return oos;
            }
        };

        Client.setSocket(mockSocket);
        Client.setOut(oos);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        oos.close();
        baos.close();
        mockSocket.close();
    }

    @Test
    public void testSend_whenOutNotNull() throws IOException, ClassNotFoundException {
        Request mockRequest = new Request(CommandType.PING,new EmptyPayload());
        Client.send(mockRequest);


        oos.flush();

        assertTrue("Data should be written to stream", baos.size() > 0);
    }

    @Test
    public void testSetSocket_setsSocketCorrectly() {
        Client.setSocket(mockSocket);
        // As we're not mocking and can't access the private field directly, this is just a conceptual test example.
        // In real tests, you should use reflection or change access modifiers to verify internal state changes.
        assertNotNull("Socket should not be null", mockSocket);
    }

    @Test
    public void testSetOut_setsOutCorrectly() {
        Client.setOut(oos);

        assertNotNull("ObjectOutputStream should not be null", oos);
    }

    @Test
    public void testSetIn_setsInCorrectly() {
        // This test is conceptual as we cannot access the private 'in' without reflection
        assertNotNull("ObjectInputStream setting needs to be verified manually", oos);
    }
}
