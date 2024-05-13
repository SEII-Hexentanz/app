package com.example.frontend;

public class Player {
    private String username;
    private int age;
    Character[] characters;
    private int imageResource;

    public Player(){
        //Empty constructor
    }

    public Player(String username, int age, int imageResource) {
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
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
