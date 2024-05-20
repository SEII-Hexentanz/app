package com.example.frontend;

import android.util.Log;

import com.example.frontend.responseHandler.ResponseHandler;

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
    private DiceFragment diceFragment;
    HashMap<at.aau.values.Color, Integer> mapStartingPoint= new HashMap<>();;

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
    public void mapStartPositions() {
        mapStartingPoint.put(at.aau.values.Color.YELLOW, 26);
        mapStartingPoint.put(at.aau.values.Color.PINK, 32);
        mapStartingPoint.put(at.aau.values.Color.RED, 6);
        mapStartingPoint.put(at.aau.values.Color.GREEN, 20);
        mapStartingPoint.put(at.aau.values.Color.LIGHT_BLUE, 9);
        mapStartingPoint.put(at.aau.values.Color.DARK_BLUE, 3);
    }

    public void initializePlayerPositions() {
        for (com.example.frontend.Player player : frontPlayer) {
            Integer startPosition = mapStartingPoint.get(player.color());
            if (startPosition != null) {
                playerPositions.put(player, startPosition);
            } else {
                playerPositions.put(player, 0); // Default to position 0 if color not found
            }
        }
    }

    public void setPlayerPosition(com.example.frontend.Player player, int position) {
        Integer oldPosition = playerPositions.get(player);
        playerPositions.put(player, position);
        support.firePropertyChange("playerPosition", Optional.ofNullable(oldPosition), position);
    }

    public int getPlayerPosition(com.example.frontend.Player player) {
        return playerPositions.getOrDefault(player, 0); // Return -1 if no position set
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % frontPlayer.size();
        support.firePropertyChange("currentPlayer", null, getCurrentPlayer());
    }

    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    private void updateCharacterState(com.example.frontend.Player player, int characterIndex, CharacterState newState) {
        if (player == null) {
            Log.e(TAG, "Player object is null in updateCharacterState");
            return;
        }

        if (newState == null) {
            Log.e(TAG, "CharacterState is null in updateCharacterState");
            return;
        }

        com.example.frontend.Player updatedPlayer = player.updateCharacterState(characterIndex, newState);
        if (updatedPlayer == null) {
            Log.e(TAG, "Updated player is null after calling updateCharacterState");
            return;
        }
        frontPlayer.remove(player);
        frontPlayer.add(updatedPlayer);
        broadcastMove(updatedPlayer, playerPositions.get(player), playerPositions.get(updatedPlayer));
    }

    private void broadcastMove(com.example.frontend.Player player, int oldPosition, int newPosition) {
        if (player == null) {
            Log.e(TAG, "Player object is null in broadcastMove");
            return;
        }
        Response response = new Response(ResponseType.UPDATE_STATE, new PlayerMovePayload(oldPosition, newPosition, player.getUsername()));
        ResponseHandler.execute(response.responseType(),response.payload(),Game.INSTANCE);

    }

    public void movePlayer(int diceResult) {
        com.example.frontend.Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Log.e(TAG, "No current player found. Cannot move.");
            return;
        }
        boolean hasAnotherTurn = false;
        int newPosition=0;
        int currentPosition = getPlayerPosition(currentPlayer);
        Log.i(TAG,"CurrentPosition: " + currentPosition);
        // Check if the player is at "Home" and the dice result allows moving out
        if (currentPosition == getPlayerPosition(currentPlayer)) {
            if (diceResult == 6) { // Assuming 6 is required to start
                currentPosition = mapStartingPoint.get(currentPlayer.color());
                setPlayerPosition(currentPlayer, currentPosition);
                Log.i(TAG, "Player " + currentPlayer.getUsername() + " moves from Home to position " + currentPosition);
                eventListener.onPlayerPositionChanged(currentPlayer, 0, currentPosition);
                broadcastMove(currentPlayer, 0, currentPosition);
                hasAnotherTurn = true; // Player gets another turn
                //diceFragment.setDiceToDefault();
            } else {
                Log.i(TAG, "Player " + currentPlayer.getUsername() + " needs a 6 to leave Home. His color is" + currentPlayer.color());
                return; // Player cannot move out of Home without rolling a 6
            }
        } else {
            newPosition = currentPosition + diceResult;
            setPlayerPosition(currentPlayer, newPosition);
            Log.i(TAG, "Player " + currentPlayer.getUsername() + " moved to position " + newPosition);
            eventListener.onPlayerPositionChanged(currentPlayer, currentPosition, newPosition);
            Client.send(new Request(CommandType.PLAYER_MOVE,new PlayerMovePayload(currentPosition,newPosition, currentPlayer.getUsername())));
            broadcastMove(currentPlayer, currentPosition, newPosition);
            if (diceResult == 6) {
                hasAnotherTurn = true; // Player gets another turn
            }
        }

        if (!hasAnotherTurn) {
            nextPlayer(); // Move to the next player only if the current player does not get another turn
        }

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