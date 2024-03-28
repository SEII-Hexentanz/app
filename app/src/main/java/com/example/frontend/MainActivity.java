package com.example.frontend;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameBoard gameBoard = new GameBoard(this, 2, 2);
        LinearLayout container = findViewById(R.id.GameBoardContainer);
        container.addView(gameBoard);

    }
}
