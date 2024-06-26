package com.example.frontend;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import at.aau.models.Character;
import at.aau.models.Request;
import at.aau.payloads.CheatPayload;
import at.aau.payloads.EmptyPayload;
import at.aau.values.CommandType;

public class DiceFragment extends Fragment implements SensorEventListener, PropertyChangeListener {
    public static final String TAG = "DICE_FRAGMENT_TAG";
    public static final String APP_PREFS = "AppPrefs";
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor accelerometer;
    private ImageView diceImage;
    private Button continueButton, cheatButton;
    private TextView diceResult, currentPlayerName, closeButton;
    private FragmentContainerView fragmentContainerView;

    private boolean isButtonLongPressed = false;
    private boolean isSensorCovered = false;
    private final static float LIGHT_SENSOR_THRESHOLD = 10.0f; // Schwellenwert für den Lichtsensor

    private final static float SHAKE_THRESHOLD = 10;
    private long lastUpdate = 0;
    private float lastX, lastY, lastZ;
    private boolean diceThrown;

    public DiceFragment() {
        Game.INSTANCE.addPropertyChangeListener(this);
    }

    public static DiceFragment newInstance(int diceValue) {
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
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.diceThrown = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);

        findViews(view);
        displayCurrentPlayer();
        onCheatingClick();
        onCloseButtonClick();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViews(View view) {
        diceImage = view.findViewById(R.id.diceImage);
        continueButton = view.findViewById(R.id.continueButtonDiceFragment);
        cheatButton = view.findViewById(R.id.cheatButton);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerView2);
        currentPlayerName = view.findViewById(R.id.currentPlayer);
        closeButton = view.findViewById(R.id.closeButton);
    }

    private void onCloseButtonClick() {
        closeButton.setOnClickListener(v -> {
            diceImage.setVisibility(View.GONE);
            closeButton.setVisibility(View.GONE);
        });
    }

    private void onCheatingClick() {
        if (isCheatUsed()) {
            cheatButton.setEnabled(false);
        } else {
            cheatButton.setOnLongClickListener(v -> {
                isButtonLongPressed = true;
                checkAndPerformCheat();
                Log.i(TAG, "CHEATING CLICK");
                return true;
            });

            cheatButton.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isButtonLongPressed = false;
                }
                return false;
            });
        }
    }

    private void checkAndPerformCheat() {
        if (isButtonLongPressed && isSensorCovered) {
            updateDiceImage(diceImage, 6);
            diceThrown = false;
            Log.i(TAG, "Set dice to 6 with sensor covered and button long pressed");
            Client.send(new Request(CommandType.CHEAT, new EmptyPayload()));
            markCheatAsUsed();
            cheatButton.setEnabled(false);
            sendCheatUsedToServer();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            isSensorCovered = lightValue < LIGHT_SENSOR_THRESHOLD;
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

            if (speed > SHAKE_THRESHOLD) {
                if (!diceThrown) {
                    sendDiceRollRequestToServer();
                    diceThrown = true;
                }
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    private void sendDiceRollRequestToServer() {
        Client.send(new Request(CommandType.DICE_ROLL, new EmptyPayload()));
    }

    private void sendCheatUsedToServer() {
        CheatPayload cheatpayload = null;
        Client.send(new Request(CommandType.CHEAT_USED, new CheatPayload(true, cheatpayload.player())));
    }

    private void displayCurrentPlayer() {
        String name = getUsernameFromPreferences();
        if (name != null) {
            currentPlayerName.setText(name + " can roll the dice!");
        } else {
            currentPlayerName.setText("No player found");
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
        diceImage.setImageResource(R.drawable.inital_dice);
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

    public void setDiceToDefault() {
        diceImage.setImageResource(R.drawable.inital_dice);
    }

    public String getUsernameFromPreferences() {
        if (getContext() == null) {
            return "defaultUsername";
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "defaultUsername");
    }

    private boolean isCheatUsed() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("cheatUsed", false);
    }

    private void markCheatAsUsed() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("cheatUsed", true);
        editor.apply();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Log.i(TAG, "PropertyChangeEvent received: " + propertyChangeEvent.getPropertyName());
        if (!isAdded()) {
            return;
        }
        if (propertyChangeEvent.getPropertyName().equals(Game.Property.MOVE_CHARACTER.name())) {
            int diceValue = (int) propertyChangeEvent.getNewValue();
            requireActivity().runOnUiThread(() -> {
                diceRolledResult(diceValue);
                closeButton.setVisibility(View.VISIBLE);
            });
        }
    }

    private void diceRolledResult(int diceValue) {
        if (isAdded()) {
            updateDiceImage(diceImage, diceValue);
            Log.i(TAG, "dice roll ");
        }
    }
}
