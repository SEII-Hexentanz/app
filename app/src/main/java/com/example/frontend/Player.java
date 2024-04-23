package com.example.frontend;

public class Player {
    private String username;
    private int age;
    private int ImageResource;

    int[] playerPosition;
    public Player(){
        //Empty constructor
    }
    //is currently empty
    public Player(String username, int age, int imageResource) {
        this.username = username;
        this.age = age;
        this.ImageResource = imageResource;
        this.playerPosition = new int[4];
    }

    public int getImageResource() {
        return ImageResource;
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
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

    public int[] getPositions(){
        return playerPosition;
    }

    public void setPlayerPosition(){
        this.playerPosition = playerPosition;
    }
}
