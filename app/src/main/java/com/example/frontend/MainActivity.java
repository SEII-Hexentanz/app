package com.example.frontend;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;

import at.aau.models.Request;
import at.aau.payloads.RegisterPayload;
import at.aau.values.CommandType;

public class MainActivity extends AppCompatActivity {
    private final Client client = new Client();
    private Button btnStart;
    private TextView inputUsername;
    private TextView inputAge;
    private View createLobbyFragment;
    private View fragmentDice;
    private View endFrag;
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client.start();

        findViews();

        checkInputUsername(inputUsername);

        checkInputAge(inputAge);
        // Get the root layout of your activity
        View rootView = findViewById(android.R.id.content);

        // Set a touch listener to the root layout
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Hide the keyboard when touched
                hideKeyboard(v);
                return false;
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {


            //Send to server the responseUser and responseAge

            public void onClick(View v) {
                final String responseUser = inputUsername.getText().toString();
                String responesAge = inputAge.getText().toString();
                Log.i("Change Fragement", "Fragment change event started");
                if (createLobbyFragment.getVisibility() == View.INVISIBLE) {
                    createLobbyFragment.setVisibility(View.GONE);
                } else {
                    createLobbyFragment.setVisibility(View.VISIBLE);
                    showCreateLobbyFragment();

                }
                Client.send(new Request(CommandType.REGISTER, new RegisterPayload(responseUser, Integer.parseInt(responesAge))));
                Log.i("ResponseAge", responesAge.toString());

            }
        });
    }

    void findViews() {
        btnStart = findViewById(R.id.buttonStart);
        inputUsername = findViewById(R.id.startScreenUsername);
        inputAge = findViewById(R.id.startScreenAge);
        createLobbyFragment = findViewById(R.id.fragmentContainerView2);
        fragmentDice = findViewById(R.id.dice2);
        endFrag = findViewById(R.id.endfragment);
    }

    void checkInputUsername(TextView inputUsername) {
        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Nichts zu tun hier
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() < 3) {
                    inputUsername.setError("Input sollte mindestens 3 Zeichen lang sein");
                } else {
                    inputUsername.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nichts zu tun hier
            }
        });
    }

    void checkInputAge(TextView inputAge) {
        inputAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Nichts zu tun hier
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!isValidAge(charSequence.toString())) {
                    inputAge.setError("Das Alter muss zwischen 8 und 99 liegen.");
                } else {
                    inputAge.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nichts zu tun hier
            }
        });
    }

    private boolean isValidAge(String ageTxt) {
        if (ageTxt.isEmpty()) {
            return false;
        }
        int age = Integer.parseInt(ageTxt);
        return age >= 8 && age <= 99;
    }

    public void showCreateLobbyFragment() {
        String name = inputUsername.getText().toString();

        CreateLobbyFragment createLobbyFragment = CreateLobbyFragment.newInstance(name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView2, createLobbyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //TODO:  maybe use switch-case statement for easier change Fragment

    }
    private void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}


