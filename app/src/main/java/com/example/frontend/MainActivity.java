package com.example.frontend;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import at.aau.models.Request;
import at.aau.payloads.RegisterPayload;
import at.aau.values.CommandType;

public class MainActivity extends AppCompatActivity implements PropertyChangeListener {
    private final Client client = new Client();
    private Button btnStart;
    private TextView inputUsername;
    private TextView inputAge;
    private View createLobbyFragment;
    View rootView;

    private final String CREATE_LOBBY_FRAGMENT = "create_lobby";
    public MainActivity(){
        Game.INSTANCE.addPropertyChangeListener(this);
    }

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
        String responesAge = inputAge.getText().toString();

        Client.send(new Request(CommandType.REGISTER, new RegisterPayload(responseUser, Integer.parseInt(responesAge))));
        Log.i("ResponseAge", responesAge);

    }

    private TextWatcher createUserNameTextWatcher() {
        return new TextWatcher() {
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
        };
    }

    private TextWatcher createAgeTextWatcher() {
        return new TextWatcher() {
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
        };
    }

    private boolean isValidAge(String ageTxt) {
        if (ageTxt.isEmpty()) {
            return false;
        }
        int age = Integer.parseInt(ageTxt);
        return age >= 8 && age <= 99;
    }

    public String getUserName() {
        String name = inputUsername.getText().toString();
        return name;
    }

    public void saveUsernameToPreferences() {
        String name = getUserName();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", name);
        editor.apply();
    }


    public void showCreateLobbyFragment() {
        String name = getUserName();
        saveUsernameToPreferences();
        Game.INSTANCE.setPlayerName(name);

        CreateLobbyFragment createLobbyFragment = CreateLobbyFragment.newInstance(name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView2, createLobbyFragment);
        fragmentTransaction.addToBackStack(CREATE_LOBBY_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Log.i("App", "PropertyChangeEvent received: " + propertyChangeEvent.getPropertyName());
        if (propertyChangeEvent.getPropertyName().equals(Game.Property.USERNAME_ALREADY_EXISTS.name())) {
            runOnUiThread(() -> {
                Toast.makeText(this, "Name already in use", Toast.LENGTH_SHORT).show();
            });
        } else if (propertyChangeEvent.getPropertyName().equals(Game.Property.PLAYER_REGISTERED.name())) {

            runOnUiThread(() -> {
            Log.i("Change Fragement", "Fragment change event started");
            createLobbyFragment.setVisibility(View.VISIBLE);
            showCreateLobbyFragment();
        });
            }
    }
}


