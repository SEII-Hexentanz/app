package com.example.frontend;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import at.aau.payloads.DicePayload;
import at.aau.payloads.PlayerMovePayload;
import at.aau.values.CharacterState;
import at.aau.values.Color;
import at.aau.values.CommandType;
import at.aau.values.GameState;
import at.aau.values.MoveType;

public enum Game {
    INSTANCE;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final SortedSet<at.aau.models.Player> players = new TreeSet<>();
    private SortedSet<Player> frontPlayer = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private final Map<Player, Integer> playerPositions = new HashMap<>();
    private int currentPlayerIndex = 0;
    public static final String TAG = "GAME_TAG";
    private final Map<Player, Boolean> canMove = new HashMap<>();
    private GameEventListener eventListener;
    private DiceFragment diceFragment;
    private String playerName;
    private Boolean myTurn = false;
    final HashMap<Color, Integer> mapStartingPoint= new HashMap<>();;

    private Player currentPlayer = null;
    public ArrayList<ImageView> gameboardPositions;
    HashMap<at.aau.values.Color, Integer> mapGoalPoint;
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

    public SortedSet<at.aau.models.Player> players(){
        return players;
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

    public void diceRolledAction(DicePayload payload, Player currentPlayer){
        if(payload.player().name().equals(currentPlayer.getUsername())){
            support.firePropertyChange(Property.MOVE_CHARACTER.name(), 0, payload.diceValue());
        }else {
            support.firePropertyChange(Property.DICE_ROLLED.name(), null, payload);
        }
    }

    public Player getCurrentPlayer() {
        if (frontPlayer.isEmpty()) {
            Log.e(TAG, "No players available.");
            return null;
        }

        if(currentPlayer == null){
            for(Player player : frontPlayer){
                if(player.getUsername().equals(playerName)){
                    return currentPlayer = player;
                } else {
                    Log.e(TAG, "Player with name " + playerName + " does not exist");
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

    public void initializeGameBoard(View view) {
        gameboardPositions = new ArrayList<>();
        for (int i = 0; i <= 35; i++) {
            int resID = view.getResources().getIdentifier("gameboardpos" + i, "id", view.getContext().getPackageName());
            gameboardPositions.add(view.findViewById(resID));
        }

        mapGoalPositions();
        mapStartPositions();
    }

    void mapGoalPositions() {
        mapGoalPoint = new HashMap<>();
        mapGoalPoint.put(Color.YELLOW, 26);
        mapGoalPoint.put(Color.PINK, 32);
        mapGoalPoint.put(Color.RED, 14);
        mapGoalPoint.put(Color.GREEN, 20);
        mapGoalPoint.put(Color.LIGHT_BLUE, 9);
        mapGoalPoint.put(Color.DARK_BLUE, 3);
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
        Log.i(TAG, "Player " + player.getUsername() + " position set to " + position + " from " + oldPosition);
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
        UpdatePositionObject upo = new UpdatePositionObject(player.getCharacters().get(0), player, MoveType.MOVE_ON_FIELD, oldPosition);
        support.firePropertyChange(Property.UPDATE_CHARACTER_POSITION.name(), null, upo);
    }


    public void movePlayer(int diceResult) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Log.e(TAG, "No current player found. Cannot move.");
            return;
        }

        boolean hasAnotherTurn = false;
        int currentPosition = getPlayerPosition(currentPlayer);
        int newPosition;
        int maxPosition = 36;

        if (currentPosition == 0 && diceResult == 6) {
            newPosition = mapStartingPoint.get(currentPlayer.color());
            setPlayerPosition(currentPlayer, newPosition);
            broadcastMove(currentPlayer, 0, newPosition);
            notifyPlayerPositionChanged(currentPlayer, 0, newPosition);
            hasAnotherTurn = true;
            Log.i(TAG, "setPlayerPosition inside 1st if" + getPlayerPosition(currentPlayer));
        }
        else if (currentPosition > 0) {
            newPosition = (currentPosition + diceResult) % (maxPosition + 1);
            setPlayerPosition(currentPlayer, newPosition);
            broadcastMove(currentPlayer, currentPosition, newPosition);
            notifyPlayerPositionChanged(currentPlayer, currentPosition, newPosition);
            gameboardPositions.get(newPosition).setImageResource(R.drawable.playericon);
            Log.i(TAG, "Updated Position to newPosition " + newPosition + " || Current Position is " + currentPosition);
            Log.i(TAG, "setPlayerPosition insinde else" + getPlayerPosition(currentPlayer));
            if (diceResult == 6) {
                hasAnotherTurn = true;
            }
        } else {
            Log.i(TAG, "Player " + currentPlayer.getUsername() + " cannot move out of Home without rolling a 6.");
        }

        if (!hasAnotherTurn) {
            nextPlayer();
        }
    }

    public void notifyPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
        if (eventListener != null) {
            eventListener.onPlayerPositionChanged(player, oldPosition, newPosition);
        }
    }


    public void addPlayers(List<Player> playersList) {
        for (Player player : playersList) {
            if (!frontPlayer.contains(player)) {
                Color color = Color.values()[frontPlayer.size() % Color.values().length];
                Player coloredPlayer = new Player(player.getUsername(), player.getAge(), player.getCharacters(),player.color());
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

    public void setMyTurn(){
        myTurn = true;
        support.firePropertyChange(Property.YOUR_TURN.name(), false, true);
    }

    public void resetMyTurn(){
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
    public Character getNextCharacterForStart(){
        for(Character c: Game.INSTANCE.getCurrentPlayer().characters){
            if(c.status().equals(CharacterState.HOME)){
                return c;
            }
        }
        return null;
    }

    public int moveCharacterToStartingPostion(Character c) {
        int position = mapStartingPoint.get(currentPlayer.color());
        Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), position, MoveType.MOVE_TO_FIELD)));
        return position;
    }

    public void updateCharacterPosition(UUID uuid, int i, MoveType moveType) {
        for(Player p : frontPlayer){
            for(Character c : p.characters){
                if(c.id().equals(uuid)){
                    int oldPosition = c.position();
                    Character newCharacter = new Character(c.id(), i, c.status());
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

    private Character getCharacterById(UUID uuid) {
        for(Player p : frontPlayer){
            for(Character c : p.characters){
                if(c.id().equals(uuid)){
                    return c;
                }
            }
        }

        throw new Resources.NotFoundException("Character not found");
    }

    enum Property {
        PLAYERS, GAME_STATE, DICE_ROLLED, MOVE_CHARACTER, YOUR_TURN, USERNAME_ALREADY_EXISTS, PLAYER_REGISTERED, UPDATE_CHARACTER_POSITION
    }
}