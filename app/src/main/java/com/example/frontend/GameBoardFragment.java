
package com.example.frontend;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.SortedSet;

public class GameBoardFragment extends Fragment {
    private Button diceBtn;
    private FragmentContainerView fragmentContainerView;
    private TextView usernameTxt;
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



    private ScaleGestureDetector scaleGestureDetector;

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
    }

    @Override
    public void onPause() {
        super.onPause();
        // Reset screen orientation to portrait when GameBoardFragment is paused
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);

        findViews(view);
        setGameBoardUsername();
        onRollDiceClick();
        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());

//ImageViewTest for BTNGreen
        btnGreenHome1.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "I am button 1", Toast.LENGTH_SHORT).show();

        });
        btnGreenHome2.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "I am button 2", Toast.LENGTH_SHORT).show();

        });

        btnGreenHome3.setOnClickListener(v->{
            Toast.makeText(requireContext(), "I am button 3", Toast.LENGTH_SHORT).show();

        });


        btnGreenHome4.setOnClickListener(v-> {
            Toast.makeText(requireContext(), "I am button 4", Toast.LENGTH_SHORT).show();
        });
    /*    gameBoard.setOnTouchListener((v, event) -> {

            if(event.getAction() == MotionEvent.ACTION_MOVE){
                //do something
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
            return true;
        });
*/


        return view;
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


    }

    private void initalizePlayerHomePositions(SortedSet<at.aau.models.Player> players) {
        for (at.aau.models.Player player : players) {
            Player newPlayer = new Player();

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


    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.gridLayoutGameBoard, diceFragment);
        fragmentTransaction.commit();
    }


/*
//necessary in next Sprint
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


    }
