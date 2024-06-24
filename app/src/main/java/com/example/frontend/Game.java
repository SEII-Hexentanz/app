package com.example.frontend;

import android.content.res.Resources;
import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import at.aau.models.Character;
import at.aau.models.Request;
import at.aau.payloads.CheatPayload;
import at.aau.payloads.DicePayload;
import at.aau.payloads.PlayerMovePayload;
import at.aau.values.CharacterState;
import at.aau.values.Color;
import at.aau.values.CommandType;
import at.aau.values.GameState;
import at.aau.values.MoveType;

public enum Game {
    INSTANCE;

    public static final String TAG = "GAME_TAG";
    final HashMap<at.aau.values.Color, Integer> mapStartingPoint = new HashMap<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private GameEventListener eventListener;
    private SortedSet<Player> frontPlayer = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private int currentPlayerIndex = 0;
    private String playerName;
    private Boolean myTurn = false;
    private int setDiceVal;
    private final Map<com.example.frontend.Player, Integer> playerPositions = new HashMap<>();
    private final Map<com.example.frontend.Player, Boolean> canMove = new HashMap<>();

    private Player currentPlayer = null;
    private Player winner;
    private boolean characterOnBoard = false;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public SortedSet<Player> FrontPlayer() {
        return frontPlayer;
    }

    public SortedSet<Player> players() {
        return frontPlayer;
    }

    public void setPlayers(SortedSet<Player> players) {
        SortedSet<Player> oldPlayers = this.frontPlayer;
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

    public void diceRolledAction(DicePayload payload, com.example.frontend.Player currentPlayer) {
        if (payload.player().name().equals(currentPlayer.getUsername())) {
            support.firePropertyChange(Property.MOVE_CHARACTER.name(), 0, payload.diceValue());
            setDiceVal = payload.diceValue();
        } else {
            support.firePropertyChange(Property.DICE_ROLLED.name(), null, payload);
        }
    }

    public void sendMoveOnFieldRequest(Character c) {
        int totalStep = c.steps() + setDiceVal;
        int postition = (c.position() + setDiceVal) % 37;

        if (totalStep >= 29) {
            Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), postition, MoveType.MOVE_TO_GOAL, totalStep)));
        } else {
            Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), postition, MoveType.MOVE_ON_FIELD, totalStep)));
        }
        resetMyTurn();
    }

    public com.example.frontend.Player getCurrentPlayer() {
        if (frontPlayer.isEmpty()) {
            Log.e(TAG, "No players available.");
            return null;
        }

        if (currentPlayer == null) {
            for (Player player : frontPlayer) {
                if (player.getUsername().equals(playerName)) {
                    return currentPlayer = player;
                } else {
                    Log.e("App", "Player with name " + playerName + " does not exist");
                }
            }
        }

        return currentPlayer;
    }

    public void mapStartPositions() {
        mapStartingPoint.put(Color.YELLOW, 27);
        mapStartingPoint.put(Color.PINK, 33);
        mapStartingPoint.put(Color.RED, 15);
        mapStartingPoint.put(Color.GREEN, 21);
        mapStartingPoint.put(Color.LIGHT_BLUE, 9);
        mapStartingPoint.put(Color.DARK_BLUE, 3);
    }

    public void initializePlayerPositions() {
        for (Player player : frontPlayer) {
            Integer startPosition = mapStartingPoint.get(player.color());
            if (startPosition != null) {
                playerPositions.put(player, startPosition);
            } else {
                playerPositions.put(player, 0); // Default to position 0 if color not found
            }
        }
    }

    public void setPlayerPosition(Player player, int position) {
        Integer oldPosition = playerPositions.get(player);
        playerPositions.put(player, position);
        support.firePropertyChange("playerPosition", Optional.ofNullable(oldPosition), position);
    }

    public int getPlayerPosition(Player player) {
        return playerPositions.getOrDefault(player, 0); // Return -1 if no position set
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % frontPlayer.size();
        support.firePropertyChange("currentPlayer", null, getCurrentPlayer());
    }

    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    private void updateCharacterState(Player player, int characterIndex, CharacterState newState) {
        if (player == null) {
            Log.e(TAG, "Player object is null in updateCharacterState");
            return;
        }

        if (newState == null) {
            Log.e(TAG, "CharacterState is null in updateCharacterState");
            return;
        }

        Player updatedPlayer = player.updateCharacterState(characterIndex, newState);
        if (updatedPlayer == null) {
            Log.e(TAG, "Updated player is null after calling updateCharacterState");
            return;
        }
        frontPlayer.remove(player);
        frontPlayer.add(updatedPlayer);
        broadcastMove(updatedPlayer, playerPositions.get(player), playerPositions.get(updatedPlayer));
    }

    private void broadcastMove(Player player, int oldPosition, int newPosition) {
        if (player == null) {
            Log.e(TAG, "Player object is null in broadcastMove");
            return;
        }
    }

    public void addPlayers(List<Player> playersList) {
        for (Player player : playersList) {
            if (!frontPlayer.contains(player)) {
                Color color = Color.values()[frontPlayer.size() % Color.values().length];
                Player coloredPlayer = new Player(player.getUsername(), player.getAge(), player.getCharacters(), player.color());
                frontPlayer.add(coloredPlayer);
                playerPositions.put(coloredPlayer, 0);
                canMove.put(coloredPlayer, false);
                Log.i(TAG, "Added player: " + coloredPlayer.getUsername());
            }
        }
    }

    public void updatePlayer(Player oldPlayer, Player newPlayer) {
        if (frontPlayer.contains(oldPlayer)) {
            Log.i(TAG, newPlayer.getUsername() + " updatePlayerMethod");
            frontPlayer.remove(oldPlayer);
            frontPlayer.add(newPlayer);
            playerPositions.put(newPlayer, playerPositions.get(oldPlayer)); // Alte Position beibehalten
            canMove.put(newPlayer, canMove.getOrDefault(oldPlayer, false)); // Bewegungsstatus beibehalten
            support.firePropertyChange("players", oldPlayer, newPlayer); // Benachrichtigen der Listener
        }
    }

    public void setMyTurn() {
        myTurn = true;
        support.firePropertyChange(Property.YOUR_TURN.name(), false, true);
    }

    public void resetMyTurn() {
        myTurn = false;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void nameAlreadyExists() {
        support.firePropertyChange(Property.USERNAME_ALREADY_EXISTS.name(), false, true);
    }

    public void playerRegistered() {
        support.firePropertyChange(Property.PLAYER_REGISTERED.name(), false, true);
    }

    public Character getNextCharacterForStart() {
        for (Character c : Game.INSTANCE.getCurrentPlayer().characters) {
            if (c.status().equals(CharacterState.HOME)) {
                return c;
            }
        }
        return null;
    }

    public int moveCharacterToStartingPostion(Character c) {
        int position = mapStartingPoint.get(currentPlayer.color());
        Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), position, MoveType.MOVE_TO_FIELD, 1)));
        characterOnBoard = true;
        resetMyTurn();
        return position;
    }

    public void updateCharacterPosition(UUID uuid, int i, MoveType moveType, int steps) {
        for (Player p : frontPlayer) {
            for (Character c : p.characters) {
                if (c.id().equals(uuid)) {
                    int oldPosition = c.position();
                    CharacterState state = null;
                    if (moveType.equals(MoveType.MOVE_TO_FIELD) || moveType.equals(MoveType.MOVE_ON_FIELD)) {
                        state = CharacterState.FIELD;
                    } else if (moveType.equals(MoveType.MOVE_TO_GOAL)) {
                        state = CharacterState.GOAL;
                    /*   } else if (moveType.equals(MoveType.MOVE_TO_HOME)) {
                        state = CharacterState.HOME;
                    }
                    */
                    }

                    Character newCharacter = new Character(c.id(), i, state, steps);

                    p.setCharacters(p.characters.stream().map(character -> character.equals(c)
                                    ? newCharacter
                                    : character)
                            .collect(Collectors.toCollection(ArrayList::new)));

                    UpdatePositionObject upo = new UpdatePositionObject(newCharacter, p, moveType, oldPosition);

                    support.firePropertyChange(Property.UPDATE_CHARACTER_POSITION.name(), null, upo);
                    return;
                }
            }
        }

        throw new Resources.NotFoundException("Character not found");
    }


    public Player winner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        support.firePropertyChange(Property.WINNER.name(), null, winner);
    }
    public boolean cheat_used;
    public void usedCheatAction(CheatPayload cheatpayload, Player currentPlayer) {
        if(cheatpayload.player().name().equals(currentPlayer.getUsername())){
            support.firePropertyChange(Property.PLAYER_USED_CHEAT.name(),false, true);
            cheat_used=cheatpayload.cheatUsed();
            Log.i(TAG,"Cheat was used by player " + cheatpayload.player().name());
        }else{
            support.firePropertyChange(Property.PLAYER_HAS_CHEAT.name(), true, cheatpayload);
        }
    }

    enum Property {
        PLAYER_HAS_CHEAT,PLAYER_USED_CHEAT,PLAYERS, GAME_STATE, WINNER, DICE_ROLLED, MOVE_CHARACTER, YOUR_TURN, USERNAME_ALREADY_EXISTS, PLAYER_REGISTERED, UPDATE_CHARACTER_POSITION, DICE_THROWN,PLAYER_POSITION;
    }
}
