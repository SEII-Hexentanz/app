package com.example.frontend;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import at.aau.models.Player;
import at.aau.values.Color;
import at.aau.values.GameState;

public enum Game {
    INSTANCE;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SortedSet<Player> players = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private Map<Player, Integer> playerPositions = new HashMap<>();
    private Player currentPlayer;
    private int currentPlayerIndex = 0;
    public static final String TAG = "GAME_TAG";
    private Map<Player, Boolean> canMove = new HashMap<>();

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
        if (!canMove.getOrDefault(currentPlayer, false) && diceResult != 6) {
            canMove.put(currentPlayer, true); //to remove when movement successfull
            Log.i(TAG, "Player must roll a 6 to start moving!");
            return; // Player must roll a 6 to start
        }

        if (diceResult == 6 && !canMove.get(currentPlayer)) {
            canMove.put(currentPlayer, true);
            Log.i(TAG, "Player rolled a 6 and can now move!");
            return; // Allow the player to start moving from the next turn
        }

        int currentPosition = getPlayerPosition(currentPlayer);
        int newPosition = currentPosition + diceResult;
        playerPositions.put(currentPlayer, newPosition);
        Log.i(TAG, "Player " + getCurrentPlayer() + " moved to position " + newPosition);
        support.firePropertyChange("playerPosition", currentPosition, newPosition);
        eventListener.onPlayerPositionChanged(currentPlayer, currentPosition, newPosition);
    }



    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            Color color = Color.values()[players.size() % Color.values().length]; // Cycle through colors
            Player coloredPlayer = new Player(player.name(), player.age(), color, player.characters());
            players.add(coloredPlayer);
            playerPositions.put(coloredPlayer, 0); // Start position for every player
            canMove.put(coloredPlayer, false); // Initially, players can't move
        }
    }

    public void updatePlayer(Player oldPlayer, Player newPlayer) {
        if (players.contains(oldPlayer)) {
            players.remove(oldPlayer);
            players.add(newPlayer);
            playerPositions.put(newPlayer, playerPositions.get(oldPlayer)); // Alte Position beibehalten
            canMove.put(newPlayer, canMove.getOrDefault(oldPlayer, false)); // Bewegungsstatus beibehalten
            support.firePropertyChange("players", oldPlayer, newPlayer); // Benachrichtigen der Listener
        }
    }



    enum Property {
        PLAYERS, GAME_STATE
    }
}
