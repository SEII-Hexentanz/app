package com.example.frontend.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.frontend.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dice);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.DiceFragment, new DiceFragment());
            fragmentTransaction.commit();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
}
