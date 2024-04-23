
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

    }

    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.gridLayoutGameBoard, diceFragment);
        fragmentTransaction.commit();
    }

    private void BoardFragment() {

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
