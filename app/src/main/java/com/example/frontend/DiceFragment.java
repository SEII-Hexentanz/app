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
    private Button continueButton;
    private TextView diceResult;
    private FragmentContainerView fragmentContainerView;

    private final float SHAKE_THRESHOLD = 10;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
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

        return view;
    }

    private void findViews(View view) {
        diceImage = view.findViewById(R.id.diceImage);
        continueButton = view.findViewById(R.id.continueButtonDiceFragment);
        diceResult = view.findViewById(R.id.diceResult);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerDice);
    }

    private void onContinueClick() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentContainerView.setVisibility(View.VISIBLE);
                showDiceFragment();


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

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                dice.useDice();
                updateDiceImage(diceImage, dice.getDice());
                if (diceResult != null) {
                    getActivity().runOnUiThread(() ->
                            diceResult.setText("Würfelergebnis: " + dice.getDice()));
                }

                diceThrown = true;
            }

            last_x = x;
            last_y = y;
            last_z = z;
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
    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainerDice, diceFragment);
        fragmentTransaction.commit();
    }
}