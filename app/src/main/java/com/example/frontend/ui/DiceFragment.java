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
import android.widget.TextView;

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
    private TextView diceResult;

    private final float SHAKE_THRESHOLD = 10;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private boolean diceThrown = false;

    public DiceFragment() {

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
        diceView = new ViewModelProvider(requireActivity()).get(DiceView.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);
        diceImage = view.findViewById(R.id.diceImage);
        continueButton = view.findViewById(R.id.continueButtonDiceFragment);
        diceResult= view.findViewById(R.id.diceResult);
        continueButton.setOnClickListener(v -> {
            diceView.setContinuePressed(true);
            diceView.setDices(dice);


        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                System.out.println("Würfel: " +dice.getDice());
                if(diceResult != null) {
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

    private void updateDiceImage(ImageView diceImage,int diceValue) {
        //TODO: add pictures of dices
        switch(diceValue){
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
            default: diceImage.setImageResource(R.drawable.inital_dice);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this example
    }
}
