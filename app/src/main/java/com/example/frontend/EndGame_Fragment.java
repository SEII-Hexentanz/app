package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EndGame_Fragment extends Fragment {

    private static final String ARG_WINNER_NAME = "winnerName";

    private String sieger;

    public EndGame_Fragment() {
        // Required empty public constructor
    }

    public static EndGame_Fragment newInstance(String winnerName) {
        EndGame_Fragment fragment = new EndGame_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_WINNER_NAME, winnerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sieger = getArguments().getString(ARG_WINNER_NAME);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ende, container, false);
        TextView winnerNameTextView = view.findViewById(R.id.winnerName);
        Button restartGameButton = view.findViewById(R.id.restartGame);

        winnerNameTextView.setText(getString(R.string.winnerText, sieger));

        restartGameButton.setOnClickListener(v -> {
            // TODO implemtieren zum rückkehren zum Start screen

        });

        return view;
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
