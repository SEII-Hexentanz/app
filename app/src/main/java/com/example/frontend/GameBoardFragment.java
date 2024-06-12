
package com.example.frontend;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;


import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;


import at.aau.models.Character;
import at.aau.models.Request;
import at.aau.payloads.DicePayload;
import at.aau.payloads.PlayerMovePayload;
import at.aau.values.CharacterState;
import at.aau.values.CommandType;
import at.aau.values.MoveType;


public class GameBoardFragment extends Fragment implements GameEventListener, PropertyChangeListener {
    public static final String TAG = "GAMEBOARD_FRAGMENT_TAG"; //helps to find it
    private Button diceBtn;
    private TextView usernameTxt, timerText;
    private CountDownTimer countDownTimer;
    private FragmentContainerView diceFragment;

    private ImageView gameBoard;
    private float mScaleFactor;


    private HashMap<at.aau.values.Color, Integer> mapGoalPoint;
    private ScaleGestureDetector scaleGestureDetector;
    private long startTime = 0L;
    //private Handler timerHandler = new Handler();
    private long millisecondsTime = 0L;
    private long timeSwapBuff = 0L;
    private final long MAX_TIMER_DURATION = 15 * 60 * 1000; //1min=60_000 // 15 minutes
    private long remainingTime = MAX_TIMER_DURATION;
    private ArrayList<ImageView> gameboardPositions;
    private ArrayList<ImageView> btnYelloHome;
    private ArrayList<ImageView> btnRosaHome;
    private ArrayList<ImageView> btnRedHome;
    private ArrayList<ImageView> btnGreenHome;
    private ArrayList<ImageView> btnBlueHome;
    private ArrayList<ImageView> btnLilaHome;

    private ArrayList<ImageView> btnYellowGoal;
    private ArrayList<ImageView> btnRosaGoal;
    private ArrayList<ImageView> btnRedGoal;
    private ArrayList<ImageView> btnGreenGoal;
    private ArrayList<ImageView> btnBlueGoal;
    private ArrayList<ImageView> btnLilaGoal;
    private ArrayList<ImageView> playerHomePositions;
    private ArrayList<ImageView> playerGoalPositions;
    //ArrayList für jedes einzelne Home und jedes einzelne Goal am Feld

    private boolean witchRevealVal = false;
    private int stepCounter; //to get steps and if <36 --> move around
    public GameBoardFragment() {
        Game.INSTANCE.addPropertyChangeListener(this);
    }


    public static GameBoardFragment newInstance(String username) {
        GameBoardFragment fragment = new GameBoardFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Game.INSTANCE.setGameEventListener(this);
        //Display Gameboard only in Landscape Mode
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);
        Game.INSTANCE.mapStartPositions();
        Game.INSTANCE.setGameEventListener(this);
        Game.INSTANCE.initializePlayerPositions();
        findViews(view);
        setGameBoardUsername();
        onRollDiceClick();

        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());

        initializeGameBoard(view);
        initalizePlayerGoalPositons(view);
        initalizePlayerHomePositions(view);

        setPlayerHomePositions(Game.INSTANCE.FrontPlayer());
        getBoardContent(gameboardPositions);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Game.INSTANCE.isMyTurn()) {
            yourTurn();
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            gameBoard.setScaleX(mScaleFactor);
            gameBoard.setScaleY(mScaleFactor);
            return true;
        }
    }

    public String getUsernameFromPreferences() {
        if (getContext() == null) {
            return "defaultUsername"; // Return default or handle the error as appropriate
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "defaultUsername");
    }


    private void setGameBoardUsername() {
        if (usernameTxt != null) { // Add null check for safety
            String name = getUsernameFromPreferences();
            usernameTxt.setText(name);
            Log.i(TAG, "USERNAME: " + name);
        } else {
            Log.e(TAG, "usernameTxt is not initialized");
        }
    }


    private void onRollDiceClick() {
        diceBtn.setOnClickListener(view -> {
            diceFragment.setVisibility(View.VISIBLE);
            showDiceFragment();
            diceBtn.setEnabled(false);
        });
    }

    private void yourTurn() {
        if (diceBtn != null) {
            diceBtn.setEnabled(true);
        }
    }

    private void findViews(View view) {
        diceBtn = view.findViewById(R.id.btn_rollDice);
        usernameTxt = view.findViewById(R.id.txtViewUsername);
        timerText = view.findViewById(R.id.timerTextView);
        gameBoard = view.findViewById(R.id.gridLayoutGameBoard);
        diceFragment = view.findViewById(R.id.fragmentContainerDice);


        //Yellow
        btnYelloHome = new ArrayList<>();
        btnYelloHome.add(view.findViewById(R.id.btnHomeYellow1));
        btnYelloHome.add(view.findViewById(R.id.btnHomeYellow2));
        btnYelloHome.add(view.findViewById(R.id.btnHomeYellow3));
        btnYelloHome.add(view.findViewById(R.id.btnHomeYellow4));


        btnYellowGoal = new ArrayList<>();
        btnYellowGoal.add(view.findViewById(R.id.btnGoalYellow1));
        btnYellowGoal.add(view.findViewById(R.id.btnGoalYellow2));
        btnYellowGoal.add(view.findViewById(R.id.btnGoalYellow3));
        btnYellowGoal.add(view.findViewById(R.id.btnGoalYellow4));

        //Rosa
        btnRosaHome = new ArrayList<>();
        btnRosaHome.add(view.findViewById(R.id.btnHomeRosa1));
        btnRosaHome.add(view.findViewById(R.id.btnHomeRosa2));
        btnRosaHome.add(view.findViewById(R.id.btnHomeRosa3));
        btnRosaHome.add(view.findViewById(R.id.btnHomeRosa4));


        btnRosaGoal = new ArrayList<>();
        btnRosaGoal.add(view.findViewById(R.id.btnGoalRosa1));
        btnRosaGoal.add(view.findViewById(R.id.btnGoalRosa2));
        btnRosaGoal.add(view.findViewById(R.id.btnGoalRosa3));
        btnRosaGoal.add(view.findViewById(R.id.btnGoalRosa4));


        //Red
        btnRedHome = new ArrayList<>();
        btnRedHome.add(view.findViewById(R.id.btnHomeRed1));
        btnRedHome.add(view.findViewById(R.id.btnHomeRed2));
        btnRedHome.add(view.findViewById(R.id.btnHomeRed3));
        btnRedHome.add(view.findViewById(R.id.btnHomeRed4));

        btnRedGoal = new ArrayList<>();
        btnRedGoal.add(view.findViewById(R.id.btnGoalRed1));
        btnRedGoal.add(view.findViewById(R.id.btnGoalRed2));
        btnRedGoal.add(view.findViewById(R.id.btnGoalRed3));
        btnRedGoal.add(view.findViewById(R.id.btnGoalRed4));


        //Green
        btnGreenHome = new ArrayList<>();
        btnGreenHome.add(view.findViewById(R.id.btnHomeGreen1));
        btnGreenHome.add(view.findViewById(R.id.btnHomeGreen2));
        btnGreenHome.add(view.findViewById(R.id.btnHomeGreen3));
        btnGreenHome.add(view.findViewById(R.id.btnHomeGreen4));


        btnGreenGoal = new ArrayList<>();

        btnGreenGoal.add(view.findViewById(R.id.btnGoalGreen1));
        btnGreenGoal.add(view.findViewById(R.id.btnGoalGreen2));
        btnGreenGoal.add(view.findViewById(R.id.btnGoalGreen3));
        btnGreenGoal.add(view.findViewById(R.id.btnGoalGreen4));


        //Blue
        btnBlueHome = new ArrayList<>();
        btnBlueHome.add(view.findViewById(R.id.btnHomeBlue1));
        btnBlueHome.add(view.findViewById(R.id.btnHomeBlue2));
        btnBlueHome.add(view.findViewById(R.id.btnHomeBlue3));
        btnBlueHome.add(view.findViewById(R.id.btnHomeBlue4));

        btnBlueGoal = new ArrayList<>();
        btnBlueGoal.add(view.findViewById(R.id.btnGoalBlue1));
        btnBlueGoal.add(view.findViewById(R.id.btnGoalBlue2));
        btnBlueGoal.add(view.findViewById(R.id.btnGoalBlue3));
        btnBlueGoal.add(view.findViewById(R.id.btnGoalBlue4));


        //LILA aka Dark_Blue

        btnLilaHome = new ArrayList<>();
        btnLilaHome.add(view.findViewById(R.id.btnHomeLila1));
        btnLilaHome.add(view.findViewById(R.id.btnHomeLila2));
        btnLilaHome.add(view.findViewById(R.id.btnHomeLila3));
        btnLilaHome.add(view.findViewById(R.id.btnHomeLila4));

        btnLilaGoal = new ArrayList<>();
        btnLilaGoal.add(view.findViewById(R.id.btnGoalLila1));
        btnLilaGoal.add(view.findViewById(R.id.btnGoalLila2));
        btnLilaGoal.add(view.findViewById(R.id.btnGoalLila3));
        btnLilaGoal.add(view.findViewById(R.id.btnGoalLila4));


    }

    private void setPlayerHomePositions(SortedSet<Player> players) {
        for (Player player : players) {
            Log.i("GameBoardFragment", "Set PlayerHomePositions");
            switch (player.color()) {
                case YELLOW -> {

                    btnYelloHome.get(0).setImageResource(R.drawable.playericon);
                    btnYelloHome.get(1).setImageResource(R.drawable.playericon);
                    btnYelloHome.get(2).setImageResource(R.drawable.playericon);
                    btnYelloHome.get(3).setImageResource(R.drawable.playericon);
                }

                case PINK -> {

                    btnRosaHome.get(0).setImageResource(R.drawable.playericon);
                    btnRosaHome.get(1).setImageResource(R.drawable.playericon);
                    btnRosaHome.get(2).setImageResource(R.drawable.playericon);
                    btnRosaHome.get(3).setImageResource(R.drawable.playericon);


                }
                case RED -> {

                    btnRedHome.get(0).setImageResource(R.drawable.playericon);
                    btnRedHome.get(1).setImageResource(R.drawable.playericon);
                    btnRedHome.get(2).setImageResource(R.drawable.playericon);
                    btnRedHome.get(3).setImageResource(R.drawable.playericon);


                }

                case GREEN -> {

                    btnRedHome.get(0).setImageResource(R.drawable.playericon);
                    btnRedHome.get(1).setImageResource(R.drawable.playericon);
                    btnRedHome.get(2).setImageResource(R.drawable.playericon);
                    btnRedHome.get(3).setImageResource(R.drawable.playericon);

                }
                case LIGHT_BLUE -> {

                    btnBlueHome.get(0).setImageResource(R.drawable.playericon);
                    btnBlueHome.get(1).setImageResource(R.drawable.playericon);
                    btnBlueHome.get(2).setImageResource(R.drawable.playericon);
                    btnBlueHome.get(3).setImageResource(R.drawable.playericon);
                }

                case DARK_BLUE -> {

                    btnLilaHome.get(0).setImageResource(R.drawable.playericon);
                    btnLilaHome.get(1).setImageResource(R.drawable.playericon);
                    btnLilaHome.get(2).setImageResource(R.drawable.playericon);
                    btnLilaHome.get(3).setImageResource(R.drawable.playericon);
                }
            }
        }
    }

    private void moveCharacterToStartingPosition() {

        if (gameboardPositions.isEmpty()) {
            throw new NullPointerException("Gameboard is not initalized yet.");
        } else {
            Character c = Game.INSTANCE.getNextCharacterForStart();
            if (c != null) {
                Game.INSTANCE.moveCharacterToStartingPostion(c);

            } else {
                Toast.makeText(requireContext(), "No character in HOME left", Toast.LENGTH_SHORT).show();
                showDialoge();
            }
        }
    }
    private void moveCharacterOnField(Character c, int oldPosition, MoveType moveType) {
        Log.d("App", "Character " + c.id() + " gets set to position " + c.position() + " from " + oldPosition);
        gameboardPositions.get(c.position()).setImageResource(R.drawable.playericon);

        gameboardPositions.get(c.position()).setOnClickListener(v -> {
            //send move request to server
            doCharacterAction(c);
        });
        setStepCounter(Math.abs(c.position()-oldPosition));
        Log.i(TAG,"Stepcounter" + stepCounter);

        if (moveType.equals(MoveType.MOVE_ON_FIELD) || moveType.equals(MoveType.MOVE_TO_GOAL)) {
            Log.d("App", "Character " + c.id() + " gets hidden on old position " + oldPosition);
            gameboardPositions.get(oldPosition).setImageResource(R.drawable.ic_launcher_background);
            gameboardPositions.get(oldPosition).setOnClickListener(v -> {
                //do nothing
            });
        }
    }

    private void doCharacterAction(Character c) {
        if(Game.INSTANCE.isMyTurn()) {

            if (witchRevealVal == true) {
                Log.i("App", "reveal witch");
            } else {

                //move command
                Game.INSTANCE.sendMoveOnFieldRequest(c);
            }
        }
    }

    private Character[] getCharacters(Player player) {
        if (player.characters != null) {
            List<Character> characterList = player.characters;
            return characterList.toArray(new Character[0]);
        }

        return new Character[0];
    }


    private void setGoalPositions(SortedSet<Player> players) {
        for (Player player : players) {
            switch (player.color()) {
                case YELLOW -> {
                    btnYellowGoal.get(0).setImageResource(R.drawable.playericon);
                    btnYellowGoal.get(1).setImageResource(R.drawable.playericon);
                    btnYellowGoal.get(2).setImageResource(R.drawable.playericon);
                    btnYellowGoal.get(3).setImageResource(R.drawable.playericon);
                }

                case PINK -> {
                    btnRosaGoal.get(0).setImageResource(R.drawable.playericon);
                    btnRosaGoal.get(1).setImageResource(R.drawable.playericon);
                    btnRosaGoal.get(2).setImageResource(R.drawable.playericon);
                    btnRosaGoal.get(3).setImageResource(R.drawable.playericon);
                }
                case RED -> {
                    btnRedGoal.get(0).setImageResource(R.drawable.playericon);
                    btnRedGoal.get(1).setImageResource(R.drawable.playericon);
                    btnRedGoal.get(2).setImageResource(R.drawable.playericon);
                    btnRedGoal.get(3).setImageResource(R.drawable.playericon);
                }

                case GREEN -> {
                    btnGreenGoal.get(0).setImageResource(R.drawable.playericon);
                    btnGreenGoal.get(1).setImageResource(R.drawable.playericon);
                    btnGreenGoal.get(2).setImageResource(R.drawable.playericon);
                    btnGreenGoal.get(3).setImageResource(R.drawable.playericon);
                }
                case LIGHT_BLUE -> {
                    btnBlueGoal.get(0).setImageResource(R.drawable.playericon);
                    btnBlueGoal.get(1).setImageResource(R.drawable.playericon);
                    btnBlueGoal.get(2).setImageResource(R.drawable.playericon);
                    btnBlueGoal.get(3).setImageResource(R.drawable.playericon);
                }

                case DARK_BLUE -> {
                    btnLilaGoal.get(0).setImageResource(R.drawable.playericon);
                    btnLilaGoal.get(1).setImageResource(R.drawable.playericon);
                    btnLilaGoal.get(2).setImageResource(R.drawable.playericon);
                    btnLilaGoal.get(3).setImageResource(R.drawable.playericon);
                }
            }
        }
    }


    void mapGoalPositions() {
        mapGoalPoint = new HashMap<>();
        mapGoalPoint.put(at.aau.values.Color.YELLOW, 26);
        mapGoalPoint.put(at.aau.values.Color.PINK, 32);
        mapGoalPoint.put(at.aau.values.Color.RED, 6);
        mapGoalPoint.put(at.aau.values.Color.GREEN, 20);
        mapGoalPoint.put(at.aau.values.Color.LIGHT_BLUE, 0);
        mapGoalPoint.put(at.aau.values.Color.DARK_BLUE, 29);
    }

    private void initalizePlayerHomePositions(View view) {
        playerHomePositions = new ArrayList<>();
        playerHomePositions.add(view.findViewById(R.id.btnHomeRosa1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeRosa2));
        playerHomePositions.add(view.findViewById(R.id.btnHomeRosa3));
        playerHomePositions.add(view.findViewById(R.id.btnHomeRosa4));

        playerHomePositions.add(view.findViewById(R.id.btnHomeLila1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeLila2));
        playerHomePositions.add(view.findViewById(R.id.btnHomeLila3));
        playerHomePositions.add(view.findViewById(R.id.btnHomeLila4));

        playerHomePositions.add(view.findViewById(R.id.btnHomeGreen1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeGreen2));
        playerHomePositions.add(view.findViewById(R.id.btnHomeGreen3));
        playerHomePositions.add(view.findViewById(R.id.btnHomeGreen4));

        playerHomePositions.add(view.findViewById(R.id.btnHomeBlue1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeBlue2));
        playerHomePositions.add(view.findViewById(R.id.btnHomeBlue3));
        playerHomePositions.add(view.findViewById(R.id.btnHomeBlue4));

        playerHomePositions.add(view.findViewById(R.id.btnHomeYellow1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeYellow1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeYellow1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeYellow1));

        playerHomePositions.add(view.findViewById(R.id.btnHomeRed1));
        playerHomePositions.add(view.findViewById(R.id.btnHomeRed2));
        playerHomePositions.add(view.findViewById(R.id.btnHomeRed3));
        playerHomePositions.add(view.findViewById(R.id.btnHomeRed4));
    }

    private void initalizePlayerGoalPositons(View view) {
        playerGoalPositions = new ArrayList<>();

        playerGoalPositions.add(view.findViewById(R.id.btnGoalRosa1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalRosa2));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalRosa3));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalRosa4));

        playerGoalPositions.add(view.findViewById(R.id.btnGoalLila1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalLila2));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalLila3));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalLila4));

        playerGoalPositions.add(view.findViewById(R.id.btnGoalGreen1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalGreen2));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalGreen3));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalGreen4));

        playerGoalPositions.add(view.findViewById(R.id.btnGoalBlue1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalBlue2));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalBlue3));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalBlue4));

        playerGoalPositions.add(view.findViewById(R.id.btnGoalYellow1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalYellow1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalYellow1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalYellow1));

        playerGoalPositions.add(view.findViewById(R.id.btnGoalRed1));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalRed2));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalRed3));
        playerGoalPositions.add(view.findViewById(R.id.btnGoalRed4));

    }

    private void initializeGameBoard(View view) {
        gameboardPositions = new ArrayList<>();
        gameboardPositions.add(view.findViewById(R.id.gameboardpos0));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos1));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos2));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos3));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos4));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos5));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos6));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos7));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos8));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos9));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos10));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos11));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos12));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos13));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos14));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos15));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos16));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos17));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos18));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos19));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos20));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos21));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos22));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos23));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos24));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos25));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos26));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos27));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos28));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos29));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos30));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos31));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos32));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos33));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos34));
        gameboardPositions.add(view.findViewById(R.id.gameboardpos35));

        mapGoalPositions();
        Game.INSTANCE.mapStartPositions();


    }

    private void getBoardContent(ArrayList<ImageView> list) {
        Log.i(TAG, "Board Content: " + String.valueOf(list.size()));
    }

    /*private Runnable updateTimeRunnable = new Runnable() {
        public void run() {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            remainingTime -= elapsedRealtime - startTime;
            startTime = elapsedRealtime;
            if (remainingTime > 0) {
                int seconds = (int) (remainingTime / 1000);
                int minutes = seconds / 60;
                seconds %= 60;
                timerText.setText(String.format("%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
                Log.i(TAG,"TIMER RUN");
            } else {
                timerHandler.removeCallbacks(this);
                showEndGameFragment();
            }
        }
    };

    private void startTimer() {
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(updateTimeRunnable, 0);
    }

    private void pauseTimer() {
        timeSwapBuff = timeSwapBuff + millisecondsTime;

        timerHandler.removeCallbacks(updateTimeRunnable);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        remainingTime -= elapsedRealtime - startTime;
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("remainingTime", remainingTime);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            remainingTime = savedInstanceState.getLong("remainingTime", MAX_TIMER_DURATION);
        }
    }

    private void showDiceFragment() {
        DiceFragment diceFragment = new DiceFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.gameBoardID, diceFragment);
        fragmentTransaction.commit();
    }


    private void showEndGameFragment() {
        EndGameFragment endGameFragment = new EndGameFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, endGameFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPlayerPositionChanged(com.example.frontend.Player player, int oldPosition, int newPosition) {
        requireActivity().runOnUiThread(() -> {
            if (oldPosition >= 0 && oldPosition < gameboardPositions.size() && newPosition >= 0 && newPosition < gameboardPositions.size()) {
                ImageView oldImageView = gameboardPositions.get(oldPosition);
                ImageView newImageView = gameboardPositions.get(newPosition);
                updateImageViews(oldImageView, newImageView, player);
            } else {
                Log.e(TAG, "Invalid position(s): oldPosition=" + oldPosition + ", newPosition=" + newPosition);
            }
        });
    }

    public void updateImageViews(ImageView oldImageView, ImageView newImageView, com.example.frontend.Player player) {
        // int playerIcon = getPlayerIcon(player);

        if (oldImageView != null) {
            oldImageView.setImageDrawable(null); // clear old position
        }
        if (newImageView != null) {
            //set defaultPlayerIcon because the color of the witch should not matter if part of reveal!
            newImageView.setImageResource(R.drawable.playericon); // set new position
        }
    }

    //necessary methdo for revealWitch
    private int getPlayerIcon(com.example.frontend.Player player) {
        // Return the drawable resource id based on player details
        return switch (player.color()) {
            case YELLOW -> R.drawable.yellowhat;
            case PINK -> R.drawable.pinkhat;
            case RED -> R.drawable.redhat;
            case GREEN -> R.drawable.greenhat;
            case LIGHT_BLUE -> R.drawable.lightbluehat;
            case DARK_BLUE -> R.drawable.bluehat;

        };
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set screen orientation to landscape when GameBoardFragment is resumed
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (remainingTime > 0) {
            //startTimer();
            Log.i("App", "startTimer");
        }
        Game.INSTANCE.setGameEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Reset screen orientation to portrait when GameBoardFragment is paused
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //pauseTimer();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Log.i("GameBoard", "PropertyChangeEvent received: " + propertyChangeEvent.getPropertyName());
        if (!isAdded()) {
            // Fragment is not attached, skip this event
            return;
        }
        if (propertyChangeEvent.getPropertyName().equals(Game.Property.MOVE_CHARACTER.name())) {
            int diceValue = (int) propertyChangeEvent.getNewValue();
            requireActivity().runOnUiThread(() -> diceRolled(diceValue));
        } else if (propertyChangeEvent.getPropertyName().equals(Game.Property.DICE_ROLLED.name())) {
            DicePayload payload = (DicePayload) propertyChangeEvent.getNewValue();
            requireActivity().runOnUiThread(() -> diceRolled(payload));
        } else if (propertyChangeEvent.getPropertyName().equals(Game.Property.YOUR_TURN.name())) {
            requireActivity().runOnUiThread(this::yourTurn);
        } else if(propertyChangeEvent.getPropertyName().equals(Game.Property.UPDATE_CHARACTER_POSITION.name())){
            Log.d("App", "Update of character position starts now");
            requireActivity().runOnUiThread(() -> updateCharacterPosition((UpdatePositionObject) propertyChangeEvent.getNewValue()));
        }
    }

    private void updateCharacterPosition(UpdatePositionObject upo) {
        if(upo.getMoveType().equals(MoveType.MOVE_TO_FIELD)){
            Log.i("App", "Character will be moved from home to field");
            moveCharacterToField(upo);
        }else if(upo.getMoveType().equals(MoveType.MOVE_ON_FIELD)){
            Log.i("App", "Character will be moved on field");
            moveCharacterOnField(upo.getCharacter(), upo.getOldPosition(), upo.getMoveType());
        } else if (upo.getMoveType().equals(MoveType.MOVE_TO_GOAL)) {
            Log.i("App", "Character will be moved from field to goal");
            moveCharacterToGoal(upo);
        }
    }

    private void moveCharacterToGoal(UpdatePositionObject upo){
        Log.i(TAG,"Character gets relocated from field to goal");
        moveCharacterOnField(upo.getCharacter(),upo.getOldPosition(),upo.getMoveType());
        /**
         TODO:
         //add counter when player moved 36 so it can move to goal summiert oldPositions
         // dice summer auf feld summieren und wenn == 27 und gleicher Spieler mit gleicher GoalFarbe kann er ins Goal
         ///setGoalPositions --> gibt Goal Positions von SPieler aus
         // add to propertyChange and call properly method
         **/

        Log.i(TAG,"Character will be on goal");
        int currentPosition = upo.getCharacter().position();
        int goalPosition = mapGoalPoint.get(upo.getPlayer().color());
        Log.i(TAG,"Info for current Position " + currentPosition + ", goalPosition " + goalPosition);
        Log.i(TAG, "Stepcounter " + getStepCounter());
        if (getStepCounter() >= 36){
            if(Math.abs(goalPosition-currentPosition) <=6){
                switch(upo.getPlayer().color()){
                    case YELLOW:
                        moveToGoalPosition(upo,btnYellowGoal);
                        break;
                    case PINK:
                        moveToGoalPosition(upo,btnRosaGoal);
                        break;
                    case RED:
                        moveToGoalPosition(upo,btnRedGoal);
                        break;
                    case GREEN:
                        moveToGoalPosition(upo, btnGreenGoal);
                        break;
                    case LIGHT_BLUE:
                        moveToGoalPosition(upo,btnBlueGoal);
                        break;
                    case DARK_BLUE:
                        moveToGoalPosition(upo, btnLilaGoal);
                        break;
                }
                Log.i(TAG, "Character moved to goal");
            }else{
                Log.i(TAG,"Character not near Goal");
            }
        }else{
            moveCharacterOnField(upo.getCharacter(), upo.getOldPosition(), upo.getMoveType());
        }
    }

    private void moveToGoalPosition(UpdatePositionObject upo, ArrayList<ImageView> goalPositions) {
        for (ImageView goalPosition : goalPositions) {
            if (goalPosition.getDrawable() == null) {
                goalPosition.setImageResource(R.drawable.playericon);
                Log.i(TAG, "Character moved to goal position");
                return;
            }
        }
    }

    private void moveCharacterToField(UpdatePositionObject upo) {
        Log.i("App", "Character gets relocated from home to field");
        moveCharacterOnField(upo.getCharacter(), upo.getOldPosition(), upo.getMoveType());

        Log.i("App", "Character will be hidden from home");
        switch (upo.getPlayer().color()){
            case YELLOW:
                hideNextCharacterInHome(btnYelloHome);
                break;
            case PINK:
                hideNextCharacterInHome(btnRosaHome);
                break;
            case RED:
                hideNextCharacterInHome(btnRedHome);
                break;
            case GREEN:
                hideNextCharacterInHome(btnGreenHome);
                break;
            case LIGHT_BLUE:
                hideNextCharacterInHome(btnBlueHome);
                break;
            case DARK_BLUE:
                hideNextCharacterInHome(btnLilaHome);
                break;
        }
    }

    private void hideNextCharacterInHome(ArrayList<ImageView> home) {
        for(ImageView i:home){
            if(i.getVisibility() == View.VISIBLE){
                i.setVisibility(View.INVISIBLE);
                Log.d("App", "Character hidden in home");
                return;
            }
        }
    }



    private void showDialoge() {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.dialog_layout);

        Button moveToStartBtn = dialog.findViewById(R.id.moveToStart);
        Button revealWitchBtn = dialog.findViewById(R.id.revealCharcter);
        Button moveOnFieldBtn = dialog.findViewById(R.id.moveOnField);

        moveOnFieldBtn.setOnClickListener(v -> {
            // Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload()));
            dialog.dismiss();
            Log.i("App", "Move Command  will be sent now");
        });
        revealWitchBtn.setOnClickListener(v -> {
            witchRevealVal = true;
            revealWitchFunct();
            dialog.dismiss();
            Log.i("App", "RevealWitch Request will be sent now + move request will be sent then");
        });

        moveToStartBtn.setOnClickListener(v -> {
            //send request to server
            //Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload()));
            moveCharacterToStartingPosition();
            dialog.dismiss();
            Log.i("App", "MoveToStart Request will be sent now");
        });

        dialog.show();
    }
    private void diceRolled(int diceValue) {
        if (!isAdded()) {
            Toast.makeText(requireContext(), "Your dice cannot be rolled", Toast.LENGTH_SHORT).show();
        } else {
            if (diceValue == 6) {
                showDialoge();
            }
            Toast.makeText(requireContext(), "Your dice has been rolled", Toast.LENGTH_SHORT).show();
        }
    }
    private void diceRolled(DicePayload payload) {
        if (isAdded()) {
            Toast.makeText(requireContext(), "Player" + payload.player() + " has rolled " + payload.diceValue(), Toast.LENGTH_SHORT).show();
            Log.i("App", payload.player() + ": " + payload.diceValue());
        }
    }
    private void revealWitchFunct() {
        Log.i("App", "Reveal Witch Function");
    }

    public int getStepCounter() {
        return stepCounter;
    }

    // Setter for stepCounter
    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }
}