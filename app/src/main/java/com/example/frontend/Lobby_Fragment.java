
package com.example.frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class Lobby_Fragment extends Fragment {

    private Button startGame;
    private ImageButton retCreateLobby;

    public Lobby_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lobby_, container, false);
        findViews(view);
        onClckStart();
        onReturnBtnClick();

        return view;
    }
    private void onReturnBtnClick(){
        retCreateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  FragmentManager manager = requireActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(Lobby_Fragment.this).commit();
                */

            }
        });
    }
    private void onClckStart() {
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGameBoardFragment();
            }
        });
    }
    private void findViews(View view){
        startGame = view.findViewById(R.id.btn_StartGame);
        retCreateLobby = view.findViewById(R.id.imgBtnRetCL);
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
