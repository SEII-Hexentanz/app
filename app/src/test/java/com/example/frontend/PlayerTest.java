package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PlayerTest {



    @Test
    public void testConstructor() {

        String username = "Alice";
        int age = 25;
        int imageResource = 123;
        Character[] characters= new Character[4];
        for(int i = 0; i < characters.length; i++){
            characters[i] = new Character();
        }
        Player player = new Player(username, age,characters, imageResource);

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
        Player player = new Player("Rolf", 20,null, R.drawable.greenhat);
        int newAge = 20;
        assertEquals(newAge, player.getAge());
    }


}
