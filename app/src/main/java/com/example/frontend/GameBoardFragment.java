package com.example.frontend;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GameBoardFragment extends Fragment {
    private Button diceBtn;
    private FragmentContainerView fragmentContainerView;

    public GameBoardFragment() {
        //leerer Konstruktor notwendig
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

       onRollDiceClick();

        return view;
    }

    private void onRollDiceClick() {
        diceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiceFragment();
                fragmentContainerView.setVisibility(View.VISIBLE);


            }
        });
    }

    private void findViews(View view) {
        diceBtn = view.findViewById(R.id.btn_rollDice);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerDice);

    }

    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainerDice, diceFragment);
        fragmentTransaction.commit();
    }
}