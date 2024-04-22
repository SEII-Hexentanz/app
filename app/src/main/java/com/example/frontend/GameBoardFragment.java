
package com.example.frontend;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.widget.ImageView;

public class GameBoardFragment extends Fragment {
    private Button diceBtn;
    private FragmentContainerView fragmentContainerView;
    private TextView usernameTxt;
    private ImageView gameBoard;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);

        findViews(view);
        setGameBoardUsername();
        onRollDiceClick();
        scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new PinchZoomListenerClass());

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
        diceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentContainerView.setVisibility(View.VISIBLE);
                showDiceFragment();
            }
        });
    }

    private void findViews(View view) {
        diceBtn = view.findViewById(R.id.btn_rollDice);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerDice);
        usernameTxt = view.findViewById(R.id.txtViewUsername);
        gameBoard = view.findViewById(R.id.gridLayoutGameBoard);
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

    public class PinchZoomListenerClass extends SimpleOnScaleGestureListener {
        public PinchZoomListenerClass() {
            super();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor = scaleFactor * detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            gameBoard.setScaleX(scaleFactor);
            gameBoard.setScaleY(scaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }
}
