package com.example.frontend;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class EndGameFragment extends Fragment {

    public EndGameFragment() {
        // Required empty public constructor
    }

    public static EndGameFragment newInstance() {
        return new EndGameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ende, container, false);
        TextView winnerNameTextView = view.findViewById(R.id.winnerName);
        Button restartGameButton = view.findViewById(R.id.restartGame);

        winnerNameTextView.setText(getString(R.string.winnerText, Game.INSTANCE.winner().getUsername()));

        restartGameButton.setOnClickListener(v -> {
            showLobbyFragment();


        });

        return view;
    }

    public void showLobbyFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(EndGameFragment.this);
        LobbyFragment lobbyFragment = new LobbyFragment();
        fragmentTransaction.replace(R.id.main, lobbyFragment);
        fragmentTransaction.commit();
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

}
