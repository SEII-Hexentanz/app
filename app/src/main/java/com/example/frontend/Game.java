package com.example.frontend;

import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import at.aau.models.Player;
import at.aau.models.Request;
import at.aau.models.Response;
import at.aau.payloads.EmptyPayload;
import at.aau.payloads.PlayerMovePayload;
import at.aau.payloads.RegisterPayload;
import at.aau.payloads.UpdateStatePayload;
import at.aau.values.CharacterState;
import at.aau.values.Color;
import at.aau.values.CommandType;
import at.aau.values.GameState;
import at.aau.values.ResponseType;

public enum Game {
    INSTANCE;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SortedSet<at.aau.models.Player> players = new TreeSet<>();
    private SortedSet<com.example.frontend.Player> frontPlayer = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private Map<com.example.frontend.Player, Integer> playerPositions = new HashMap<>();
    private int currentPlayerIndex = 0;
    public static final String TAG = "GAME_TAG";
    private Map<com.example.frontend.Player, Boolean> canMove = new HashMap<>();
    private GameEventListener eventListener;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public SortedSet<com.example.frontend.Player> FrontPlayer() {
        return frontPlayer;
    }

    public SortedSet<at.aau.models.Player> players(){
        return players;
    }

    public void setPlayers(SortedSet<com.example.frontend.Player> players) {
        SortedSet<com.example.frontend.Player> oldPlayers = this.frontPlayer;
        this.frontPlayer = players;
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

    public com.example.frontend.Player getCurrentPlayer() {
        if (frontPlayer.isEmpty()) {
            Log.e(TAG, "No players available.");
            return null;
        }
        return frontPlayer.stream().skip(currentPlayerIndex).findFirst().orElse(null);
    }

    public void setPlayerPosition(com.example.frontend.Player player, int position) {
        Integer oldPosition = playerPositions.get(player);
        playerPositions.put(player, position);
        support.firePropertyChange("playerPosition", Optional.ofNullable(oldPosition), position);
    }

    public int getPlayerPosition(com.example.frontend.Player player) {
        return playerPositions.getOrDefault(player, -1); // Return -1 if no position set
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % frontPlayer.size();
        support.firePropertyChange("currentPlayer", null, getCurrentPlayer());
    }

    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    public void movePlayer(int diceResult) {
        com.example.frontend.Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Log.e(TAG, "No current player found. Cannot move.");
            return;
        }

        boolean hasRolledSix = diceResult == 6;
        boolean canStartMoving = canMove.getOrDefault(currentPlayer, false);

        if (!canStartMoving && !hasRolledSix) {
            updateCharacterState(currentPlayer, currentPlayerIndex, CharacterState.HOME);
            Log.i(TAG, currentPlayer.getUsername() + " must roll a 6 to start moving!");
            return; // Spieler muss eine 6 würfeln, um zu beginnen
        }
        int currentPosition = getPlayerPosition(currentPlayer);
        int newPosition = currentPosition + diceResult;

        if (hasRolledSix && !canStartMoving) {
            canMove.put(currentPlayer, true);
            updateCharacterState(currentPlayer, currentPlayerIndex, CharacterState.FIELD);
            Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(currentPosition,newPosition,currentPlayer.getUsername())));
            Log.i(TAG, currentPlayer.getUsername() + " rolled a 6 and can now move!");
            nextPlayer();
            return; // Erlaubt dem Spieler, ab dem nächsten Zug zu starten
        }

        setPlayerPosition(currentPlayer, newPosition);
        Log.i(TAG, "Player " + currentPlayer.getUsername() + " moved to position " + newPosition + " from " + currentPosition);
        support.firePropertyChange("playerPosition", currentPosition, newPosition);
        eventListener.onPlayerPositionChanged(currentPlayer, currentPosition, newPosition);
        broadcastMove(currentPlayer, currentPosition, newPosition);
    }

    private void broadcastMove(com.example.frontend.Player player, int oldPosition, int newPosition) {
        Response response = new Response(ResponseType.UPDATE_STATE, new PlayerMovePayload(oldPosition, newPosition, player.getUsername()));
        for (Player p : players) {
            p.send(response);
        }
    }

    private void updateCharacterState(com.example.frontend.Player player, int characterIndex, CharacterState newState) {
        com.example.frontend.Player updatedPlayer = player.updateCharacterState(characterIndex, newState);
        frontPlayer.remove(player);
        frontPlayer.add(updatedPlayer);
        broadcastMove(updatedPlayer, playerPositions.get(player), playerPositions.get(updatedPlayer));
    }

    public void addPlayers(List<com.example.frontend.Player> playersList) {
        for (com.example.frontend.Player player : playersList) {
            if (!frontPlayer.contains(player)) {
                Color color = Color.values()[frontPlayer.size() % Color.values().length];
                com.example.frontend.Player coloredPlayer = new com.example.frontend.Player(player.getUsername(), player.getAge(), player.getCharacters(),player.color());
                frontPlayer.add(coloredPlayer);
                playerPositions.put(coloredPlayer, 0);
                canMove.put(coloredPlayer, false);
                Log.i(TAG, "Added player: " + coloredPlayer.getUsername());
            }
        }
    }

    public void updatePlayer(com.example.frontend.Player oldPlayer, com.example.frontend.Player newPlayer) {
        if (frontPlayer.contains(oldPlayer)) {
            Log.i(TAG, newPlayer.getUsername() + " updatePlayerMethod");
            frontPlayer.remove(oldPlayer);
            frontPlayer.add(newPlayer);
            playerPositions.put(newPlayer, playerPositions.get(oldPlayer)); // Alte Position beibehalten
            canMove.put(newPlayer, canMove.getOrDefault(oldPlayer, false)); // Bewegungsstatus beibehalten
            support.firePropertyChange("players", oldPlayer, newPlayer); // Benachrichtigen der Listener
        }
    }

    enum Property {
        PLAYERS, GAME_STATE
    }
}