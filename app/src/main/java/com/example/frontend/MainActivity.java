package com.example.frontend;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private TextView inputUsername;
    private TextView inputAge;
    private View createLobbyFragment;
    private View mainActivityView;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViews();

        checkInputUsername(inputUsername);

        checkInputAge(inputAge);


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
                    changeFragment();
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
    }

    void checkInputUsername(TextView inputUsername) {
        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //this method needs to be inherited from the TextWatcher Class
                throw new UnsupportedOperationException("beforeTextChanged method is not supported in this context.");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() < 3) {
                    inputUsername.setError("Input should be at least 3 characters");
                } else {
                    inputUsername.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //This method needs to be inherited by the TextWatcher Class
                throw new UnsupportedOperationException("beforeTextChanged method is not supported in this context.");
            }
        });
    }

    void checkInputAge(TextView inputAge) {
        inputAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This method needs to be inherited by the TextWatcher Class
                throw new UnsupportedOperationException("beforeTextChanged method is not supported in this context.");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidAge(charSequence.toString())) {
                    inputAge.setError("Age must be between 8 and 99.");
                } else {
                    inputAge.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //This method needs to be inherited by the TextWatcher Class
                throw new UnsupportedOperationException("beforeTextChanged method is not supported in this context.");
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

    public void changeFragment() {

        CreateLobbyFragment createLobbyFragment = new CreateLobbyFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView2, createLobbyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void startClientThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Hier kommt die Client-Logik (Verbindungsaufbau, Senden, Empfangen)
                    // Beispiel:
                    Client client = new Client();
                    client.startClient();

                    // Nach erfolgreichem Datenaustausch, UI im Hauptthread aktualisieren
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI-Änderungen, z.B. eine Toast-Nachricht anzeigen
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fehlerbehandlung, z.B. eine Fehlermeldung in der UI anzeigen
                }
            }
        }).start();
    }
}


