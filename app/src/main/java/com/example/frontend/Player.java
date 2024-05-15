package com.example.frontend;

import java.util.List;

import at.aau.models.Character;
import at.aau.values.Color;

public class Player {
    private String username;
    private int age;
    Character[] characters;
    private int imageResource;

    public Player(){
        //Empty constructor
    }
    //is currently empty
    public Player(String username, int age, Character[] characters,int imageResource) {
        this.username = username;
        this.age = age;
        this.imageResource = imageResource;
        this.characters = new Character[4];
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

    public Character[] getCharacter(){
        return characters;
    }

    public void setCharacter(){
        this.characters = characters;
    }

    public int getImageResource() {
        return imageResource;
    }
    public void setImageResource(int imageResource) {
        imageResource = imageResource;
    }

}
