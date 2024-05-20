package com.example.frontend;

import java.util.ArrayList;
import java.util.List;

import at.aau.models.Character;
import at.aau.values.CharacterState;
import at.aau.values.Color;

public class Player implements Comparable<Player> {
    List<Character> characters;

    private String username;
    private int age;
    private int imageResource;
    private Color color;

    public Player() {
        //Empty constructor
    }

    public Player(String username, int age, List<Character> characters, Color color) {
        this.username = username;
        this.age = age;
        this.characters = characters;
        this.color = color;
        switch (color) {
            case YELLOW -> this.imageResource = R.drawable.yellowhat;
            case PINK -> this.imageResource = R.drawable.pinkhat;
            case RED -> this.imageResource = R.drawable.redhat;
            case GREEN -> this.imageResource = R.drawable.greenhat;
            case LIGHT_BLUE -> this.imageResource = R.drawable.lightbluehat;
            case DARK_BLUE -> this.imageResource = R.drawable.bluehat;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public int compareTo(Player o) {
        return o.getAge() - this.getAge();
    }

    public Color color() {
        return color;
    }

    public Player updateCharacterState(int characterIndex, CharacterState newState) {
        Character updatedCharacter = new Character(characters.get(characterIndex).position(), newState);
        List<Character> updatedCharacters = new ArrayList<>(characters);
        updatedCharacters.set(characterIndex, updatedCharacter);
        return new Player(username, age, updatedCharacters, color);
    }


}
