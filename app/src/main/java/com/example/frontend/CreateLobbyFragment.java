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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLobbyFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button createGame;
    private Button joinGame;
    private View createLobbyLayout;
    private View lobbyFragment;

    public CreateLobbyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateLobbyFragment.
     */

    public static CreateLobbyFragment newInstance(String param1, String param2) {
        CreateLobbyFragment fragment = new CreateLobbyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

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