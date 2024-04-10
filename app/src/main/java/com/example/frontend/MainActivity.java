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

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private TextView inputUsername;
    private TextView inputAge;
    private View createLobbyFragment;
    private View mainActivityView;
    private View fragment_dice;
    private View endFrag;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            final String responseUser = inputUsername.getText().toString();
            final String responesAge = inputAge.getText().toString();

            //Send to server the responseUser and responseAge

            public void onClick(View v) {
                Log.i("Change Fragement", "Fragment change event started");
                if (createLobbyFragment.getVisibility() == View.INVISIBLE) {
                    createLobbyFragment.setVisibility(View.GONE);
                } else {
                    mainActivityView.setVisibility(View.GONE);
                    createLobbyFragment.setVisibility(View.VISIBLE);
                    showCreateLobbyFragment();

                }
                startClientThread();

            }
        });
    }

    void findViews() {
        btnStart = findViewById(R.id.buttonStart);
        inputUsername = findViewById(R.id.startScreenUsername);
        inputAge = findViewById(R.id.startScreenAge);
        createLobbyFragment = findViewById(R.id.fragmentContainerView2);
        mainActivityView = findViewById(R.id.main2);
        fragment_dice=findViewById(R.id.dice2);
        endFrag=findViewById(R.id.endfragment);
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

        GameBoardFragment gameBoardFragment = GameBoardFragment.newInstance(name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView2, createLobbyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //TODO:  maybe use switch-case statement for easier change Fragment

    }

    private void startClientThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = new Client();
                try {

                    client.startClient();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (Exception e) {
                    //e.printStackTrace(); //Sensitive
                    client.handleException(e);
                }
            }
        }).start();
    }
    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, diceFragment);
        fragmentTransaction.commit();
    }

    public void showWinner() {
        String winnerName="Max Mustermann";
        EndGame_Fragment endFragment = EndGame_Fragment.newInstance(winnerName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, endFragment);
        fragmentTransaction.commit();
    }


    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}


