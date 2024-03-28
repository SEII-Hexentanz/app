package com.example.frontend;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private TextView inputUsername;
    private TextView inputAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViews();

        checkInputUsername(inputUsername);


        btnStart.setOnClickListener(new View.OnClickListener() {
            String responseUser = inputUsername.getText().toString();
            String responesAge = inputAge.getText().toString();


            public void onClick(View v) {
                Log.i("Change Fragement", "Fragment change event started");

            }
        });
    }

    void findViews() {
        btnStart = findViewById(R.id.buttonStart);
        inputUsername = findViewById(R.id.startScreenUsername);
        inputAge = findViewById(R.id.startScreenAge);
    }

    void checkInputUsername(TextView inputUsername) {
        inputUsername.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() < 3) {
                    inputUsername.setError("Inout should be at least 3 characters");
                } else {
                    inputUsername.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}


