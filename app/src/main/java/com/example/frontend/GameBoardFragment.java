
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


public class GameBoardFragment extends Fragment implements GameEventListener, PropertyChangeListener {
    public static final String TAG = "GAMEBOARD_FRAGMENT_TAG"; //helps to find it
    private Button diceBtn;
    private TextView usernameTxt, timerText;
    private CountDownTimer countDownTimer;
    private FragmentContainerView diceFragment;

    private ImageView gameBoard;
    private float mScaleFactor;

    private ImageView btnGreenHome1;
    private ImageView btnGreenHome2;
    private ImageView btnGreenHome3;
    private ImageView btnGreenHome4;

    private ImageView btnRedHome1;
    private ImageView btnRedHome2;
    private ImageView btnRedHome3;
    private ImageView btnRedHome4;

    private ImageView btnBlueHome1;
    private ImageView btnBlueHome2;
    private ImageView btnBlueHome3;
    private ImageView btnBlueHome4;


    private ImageView btnLilaHome1;
    private ImageView btnLilaHome2;
    private ImageView btnLilaHome3;
    private ImageView btnLilaHome4;

    private ImageView btnRosaHome1;
    private ImageView btnRosaHome2;
    private ImageView btnRosaHome3;
    private ImageView btnRosaHome4;

    private ImageView btnYellowHome1;
    private ImageView btnYellowHome2;
    private ImageView btnYellowHome3;
    private ImageView btnYellowHome4;

    private ImageView btnGreenGoal1;
    private ImageView btnGreenGoal2;
    private ImageView btnGreenGoal3;
    private ImageView btnGreenGoal4;

    private ImageView btnRedGoal1;
    private ImageView btnRedGoal2;
    private ImageView btnRedGoal3;
    private ImageView btnRedGoal4;

    private ImageView btnBlueGoal1;
    private ImageView btnBlueGoal2;
    private ImageView btnBlueGoal3;
    private ImageView btnBlueGoal4;


    private ImageView btnLilaGoal1;
    private ImageView btnLilaGoal2;
    private ImageView btnLilaGoal3;
    private ImageView btnLilaGoal4;

    private ImageView btnRosaGoal1;
    private ImageView btnRosaGoal2;
    private ImageView btnRosaGoal3;
    private ImageView btnRosaGoal4;

    private ImageView btnYellowGoal1;
    private ImageView btnYellowGoal2;
    private ImageView btnYellowGoal3;
    private ImageView btnYellowGoal4;


    private HashMap<at.aau.values.Color, Integer> mapGoalPoint;
    private ScaleGestureDetector scaleGestureDetector;
    private long startTime = 0L;
    private Handler timerHandler = new Handler();
    private long millisecondsTime = 0L;
    private long timeSwapBuff = 0L;
    private final long MAX_TIMER_DURATION = 15 * 60 * 1000; //1min=60_000 // 15 minutes
    private long remainingTime = MAX_TIMER_DURATION;
    private ArrayList<ImageView> gameboardPositions;

    private ArrayList<ImageView> playerHomePositions;

    private ArrayList<ImageView> playerGoalPositions;

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

        btnGreenHome1 = view.findViewById(R.id.btnHomeGreen1);
        btnGreenHome2 = view.findViewById(R.id.btnHomeGreen2);
        btnGreenHome3 = view.findViewById(R.id.btnHomeGreen3);
        btnGreenHome4 = view.findViewById(R.id.btnHomeGreen4);

        btnRedHome1 = view.findViewById(R.id.btnHomeRed1);
        btnRedHome2 = view.findViewById(R.id.btnHomeRed2);
        btnRedHome3 = view.findViewById(R.id.btnHomeRed3);
        btnRedHome4 = view.findViewById(R.id.btnHomeRed4);

        btnBlueHome1 = view.findViewById(R.id.btnHomeBlue1);
        btnBlueHome2 = view.findViewById(R.id.btnHomeBlue2);
        btnBlueHome3 = view.findViewById(R.id.btnHomeBlue3);
        btnBlueHome4 = view.findViewById(R.id.btnHomeBlue4);

        btnLilaHome1 = view.findViewById(R.id.btnHomeLila1);
        btnLilaHome2 = view.findViewById(R.id.btnHomeLila2);
        btnLilaHome3 = view.findViewById(R.id.btnHomeLila3);
        btnLilaHome4 = view.findViewById(R.id.btnHomeLila4);

        btnRosaHome1 = view.findViewById(R.id.btnHomeRosa1);
        btnRosaHome2 = view.findViewById(R.id.btnHomeRosa2);
        btnRosaHome3 = view.findViewById(R.id.btnHomeRosa3);
        btnRosaHome4 = view.findViewById(R.id.btnHomeRosa4);

        btnYellowHome1 = view.findViewById(R.id.btnHomeYellow1);
        btnYellowHome2 = view.findViewById(R.id.btnHomeYellow2);
        btnYellowHome3 = view.findViewById(R.id.btnHomeYellow3);
        btnYellowHome4 = view.findViewById(R.id.btnHomeYellow4);


        btnGreenGoal1 = view.findViewById(R.id.btnGoalGreen1);
        btnGreenGoal2 = view.findViewById(R.id.btnGoalGreen2);
        btnGreenGoal3 = view.findViewById(R.id.btnGoalGreen3);
        btnGreenGoal4 = view.findViewById(R.id.btnGoalGreen4);

        btnRedGoal1 = view.findViewById(R.id.btnGoalRed1);
        btnRedGoal2 = view.findViewById(R.id.btnGoalRed2);
        btnRedGoal3 = view.findViewById(R.id.btnGoalRed3);
        btnRedGoal4 = view.findViewById(R.id.btnGoalRed4);

        btnBlueGoal1 = view.findViewById(R.id.btnGoalBlue1);
        btnBlueGoal2 = view.findViewById(R.id.btnGoalBlue2);
        btnBlueGoal3 = view.findViewById(R.id.btnGoalBlue3);
        btnBlueGoal4 = view.findViewById(R.id.btnGoalBlue4);

        btnLilaGoal1 = view.findViewById(R.id.btnGoalLila1);
        btnLilaGoal2 = view.findViewById(R.id.btnGoalLila2);
        btnLilaGoal3 = view.findViewById(R.id.btnGoalLila3);
        btnLilaGoal4 = view.findViewById(R.id.btnGoalLila4);

        btnRosaGoal1 = view.findViewById(R.id.btnGoalRosa1);
        btnRosaGoal2 = view.findViewById(R.id.btnGoalRosa2);
        btnRosaGoal3 = view.findViewById(R.id.btnGoalRosa3);
        btnRosaGoal4 = view.findViewById(R.id.btnGoalRosa4);

        btnYellowGoal1 = view.findViewById(R.id.btnGoalYellow1);
        btnYellowGoal2 = view.findViewById(R.id.btnGoalYellow2);
        btnYellowGoal3 = view.findViewById(R.id.btnGoalYellow3);
        btnYellowGoal4 = view.findViewById(R.id.btnGoalYellow4);


    }


    private void setPlayerHomePositions(SortedSet<Player> players) {
        for (Player player : players) {
            Log.i("GameBoardFragment", "Set PlayerHomePositions");
            switch (player.color()) {
                case YELLOW -> {
                    Character[] characters = getCharacters(player);

                    btnYellowHome1.setImageResource(R.drawable.playericon);
                    btnYellowHome2.setImageResource(R.drawable.playericon);
                    btnYellowHome3.setImageResource(R.drawable.playericon);
                    btnYellowHome4.setImageResource(R.drawable.playericon);

                    btnYellowHome1.setOnClickListener(v -> {
                        moveToStartCharacter(characters[0]);
                    });
                    btnYellowHome2.setOnClickListener(v -> {
                        moveToStartCharacter(characters[1]);
                    });
                    btnYellowHome3.setOnClickListener(v -> {
                        moveToStartCharacter(characters[2]);
                    });
                    btnYellowHome4.setOnClickListener(v -> {
                        moveToStartCharacter(characters[3]);
                    });
                }

                case PINK -> {
                       Character[] characters = getCharacters(player);

                    btnRosaHome1.setImageResource(R.drawable.playericon);
                    btnRosaHome2.setImageResource(R.drawable.playericon);
                    btnRosaHome3.setImageResource(R.drawable.playericon);
                    btnRosaHome4.setImageResource(R.drawable.playericon);

                  btnRosaHome1.setOnClickListener(v -> {
                        moveToStartCharacter(characters[0]);});
                    btnRosaHome2.setOnClickListener(v -> {
                        moveToStartCharacter(characters[1]);});
                    btnRosaHome3.setOnClickListener(v -> {
                        moveToStartCharacter(characters[2]);});
                    btnRosaHome4.setOnClickListener(v -> {
                        moveToStartCharacter(characters[3]);});


                }
                case RED -> {
                    Character[] characters = getCharacters(player);

                    btnRedHome1.setImageResource(R.drawable.playericon);
                    btnRedHome2.setImageResource(R.drawable.playericon);
                    btnRedHome3.setImageResource(R.drawable.playericon);
                    btnRedHome4.setImageResource(R.drawable.playericon);

                    btnRedHome1.setOnClickListener(v -> {
                        moveToStartCharacter(characters[0]);});
                    btnRedHome2.setOnClickListener(v -> {
                        moveToStartCharacter(characters[1]);});
                    btnRedHome3.setOnClickListener(v -> {
                        moveToStartCharacter(characters[2]);});
                    btnRedHome4.setOnClickListener(v -> {
                        moveToStartCharacter(characters[3]);});
                }

                case GREEN -> {
                    Character[] characters = getCharacters(player);

                    btnGreenHome1.setImageResource(R.drawable.playericon);
                    btnGreenHome2.setImageResource(R.drawable.playericon);
                    btnGreenHome3.setImageResource(R.drawable.playericon);
                    btnGreenHome4.setImageResource(R.drawable.playericon);

                    btnGreenHome1.setOnClickListener(v -> {
                        moveToStartCharacter(characters[0]);});
                    btnGreenHome2.setOnClickListener(v -> {
                        moveToStartCharacter(characters[1]);});
                    btnGreenHome3.setOnClickListener(v -> {
                        moveToStartCharacter(characters[2]);});
                    btnGreenHome4.setOnClickListener(v -> {
                        moveToStartCharacter(characters[3]);});
                }
                case LIGHT_BLUE -> {
                    Character[] characters = getCharacters(player);

                    btnBlueHome1.setImageResource(R.drawable.playericon);
                    btnBlueHome2.setImageResource(R.drawable.playericon);
                    btnBlueHome3.setImageResource(R.drawable.playericon);
                    btnBlueHome4.setImageResource(R.drawable.playericon);

                    btnBlueHome1.setOnClickListener(v -> {
                        moveToStartCharacter(characters[0]);});
                    btnBlueHome2.setOnClickListener(v -> {
                        moveToStartCharacter(characters[1]);});
                    btnBlueHome3.setOnClickListener(v -> {
                        moveToStartCharacter(characters[2]);});
                    btnBlueHome4.setOnClickListener(v -> {
                        moveToStartCharacter(characters[3]);});
                }

                case DARK_BLUE -> {
                    Character[] characters = getCharacters(player);

                    btnLilaHome1.setImageResource(R.drawable.playericon);
                    btnLilaHome2.setImageResource(R.drawable.playericon);
                    btnLilaHome3.setImageResource(R.drawable.playericon);
                    btnLilaHome4.setImageResource(R.drawable.playericon);

                    btnLilaHome1.setOnClickListener(v -> {
                        moveToStartCharacter(characters[0]);});
                    btnLilaHome2.setOnClickListener(v -> {
                        moveToStartCharacter(characters[1]);});
                    btnLilaHome3.setOnClickListener(v -> {
                        moveToStartCharacter(characters[2]);});
                    btnLilaHome4.setOnClickListener(v -> {
                        moveToStartCharacter(characters[3]);});
                }
            }
        }
    }

    private void moveToStartCharacter(Character c) {

        if (gameboardPositions.isEmpty()) {
            throw new NullPointerException("Gameboard is not initalized yet.");
        } else {

            if (c.status().equals(CharacterState.HOME)) {
                Log.i("App", "Character " + c.id() + " is currently in HOME");
            } else if (c.status().equals(CharacterState.FIELD)) {
                Log.i("App", "Character " + c.id() + " is currently on the field");
            } else if (c.status().equals(CharacterState.GOAL)) {
                Log.i("App", "Character " + c.id() + " is currently in GOAL");
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
                    btnYellowGoal1.setImageResource(R.drawable.playericon);
                    btnYellowGoal2.setImageResource(R.drawable.playericon);
                    btnYellowGoal3.setImageResource(R.drawable.playericon);
                    btnYellowGoal4.setImageResource(R.drawable.playericon);
                }

                case PINK -> {
                    btnRosaGoal1.setImageResource(R.drawable.playericon);
                    btnRosaGoal2.setImageResource(R.drawable.playericon);
                    btnRosaGoal3.setImageResource(R.drawable.playericon);
                    btnRosaGoal4.setImageResource(R.drawable.playericon);
                }
                case RED -> {
                    btnRedGoal1.setImageResource(R.drawable.playericon);
                    btnRedGoal2.setImageResource(R.drawable.playericon);
                    btnRedGoal3.setImageResource(R.drawable.playericon);
                    btnRedGoal4.setImageResource(R.drawable.playericon);
                }

                case GREEN -> {
                    btnGreenGoal1.setImageResource(R.drawable.playericon);
                    btnGreenGoal2.setImageResource(R.drawable.playericon);
                    btnGreenGoal3.setImageResource(R.drawable.playericon);
                    btnGreenGoal4.setImageResource(R.drawable.playericon);
                }
                case LIGHT_BLUE -> {
                    btnBlueGoal1.setImageResource(R.drawable.playericon);
                    btnBlueGoal2.setImageResource(R.drawable.playericon);
                    btnBlueGoal3.setImageResource(R.drawable.playericon);
                    btnBlueGoal4.setImageResource(R.drawable.playericon);
                }

                case DARK_BLUE -> {
                    btnLilaGoal1.setImageResource(R.drawable.playericon);
                    btnLilaGoal2.setImageResource(R.drawable.playericon);
                    btnLilaGoal3.setImageResource(R.drawable.playericon);
                    btnLilaGoal4.setImageResource(R.drawable.playericon);
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
    //used for reveal witch!
    private int getPlayerIcon(com.example.frontend.Player player) {
        // Return the drawable resource id based on player details
        switch (player.color()) {
            case YELLOW:
                return R.drawable.yellowhat;
            case PINK:
                return R.drawable.pinkhat;
            case RED:
                return R.drawable.redhat;
            case GREEN:
                return R.drawable.greenhat;
            case LIGHT_BLUE:
                return R.drawable.lightbluehat;
            case DARK_BLUE:
                return R.drawable.bluehat;
            // add other cases as necessary
        }
        return -1; // default or error case
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
            requireActivity().runOnUiThread(() -> {
                diceRolled(diceValue);
            });
        } else if (propertyChangeEvent.getPropertyName().equals(Game.Property.DICE_ROLLED.name())) {
            DicePayload payload = (DicePayload) propertyChangeEvent.getNewValue();
            requireActivity().runOnUiThread(() -> {
                diceRolled(payload);
            });
        } else if (propertyChangeEvent.getPropertyName().equals(Game.Property.YOUR_TURN.name())) {
            yourTurn();
        }
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
            revealWitchFunct();
            dialog.dismiss();
            Log.i("App", "RevealWitch Request will be sent now + move request will be sent then");
        });

        moveToStartBtn.setOnClickListener(v -> {
            //send request to server
            //Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload()));

            dialog.dismiss();
            Log.i("App", "MoveToStart Request will be sent now");
        });

        dialog.show();
    }

    private void revealWitchFunct() {
        Log.i("App", "Reveal Witch Function");
    }

    private void diceRolled(DicePayload payload) {
        if (isAdded()) {
            Toast.makeText(requireContext(), "Player" + payload.player() + " has rolled " + payload.diceValue(), Toast.LENGTH_SHORT).show();
            Log.i("App", payload.player() + ": " + payload.diceValue());
        }
    }
}
