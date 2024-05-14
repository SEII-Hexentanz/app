package com.example.frontend;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import at.aau.models.Request;
import at.aau.payloads.RegisterPayload;
import at.aau.values.CommandType;

public class MainActivity extends AppCompatActivity {
    private final Client client = new Client();
    private Button btnStart;
    private TextView inputUsername;
    private TextView inputAge;
    private View createLobbyFragment;
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client.start();
        findViews();
        setListeners();
    }

    void findViews() {
        btnStart = findViewById(R.id.buttonStart);
        inputUsername = findViewById(R.id.startScreenUsername);
        inputAge = findViewById(R.id.startScreenAge);
        createLobbyFragment = findViewById(R.id.fragmentContainerView2);
        rootView = findViewById(android.R.id.content);

    }

    private void setListeners() {
        rootView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });
        btnStart.setOnClickListener(v -> onClickStartButton());
        inputUsername.addTextChangedListener(createUserNameTextWatcher());
        inputAge.addTextChangedListener(createAgeTextWatcher());
    }

    private void onClickStartButton() {
        String responseUser = inputUsername.getText().toString();
        String responseAge = inputAge.getText().toString();

        Log.i("Change Fragement", "Fragment change event started");
        if (createLobbyFragment.getVisibility() == View.INVISIBLE) {
            createLobbyFragment.setVisibility(View.GONE);
        } else {
            createLobbyFragment.setVisibility(View.VISIBLE);
            showCreateLobbyFragment();

        }
        Client.send(new Request(CommandType.REGISTER, new RegisterPayload(responseUser, Integer.parseInt(responseAge))));
        Log.i("ResponseAge", responseAge);

    }

    private TextWatcher createUserNameTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //Nothing to do here
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
                // Nothing to do here
            }
        };
    }

    private TextWatcher createAgeTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //Nothing to do here
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
                // Nothing to do here
            }
        };
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
    }

    private void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


