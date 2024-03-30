package com.example.frontend.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.gamelogic.Dice;
import com.example.frontend.R;
import com.example.frontend.ui.viewmodels.DiceView;

public class DiceFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Dice dice;
    private ImageView diceImage;
    private Button continueButton;
    private DiceView diceView;

    private final float SHAKE_THRESHOLD = 2.7f;
    private final int SHAKE_WAIT_TIME_MS = 500;
    private long mShakeTime = 0;

    //TODO: add onCreate für server
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diceView = new ViewModelProvider(requireActivity()).get(DiceView.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);
        diceImage = view.findViewById(R.id.diceImage);
        continueButton = view.findViewById(R.id.continueButtonDiceFragment);

        dice = new Dice();

        continueButton.setOnClickListener(v -> {

            // TODO: Weitere navigation zu anderen fragments
            //diceView.setContinuePressed(true);
            diceView.setDices(dice);
            dice.useDice();
            updateDiceImage(dice.getDice());
            diceView.setDices(dice);


        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - mShakeTime) > SHAKE_WAIT_TIME_MS) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
                if (acceleration > SHAKE_THRESHOLD) {
                    mShakeTime = currentTime;
                    dice.useDice();
                    updateDiceImage(dice.getDice()); // Aktualisiert das Bild basierend auf dem Würfelergebnis
                }
            }
        }
    }

    private void updateDiceImage(int diceValue) {
        //TODO: add pictures of dices
        switch(diceValue){
            case 1:
                diceImage.setImageResource(R.drawable.dice1);
                break;
            /*case 2:
                diceImage.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceImage.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceImage.setImageResource(R.drawable.dice4);
                break;
            case 5:
                diceImage.setImageResource(R.drawable.dice5);
                break;
            case 6:
                diceImage.setImageResource(R.drawable.dice6);
                break;*/
        }
        // statt switch: int resId = getResources().getIdentifier("dice_" + diceNumber, "drawable", requireActivity().getPackageName());
        //        diceImage.setImageResource(resId);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Kann leer bleiben, falls nicht benötigt.
    }
}
