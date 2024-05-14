package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import at.aau.models.Character;
import at.aau.values.CharacterState;
import at.aau.values.Color;

import java.util.Arrays;

public class PlayerTest {



    @Test
    public void testConstructor() {

        String username = "Alice";
        int age = 25;
        int imageResource = 123;
        List<Character> characters = new ArrayList<>(4);
        for (int i = 0; i < characters.size(); i++) {
            characters.add(new Character(0, CharacterState.HOME));
        }
        Player player = new Player(username, age, characters, Color.RED);

        assertEquals(username, player.getUsername());
        assertEquals(age, player.getAge());
        assertEquals(R.drawable.redhat, player.getImageResource());
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
        Player player = new Player("Rolf", 20, null, Color.GREEN);
        int newAge = 20;
        assertEquals(newAge, player.getAge());
    }


}
