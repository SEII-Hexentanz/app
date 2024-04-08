package com.example.frontend;

import java.security.SecureRandom;

public class Dice {
    private int dice_number;
    private SecureRandom ran;
    // as sonarcloud suggests due to security reason use security random instead of ran

    public Dice(){
        super();
        dice_number=0;
        ran=new SecureRandom();
    }

    public void useDice(){
        dice_number= ran.nextInt(6)+1;
    }

    //is also sum of dice
    public int getDice() {
        return dice_number;
    }

    public void setRan(SecureRandom ran){
        this.ran = ran;
    }

    @Override
    public String toString(){
        return String.format("dice=%d", dice_number);
    }

}
