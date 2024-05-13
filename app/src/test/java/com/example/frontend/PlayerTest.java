package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class PlayerTest {



    @Test
    public void testConstructor() {

        String username = "Alice";
        int age = 25;
        int imageResource = 123;
        Character[] characters= new Character[4];
        Player player = new Player(username, age, imageResource);

        assertEquals(username, player.getUsername());
        assertEquals(age, player.getAge());
        assertEquals(imageResource, player.getImageResource());
    }
    @Test
    public void testSetAndGetUsername() {
        Player player = new Player();
        String newUsername = "Bob";
        player.setUsername(newUsername);
        assertEquals(newUsername, player.getUsername());
    }

    @Test
    public void testSetAndGetAge() {
        Player player = new Player("Rolf", 20, R.drawable.greenhat);
        int newAge = 20;
        assertEquals(newAge, player.getAge());
    }


}
