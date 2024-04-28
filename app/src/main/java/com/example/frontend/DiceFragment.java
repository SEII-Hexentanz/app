package com.example.frontend;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import at.aau.models.Request;
import at.aau.payloads.EmptyPayload;
import at.aau.values.CommandType;

public class DiceFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor accelerometer;
    private Dice dice;
    private ImageView diceImage;
    private Button continueButton, cheatButton;
    private TextView diceResult;
    private FragmentContainerView fragmentContainerView;

    private boolean isButtonLongPressed = false;
    private boolean isSensorCovered = false;
    private final static float lightSensorThreshold = 10.0f; // Schwellenwert für den Lichtsensor

    private final static float shakeThreshold = 10;
    private long lastUpdate = 0;
    private float lastX, lastY, lastZ;
    private boolean diceThrown = false;

    public DiceFragment() {
        //typical factory method constructor
    }

    public static DiceFragment newInstance() {
        DiceFragment diceFragment = new DiceFragment();
        Bundle args = new Bundle();
        diceFragment.setArguments(args);
        return diceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); // Lichtsensor hinzufügen
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        dice = new Dice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);

        findViews(view);
        onContinueClick();
        onCheatingClick();
        return view;
    }

    private void findViews(View view) {
        diceImage = view.findViewById(R.id.diceImage);
        continueButton = view.findViewById(R.id.continueButtonDiceFragment);
        cheatButton = view.findViewById(R.id.cheatButton);
        diceResult = view.findViewById(R.id.diceResult);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerView2);
    }

    private void onContinueClick() {
        continueButton.setOnClickListener(view -> {
            fragmentContainerView.setVisibility(View.VISIBLE);
            showGameBoardFragment();
        });
    }

    private void onCheatingClick(){
        cheatButton.setOnLongClickListener(v -> {
            isButtonLongPressed = true;
            checkAndPerformCheat();
            return true;
        });

        cheatButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isButtonLongPressed = false;
            }
            return false; // Let the event continue to the long click listener
        });
    }

    private void checkAndPerformCheat() {
        if (isButtonLongPressed && isSensorCovered) {
            dice.setDice(6);
            updateDiceImage(diceImage, 6);
            diceThrown = false;
            Log.i("cheat", "Set dice to 6 with sensor covered and button long pressed");
         //   Client.send(new Request(CommandType.CHEAT, new EmptyPayload()));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            isSensorCovered = lightValue < lightSensorThreshold;
            checkAndPerformCheat();
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && !diceThrown) {
            processAccelerometerInput(event);
        }
    }

    private void processAccelerometerInput(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdate) > 1000) {
            long diffTime = (currentTime - lastUpdate);
            lastUpdate = currentTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

            if (speed > shakeThreshold) {
                dice.useDice();
                updateDiceImage(diceImage, dice.getDice());
                diceThrown = true;
              //  Client.send(new Request(CommandType.DICE_ROLL, new EmptyPayload()));
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        diceImage.setImageResource(R.drawable.inital_dice); // Reset to initial dice image
        diceThrown = false;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this example
    }

    private void updateDiceImage(ImageView diceImage, int diceValue) {
        switch (diceValue) {
            case 1:
                diceImage.setImageResource(R.drawable.dice1);
                break;
            case 2:
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
                break;
            default:
                diceImage.setImageResource(R.drawable.inital_dice);
        }

    }

    private void showGameBoardFragment() {
        GameBoardFragment gameBoardFragment = new GameBoardFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content,gameBoardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
