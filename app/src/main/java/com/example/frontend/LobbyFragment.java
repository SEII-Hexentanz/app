package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.SortedSet;

import at.aau.models.Request;
import at.aau.payloads.EmptyPayload;
import at.aau.values.CommandType;
import at.aau.values.GameState;


public class LobbyFragment extends Fragment implements PropertyChangeListener {

    ImageButton retCreateLobby;
    ArrayList<Player> userList = new ArrayList<>();
    PlayerAdapter adapter;
    private Button startGame;
    private Button rulesBtn;
    private RecyclerView recyclerView;
    private TextView playerCount;

    public LobbyFragment() {
        // Required empty public constructor
        Game.INSTANCE.addPropertyChangeListener(this);
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
        onClickStart();
        updatePlayerData(Game.INSTANCE.players());


        adapter = new PlayerAdapter(userList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int allPlayer = adapter.getItemCount();

        playerCount.setText(allPlayer + "");

        onClickStart();
        onReturnBtnClick();

        return view;
    }

    private void updatePlayerData(SortedSet<Player> players) {
        userList.clear();
        userList.addAll(players);
        if (adapter != null) requireActivity().runOnUiThread(adapter::notifyDataSetChanged);
    }

    private void onReturnBtnClick() {
        retCreateLobby.setOnClickListener(view -> {
            // Inside your Fragment class where you want to close and return
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }

    private void onClickStart() {
        startGame.setOnClickListener(view -> {
            Client.send(new Request(CommandType.START, new EmptyPayload()));
        });

        rulesBtn.setOnClickListener(v -> showRulesFragment());
    }

    private void findViews(View view) {
        startGame = view.findViewById(R.id.btn_StartGame);
        retCreateLobby = view.findViewById(R.id.imgBtnRetCL);
        recyclerView = view.findViewById(R.id.lobbyPlayerRecyclerView);
        playerCount = view.findViewById(R.id.txtlistPlayerCount);
        rulesBtn = view.findViewById(R.id.button);
    }

    private void showGameBoardFragment() {

        GameBoardFragment gameBoardFragment = new GameBoardFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, gameBoardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showRulesFragment() {

        RulesFragment rulesfragment = new RulesFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, rulesfragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Log.i("App", "PropertyChangeEvent received: " + propertyChangeEvent.getPropertyName());
        if (propertyChangeEvent.getPropertyName().equals(Game.Property.PLAYERS.name())) {
            updatePlayerData((SortedSet<Player>) propertyChangeEvent.getNewValue());
        } else if (propertyChangeEvent.getPropertyName().equals(Game.Property.GAME_STATE.name()) &&
                propertyChangeEvent.getNewValue() == GameState.RUNNING) {
            requireActivity().runOnUiThread(this::showGameBoardFragment);
        }
    }
}
