package com.example.frontend;

import androidx.annotation.NonNull;

import java.security.SecureRandom;
import java.util.Locale;

public class Dice {
    private int diceNumber;
    private SecureRandom ran;
    // as sonarcloud suggests due to security reason use security random instead of ran

    public Dice() {
        super();
        diceNumber = 0;
        ran = new SecureRandom();
    }

    public void useDice() {
        diceNumber = ran.nextInt(6) + 1;
    }

    //is also sum of dice
    public int getDice() {
        return diceNumber;
    }


    public void setDice(int diceNumber){this.diceNumber=diceNumber;}

    public void setRan(SecureRandom ran) {
        this.ran = ran;
    }

    @NonNull
    @Override
    public String toString() {

        String format = "dice=%d";
        return String.format(Locale.getDefault(),format, diceNumber);
    }

}
