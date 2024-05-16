package com.example.frontend;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.SortedSet;
import java.util.TreeSet;

import at.aau.values.GameState;

public enum Game {
    INSTANCE;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SortedSet<com.example.frontend.Player> players = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;

    private boolean myTurn;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public SortedSet<com.example.frontend.Player> players() {
        return players;
    }

    public void setPlayers(SortedSet<com.example.frontend.Player> players) {
        SortedSet<Player> oldPlayers = this.players;
        this.players = players;
        support.firePropertyChange(Property.PLAYERS.name(), oldPlayers, players);
    }

    public GameState gameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        GameState oldGameState = this.gameState;
        this.gameState = gameState;
        support.firePropertyChange(Property.GAME_STATE.name(), oldGameState, gameState);
    }

    public void setMyTurn(){
        boolean oldMyTurn = myTurn;
        myTurn = true;
        support.firePropertyChange(Property.MY_TURN.name(), oldMyTurn, myTurn);
    }

    public boolean isMyTurn(){
        return myTurn;
    }

    public void endMyTurn() {
        myTurn = false;
    }

    enum Property {
        PLAYERS, GAME_STATE, MY_TURN
    }
}
