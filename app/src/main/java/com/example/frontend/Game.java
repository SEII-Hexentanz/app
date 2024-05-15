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
import at.aau.values.Color;
import at.aau.values.CommandType;
import at.aau.values.GameState;
import at.aau.values.ResponseType;

public enum Game {
    INSTANCE;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SortedSet<Player> players = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private Map<Player, Integer> playerPositions = new HashMap<>();
    private int currentPlayerIndex = 0;
    public static final String TAG = "GAME_TAG";
    private Map<Player, Boolean> canMove = new HashMap<>();
    private GameEventListener eventListener;

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

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            Log.e(TAG, "No players available.");
            return null;
        }
        return players.stream().skip(currentPlayerIndex).findFirst().orElse(null);
    }

    public void setPlayerPosition(Player player, int position) {
        Integer oldPosition = playerPositions.get(player);
        playerPositions.put(player, position);
        support.firePropertyChange("playerPosition", Optional.ofNullable(oldPosition), position);
    }

    public int getPlayerPosition(Player player) {
        return playerPositions.getOrDefault(player, -1); // Return -1 if no position set
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        support.firePropertyChange("currentPlayer", null, getCurrentPlayer());
    }

    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    public void movePlayer(int diceResult) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Log.e(TAG, "No current player found. Cannot move.");
            return;
        }

        boolean hasRolledSix = diceResult == 6;
        boolean canStartMoving = canMove.getOrDefault(currentPlayer, false);

       /* if (!canStartMoving && !hasRolledSix) {
            canMove.put(currentPlayer, true); // can move nonetheless
            Log.i(TAG, currentPlayer.name() + " must roll a 6 to start moving!");
            return; // Spieler muss eine 6 würfeln, um zu beginnen
        }

        if (hasRolledSix && !canStartMoving) {
            canMove.put(currentPlayer, true);
            Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(newPosition)));
            Log.i(TAG, currentPlayer.name() + " rolled a 6 and can now move!");
            return; // Erlaubt dem Spieler, ab dem nächsten Zug zu starten
        }*/
        int currentPosition = getPlayerPosition(currentPlayer);
        int newPosition = currentPosition + diceResult;
        setPlayerPosition(currentPlayer, newPosition);

        Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(currentPosition,newPosition,currentPlayer.name())));
        Log.i(TAG, "Player " + currentPlayer.name() + " moved to position " + newPosition + " from " + currentPosition);
        support.firePropertyChange("playerPosition", currentPosition, newPosition);
        eventListener.onPlayerPositionChanged(currentPlayer, currentPosition, newPosition);
        broadcastMove(currentPlayer, currentPosition, newPosition);
    }

    private void broadcastMove(Player player, int oldPosition, int newPosition) {
        Response response = new Response(ResponseType.UPDATE_STATE, new PlayerMovePayload(oldPosition, newPosition, player.name()));
        for (Player p : players) {
            p.send(response);
        }
    }

    public void addPlayers(List<at.aau.models.Player> playersList) {
        for (at.aau.models.Player player : playersList) {
            if (!players.contains(player)) {
                Color color = Color.values()[players.size() % Color.values().length];
                Player coloredPlayer = new Player(player.name(), player.age(), color, player.characters());
                players.add(coloredPlayer);
                playerPositions.put(coloredPlayer, 0);
                canMove.put(coloredPlayer, false);
                Log.i(TAG, "Added player: " + coloredPlayer.name());
            }
        }
    }

    public void updatePlayer(Player oldPlayer, Player newPlayer) {
        if (players.contains(oldPlayer)) {
            Log.i(TAG, newPlayer.name() + " updatePlayerMethod");
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
