package com.example.frontend;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import com.example.frontend.gamelogic.Dice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import java.security.SecureRandom;
import java.util.Random;

public class DiceTest {
    static Dice dices;

    @BeforeEach
    public void setup() {
        dices = new Dice();
    }

    @Test
    public void testCreateDices() {
        assertNotNull(dices, "Dice-Instanz sollte nicht null sein.");
    }

    @Test
    public void testUseDiceGeneratesValidValue() {
        dices.useDice();
        int diceValue = dices.getDice();
        assertTrue(diceValue >= 1 && diceValue <= 6, "Der Würfelwert sollte zwischen 1 und 6 liegen.");
    }

    @Test
    public void testGetDiceAfterUseDice() {
        dices.useDice();
        int result = dices.getDice();
        assertTrue(result >= 1 && result <= 6, "Der Wert von getDice() sollte zwischen 1 und 6 sein.");
    }

    @Test
    public void testSetRanSetsRandomObjectCorrectly() {
        SecureRandom newRandom = new SecureRandom();
        dices.setRan(newRandom);
        dices.useDice();
        int diceValue = dices.getDice();
        assertTrue(diceValue >= 1 && diceValue <= 6, "Nach Setzen von Random und Aufruf von useDice() sollte ein gültiger Würfelwert zwischen 1 und 6 vorhanden sein.");
    }

}
