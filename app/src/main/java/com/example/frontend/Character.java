package com.example.frontend;

public class Character {
    private int position;
    private CharacterState status;

    public Character(int position, CharacterState status){
        this.position = position;
        this.status = status;
    }

    public  Character(){
        this.position = 0;
        this.status = CharacterState.HOME;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CharacterState getStatus() {
        return status;
    }

    public void setStatus(CharacterState status) {
        this.status = status;
    }
}
