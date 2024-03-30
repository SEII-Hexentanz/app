package com.example.frontend.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dice);

        /*// Prüfen Sie, ob die Activity neu erstellt wird oder wiederhergestellt wird
        if (savedInstanceState == null) {
            // Erstellen Sie eine Instanz Ihres Fragments
            DiceFragment diceFragment = new DiceFragment();

            // Fügen Sie das Fragment zum 'fragment_container' hinzu
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, diceFragment)
                    .commit();
        }*/
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
}
