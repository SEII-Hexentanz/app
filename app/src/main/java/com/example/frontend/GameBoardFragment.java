
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
import android.widget.TextView;


public class GameBoardFragment extends Fragment {
    private Button diceBtn;
    private FragmentContainerView fragmentContainerView;
    private TextView usernameTxt;

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


        return view;
    }

    private void setGameBoardUsername() {
        Bundle bundle = getArguments();
        if(bundle != null){
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
    }

    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainerDice, diceFragment);
        fragmentTransaction.commit();
    }
}
