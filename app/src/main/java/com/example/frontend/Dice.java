package com.example.frontend;

import java.security.SecureRandom;

public class Dice {
    private int diceNumber;
    private SecureRandom ran;
    // as sonarcloud suggests due to security reason use security random instead of ran

    public Dice() {
        super();
        diceNumber = 0;
        ran = new SecureRandom();
    }

   /* public int useDice() {
        diceNumber = ran.nextInt(6) + 1;
        return diceNumber;
    }*/

    //is also sum of dice
    public int getDice() {
        return diceNumber;
    }


    public void setDice(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public void setRan(SecureRandom ran) {
        this.ran = ran;
    }

    @Override
    public String toString() {
        return String.format("dice=%d", diceNumber);
    }

}
