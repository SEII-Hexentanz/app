package com.example.frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLobbyFragment extends Fragment {
    private Button createGame;
    private Button joinGame;
    private TextView nameBox;

    public CreateLobbyFragment() {
    }

    public static CreateLobbyFragment newInstance(String name) {
        CreateLobbyFragment fragment = new CreateLobbyFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
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
            setUserName();
            onCreateGameCLick();
            onJoinClick();
            return view;
        }

    private void setUserName() {
        Bundle bundle = getArguments();
        if(bundle != null){
            String name = bundle.getString("name");
            nameBox.setText(name + " !");
        }
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
            nameBox = view.findViewById(R.id.editTextUsername);
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
            String name = nameBox.getText().toString();

            GameBoardFragment gameBoardFragment = GameBoardFragment.newInstance(name);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(android.R.id.content,gameBoardFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
