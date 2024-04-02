package com.example.frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLobbyFragment extends Fragment {

    private Button createGame;
    private Button joinGame;
    private View createLobbyLayout;
    private View lobbyFragment;

    private ImageButton closeBtn;
    public CreateLobbyFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();



        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Change Lobby Fragment", "Lobby Fragment change event started");
                if (lobbyFragment.getVisibility() == View.INVISIBLE) {
                    createLobbyLayout.setVisibility(View.GONE);
                } else {
                    createLobbyLayout.setVisibility(View.GONE);
                    lobbyFragment.setVisibility(View.VISIBLE);
                    changeFragment();
                }

            }
        });

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Change Join Fragment", "Join Fragment change event started");
                if (lobbyFragment.getVisibility() == View.INVISIBLE) {
                    createLobbyLayout.setVisibility(View.GONE);
                } else {
                    createLobbyLayout.setVisibility(View.GONE);
                    lobbyFragment.setVisibility(View.VISIBLE);
                    changeFragment();
                }

            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getParentFragmentManager().beginTransaction().remove(CreateLobbyFragment.this).commit();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_lobby, container, false);
    }

    public void findViews() {
        createGame = createGame.findViewById(R.id.btn_createGame);
        joinGame = joinGame.findViewById(R.id.btn_joinGame);
        lobbyFragment = lobbyFragment.findViewById(R.id.fragmentLobby);
        createLobbyLayout = createLobbyLayout.findViewById(R.id.createLobbyId);
        closeBtn = closeBtn.findViewById(R.id.closeFragmentCreateLobby);
    }

    public void changeFragment() {

        CreateLobbyFragment lobbyFragment = new CreateLobbyFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentLobby, lobbyFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }
}