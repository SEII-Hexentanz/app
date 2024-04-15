package com.example.frontend;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.LayoutInflater;
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

public class DiceFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Dice dice;
    private ImageView diceImage;
    private Button continueButton, cheatButton;
    private TextView diceResult;
    private FragmentContainerView fragmentContainerView;

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
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
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
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentContainerView.setVisibility(View.VISIBLE);
                showGameBoardFragment();
            }
        });
    }

    private void onCheatingClick(){
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dice.setDice(6);
                updateDiceImage(diceImage, 6);
                diceThrown = false;
                //add updating player position method
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        diceImage.setImageResource(R.drawable.inital_dice); // Reset to initial dice image
        diceThrown = false;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER || diceThrown)
            return;

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
                /*
                if (diceResult != null) {
                    getActivity().runOnUiThread(() ->
                            diceResult.setText("Würfelergebnis: " + dice.getDice()));
                }*/

                diceThrown = true;
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this example
    }

    private void showGameBoardFragment(){

        GameBoardFragment gameBoardFragment = new GameBoardFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content,gameBoardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}