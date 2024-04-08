package com.example.frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLobbyFragment extends Fragment {
private Button createGame;
private Button joinGame;

    public CreateLobbyFragment() {
        //leerer Konstruktor notwendig
    }

    public static CreateLobbyFragment newInstance() {
        CreateLobbyFragment fragment = new CreateLobbyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_lobby, container, false);
       findViews(view);
        onCreateGameCLick();
        onJoinClick();
        return view;
    }

    private void onCreateGameCLick() {
        createGame.setOnClickListener(v -> showFragmentLobby());
    }

    private void onJoinClick() {
        joinGame.setOnClickListener(v -> showGameBoardFragment());
    }
    private void findViews(View view){
        createGame = view.findViewById(R.id.btn_createGame);
         joinGame = view.findViewById(R.id.btn_joinGame);
    }

    private void showFragmentLobby(){
        Lobby_Fragment lobbyFragment = new Lobby_Fragment();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView2,lobbyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void showGameBoardFragment(){
        GameBoardFragment gameBoardFragment = new GameBoardFragment();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(android.R.id.content,gameBoardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}