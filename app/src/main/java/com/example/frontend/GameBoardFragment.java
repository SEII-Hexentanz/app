package com.example.frontend;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;


public class GameBoardFragment extends Fragment implements PropertyChangeListener {
    private final long MAX_TIMER_DURATION = 15 * 60 * 1000; //1min=60_000 // 15 minutes
    private Button diceBtn;
    private FragmentContainerView fragmentContainerView;
    private TextView usernameTxt, timerText;
    private CountDownTimer countDownTimer;
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
    private ImageView boardField0;
    private ImageView boardField1;
    private ImageView boardField2;
    private ImageView boardField3;
    private ImageView boardField4;
    private ImageView boardField5;
    private ImageView boardField6;
    private ImageView boardField7;
    private ImageView boardField8;
    private ImageView boardField9;
    private ImageView boardField10;
    private ImageView boardField11;
    private ImageView boardField12;
    private ImageView boardField13;
    private ImageView boardField14;
    private ImageView boardField15;
    private ImageView boardField16;
    private ImageView boardField17;
    private ImageView boardField18;
    private ImageView boardField19;
    private ImageView boardField20;
    private ImageView boardField21;
    private ImageView boardField22;
    private ImageView boardField23;
    private ImageView boardField24;
    private ImageView boardField25;
    private ImageView boardField26;
    private ImageView boardField27;
    private ImageView boardField28;
    private ImageView boardField29;
    private ImageView boardField30;
    private ImageView boardField31;
    private ImageView boardField32;
    private ImageView boardField33;
    private ImageView boardField34;
    private ArrayList<ImageView> list;
    private ScaleGestureDetector scaleGestureDetector;
    private long startTime = 0L;
    private Handler timerHandler = new Handler();
    private long millisecondsTime = 0L;
    private long timeSwapBuff = 0L;
    private Runnable updateTimeRunnable = new Runnable() {
        public void run() {
            millisecondsTime = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (millisecondsTime / 1000);
            int minutes = seconds / 60;
            seconds %= 60;
            timerText.setText(String.format("%02d:%02d", minutes, seconds));
            if (millisecondsTime >= MAX_TIMER_DURATION) {
                showEndGameFragment();
            } else {
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    public GameBoardFragment() {
        //leerer Konstruktor notwendig
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
        //Display Gameboard only in Landscape Mode

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set screen orientation to landscape when GameBoardFragment is resumed
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Reset screen orientation to portrait when GameBoardFragment is paused
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        pauseTimer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);

        findViews(view);
        setGameBoardUsername();
        onRollDiceClick();
        //   initializeGameBoard(view);
        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());
        initalizePlayerHomePositions(Game.INSTANCE.players());
        // getBoardContent(list);

        return view;
    }

    private void setGameBoardUsername() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("username");
            usernameTxt.setText(name);
        }
    }

    private void onRollDiceClick() {
        diceBtn.setOnClickListener(view -> {
            fragmentContainerView.setVisibility(View.VISIBLE);
            showDiceFragment();
        });
    }

    private void findViews(View view) {
        diceBtn = view.findViewById(R.id.btn_rollDice);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerDice);
        usernameTxt = view.findViewById(R.id.txtViewUsername);
        timerText = view.findViewById(R.id.timerTextView);
        gameBoard = view.findViewById(R.id.gridLayoutGameBoard);

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

        boardField0 = view.findViewById(R.id.gameboardpos0);
        boardField1 = view.findViewById(R.id.gameboardpos1);
        boardField2 = view.findViewById(R.id.gameboardpos2);
        boardField3 = view.findViewById(R.id.gameboardpos3);
        boardField4 = view.findViewById(R.id.gameboardpos4);
        boardField5 = view.findViewById(R.id.gameboardpos5);
        boardField6 = view.findViewById(R.id.gameboardpos6);
        boardField7 = view.findViewById(R.id.gameboardpos7);
        boardField8 = view.findViewById(R.id.gameboardpos8);
        boardField9 = view.findViewById(R.id.gameboardpos9);
        boardField10 = view.findViewById(R.id.gameboardpos10);
        boardField11 = view.findViewById(R.id.gameboardpos11);
        boardField12 = view.findViewById(R.id.gameboardpos12);
        boardField13 = view.findViewById(R.id.gameboardpos13);
        boardField14 = view.findViewById(R.id.gameboardpos14);
        boardField15 = view.findViewById(R.id.gameboardpos15);
        boardField16 = view.findViewById(R.id.gameboardpos16);
        boardField17 = view.findViewById(R.id.gameboardpos17);
        boardField18 = view.findViewById(R.id.gameboardpos18);
        boardField19 = view.findViewById(R.id.gameboardpos19);
        boardField20 = view.findViewById(R.id.gameboardpos20);
        boardField21 = view.findViewById(R.id.gameboardpos21);
        boardField22 = view.findViewById(R.id.gameboardpos22);
        boardField23 = view.findViewById(R.id.gameboardpos23);
        boardField24 = view.findViewById(R.id.gameboardpos24);
        boardField25 = view.findViewById(R.id.gameboardpos25);
        boardField26 = view.findViewById(R.id.gameboardpos26);
        boardField27 = view.findViewById(R.id.gameboardpos27);
        boardField28 = view.findViewById(R.id.gameboardpos28);
        boardField29 = view.findViewById(R.id.gameboardpos29);
        boardField30 = view.findViewById(R.id.gameboardpos30);
        boardField31 = view.findViewById(R.id.gameboardpos31);
        boardField32 = view.findViewById(R.id.gameboardpos32);
        boardField33 = view.findViewById(R.id.gameboardpos33);
        boardField34 = view.findViewById(R.id.gameboardpos34);
    }

    private void initalizePlayerHomePositions(SortedSet<at.aau.models.Player> players) {
        for (at.aau.models.Player player : players) {

            switch (player.color()) {
                case YELLOW -> {
                    btnYellowHome1.setImageResource(R.drawable.playericon);
                    btnYellowHome2.setImageResource(R.drawable.playericon);
                    btnYellowHome3.setImageResource(R.drawable.playericon);
                    btnYellowHome4.setImageResource(R.drawable.playericon);
                }

                case PINK -> {
                    btnRosaHome1.setImageResource(R.drawable.playericon);
                    btnRosaHome2.setImageResource(R.drawable.playericon);
                    btnRosaHome3.setImageResource(R.drawable.playericon);
                    btnRosaHome4.setImageResource(R.drawable.playericon);
                }
                case RED -> {
                    btnRosaHome1.setImageResource(R.drawable.playericon);
                    btnRosaHome2.setImageResource(R.drawable.playericon);
                    btnRosaHome3.setImageResource(R.drawable.playericon);
                    btnRosaHome4.setImageResource(R.drawable.playericon);
                }

                case GREEN -> {
                    btnGreenHome1.setImageResource(R.drawable.playericon);
                    btnGreenHome2.setImageResource(R.drawable.playericon);
                    btnGreenHome3.setImageResource(R.drawable.playericon);
                    btnGreenHome4.setImageResource(R.drawable.playericon);
                }
                case LIGHT_BLUE -> {
                    btnBlueHome1.setImageResource(R.drawable.playericon);
                    btnBlueHome2.setImageResource(R.drawable.playericon);
                    btnBlueHome3.setImageResource(R.drawable.playericon);
                    btnBlueHome4.setImageResource(R.drawable.playericon);
                }

                case DARK_BLUE -> {
                    btnLilaHome1.setImageResource(R.drawable.playericon);
                    btnLilaHome2.setImageResource(R.drawable.playericon);
                    btnLilaHome3.setImageResource(R.drawable.playericon);
                    btnLilaHome4.setImageResource(R.drawable.playericon);
                }
            }
        }
    }

    private ArrayList<ImageView> initializeGameBoard(View view) {
        ArrayList<ImageView> listView = new ArrayList<ImageView>();
        int numFields = 35; // Assuming you have 35 board fields

        for (int i = 0; i < numFields; i++) {
            ImageView boardField = view.findViewById(getResources().getIdentifier("gameboardpos" + i, "id", requireContext().getPackageName()));
            boardField.setImageResource(R.drawable.playericon);
            listView.add(boardField);
        }

        return listView;

    }

    private void getBoardContent(ArrayList<ImageView> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
            Log.i("GameboardList", String.valueOf(list.size()));
        }
    }

    private void startTimer() {
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(updateTimeRunnable, 0);
    }

    private void pauseTimer() {
        timeSwapBuff += millisecondsTime;
        timerHandler.removeCallbacks(updateTimeRunnable);
    }

    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.dice, diceFragment);
        fragmentTransaction.commit();
    }

    private List<ImageView> findImageViewByID(int count) {
        List<ImageView> imageViews = new ArrayList<>();
        Resources res = getResources();
        String packageName = requireContext().getPackageName();
        for (int i = 1; i <= count; i++) {
            int id = res.getIdentifier("gameboard" + i, "id", packageName);
            ImageView imageView = requireView().findViewById(id);
            imageViews.add(imageView);
        }
        return imageViews;
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
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Log.i("App", "PropertyChangeEvent received: " + propertyChangeEvent.getPropertyName());
        if (propertyChangeEvent.getPropertyName().equals(Game.Property.WINNER.name())) {
//            TODO: End Screen #17
        }
    }



/*
//necessary in Sprint 2
//EPIC method that will be used in END Game
    public void showWinner() {
        String winnerName="Max Mustermann";
        EngGameFragment endFragment = EngGameFragment.newInstance(winnerName);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, endFragment);
        fragmentTransaction.commit();
    }
*/

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

}
