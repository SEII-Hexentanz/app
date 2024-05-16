package com.example.frontend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;

 class DiceTest {
    private Dice dices;

    @BeforeEach
    public void setup() {
        dices = new Dice();
    }

    @Test
     void testDiceInitialization() {
        // Überprüfen, ob die Dice-Instanz korrekt initialisiert wurde
        assertNotNull(dices, "Dice-Instanz sollte nicht null sein.");
        // Überprüfen, ob der Startwert des Würfels 0 ist
        assertEquals(0, dices.getDice(), "Initialer Würfelwert sollte 0 sein.");
    }

    @Test
     void testDiceRollValueRange() {
        // Führe eine Reihe von Würfelwürfen aus, um sicherzustellen, dass alle Werte im gültigen Bereich liegen
        for (int i = 0; i < 1000; i++) {
            dices.useDice();
            int result = dices.getDice();
            assertTrue(result >= 1 && result <= 6, "Würfelwert sollte zwischen 1 und 6 liegen.");
        }
    }

    @Test
     void testSetRandomObject() {
        // Erstellen eines neuen SecureRandom-Objekts und Setzen dieses als Random-Objekt für die Dice-Instanz
        SecureRandom newRandom = new SecureRandom();
        dices.setRan(newRandom);
        // Da das direkte Überprüfen des internen Zustands von 'ran' nicht möglich ist,
        // überprüfen wir stattdessen, ob 'useDice' weiterhin wie erwartet funktioniert.
        dices.useDice();
        int result = dices.getDice();
        assertTrue(result >= 1 && result <= 6, "Nach dem Setzen von SecureRandom sollte der Würfelwert zwischen 1 und 6 liegen.");
    }


     @Test
      void testSetDice() {

         dices.setDice(4);
         assertEquals(4, dices.useDice());
     }

     @Test
      void testToString() {
         dices.setDice(3);
         assertEquals("dice=3", dices.toString());
     }
}
