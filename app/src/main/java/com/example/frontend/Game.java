package com.example.frontend;

import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import at.aau.models.Player;
import at.aau.values.GameState;

public enum Game {
    INSTANCE;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SortedSet<Player> players = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private Map<Player, Integer> playerPositions = new HashMap<>();
    private Player currentPlayer;
    private int currentPlayerIndex = 0;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public SortedSet<Player> players() {
        return players;
    }

    public void setPlayers(SortedSet<Player> players) {
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

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPlayerPosition(Player player, int position) {
        Integer oldPosition = playerPositions.get(player);
        playerPositions.put(player, position);
        support.firePropertyChange("playerPosition", Optional.ofNullable(oldPosition), position);
    }

    public int getPlayerPosition(Player player) {
        return playerPositions.getOrDefault(player, -1); // Return -1 if no position set
    }

    public int getCurrentPosition() {
        return getPlayerPosition(getCurrentPlayer());
    }
    public void setCurrentPosition(int newPosition) {
        if (getCurrentPlayer() != null) {
            setPlayerPosition(getCurrentPlayer(), newPosition);
        }
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        support.firePropertyChange("currentPlayer", null, getCurrentPlayer());
    }

    private GameEventListener eventListener;
    public void setGameEventListener (GameEventListener listener){
        this.eventListener = listener;
    }
    public void movePlayer(int diceResult) {
        Player currentPlayer = getCurrentPlayer();
        int currentPosition = getPlayerPosition(currentPlayer);
        int newPosition = currentPosition + diceResult;
        playerPositions.put(currentPlayer, newPosition);
        // Notify the system about the move
        support.firePropertyChange("playerPosition", currentPosition, newPosition);
        //nextPlayer(); // Move to the next player
        Log.i("GAME", "Player: " +getCurrentPlayer() +" has moved " +newPosition );
        eventListener.onPlayerPositionChanged(currentPlayer, currentPosition, newPosition);
    }

    public void addPlayer(Player player) {
        players.add(player);
        playerPositions.put(player, 0); // Start position for every player
    }
    

    enum Property {
        PLAYERS, GAME_STATE
    }
}
