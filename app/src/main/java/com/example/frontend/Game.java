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
    private SortedSet<com.example.frontend.Player> frontPlayer = new TreeSet<>();
    private GameState gameState = GameState.LOBBY;
    private final Map<com.example.frontend.Player, Integer> playerPositions = new HashMap<>();
    private int currentPlayerIndex = 0;
    public static final String TAG = "GAME_TAG";
    private final Map<com.example.frontend.Player, Boolean> canMove = new HashMap<>();
    private GameEventListener eventListener;
    private DiceFragment diceFragment;
    private String playerName;
    private Boolean myTurn = false;
    private int setDiceVal;
    final HashMap<at.aau.values.Color, Integer> mapStartingPoint= new HashMap<>();;

    private Player currentPlayer = null;

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

    public void diceRolledAction(DicePayload payload, com.example.frontend.Player currentPlayer){
        if(payload.player().name().equals(currentPlayer.getUsername())){
            support.firePropertyChange(Property.MOVE_CHARACTER.name(), 0, payload.diceValue());
            setDiceVal = payload.diceValue();

        }else {
            support.firePropertyChange(Property.DICE_ROLLED.name(), null, payload);

        }
    }
    public void sendMoveOnFieldRequest(Character c){
        int totalStep =c.steps() + setDiceVal;
        int postition = (c.position()+setDiceVal) % 37;
        if(totalStep>=27){
            Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), postition, MoveType.MOVE_TO_GOAL, totalStep)));
        }else{
            Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), postition, MoveType.MOVE_ON_FIELD,totalStep)));
        }
        resetMyTurn();
    }


    public com.example.frontend.Player getCurrentPlayer() {
        if (frontPlayer.isEmpty()) {
            Log.e(TAG, "No players available.");
            return null;
        }

        if(currentPlayer == null){
            for(Player player : frontPlayer){
                if(player.getUsername().equals(playerName)){
                    return currentPlayer = player;
                } else {
                    Log.e("App", "Player with name " + playerName + " does not exist");
                }
            }
        }

        return currentPlayer;
    }
    public void mapStartPositions() {
        mapStartingPoint.put(at.aau.values.Color.YELLOW, 27);
        mapStartingPoint.put(at.aau.values.Color.PINK, 33);
        mapStartingPoint.put(at.aau.values.Color.RED, 15);
        mapStartingPoint.put(at.aau.values.Color.GREEN, 21);
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
         //Response response = new Response(ResponseType.UPDATE_STATE, new PlayerMovePayload(oldPosition, newPosition, player.getUsername()));
        //ResponseHandler.execute(response.responseType(),response.payload(),Game.INSTANCE);

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
       //     Client.send(new Request(CommandType.PLAYER_MOVE,new PlayerMovePayload(currentPosition,newPosition, currentPlayer.getUsername())));
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

    public void moveToStart() {
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
       Client.send(new Request(CommandType.PLAYER_MOVE, new PlayerMovePayload(c.id(), position, MoveType.MOVE_TO_FIELD,0)));
       resetMyTurn();
        return position;
    }

    public void updateCharacterPosition(UUID uuid, int i, MoveType moveType,int steps) {
        for(Player p : frontPlayer){
            for(Character c : p.characters){
                if(c.id().equals(uuid)){
                    int oldPosition = c.position();
                    Character newCharacter = new Character(c.id(), i, c.status(),steps);
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
        PLAYERS, GAME_STATE, DICE_ROLLED, MOVE_CHARACTER, YOUR_TURN, USERNAME_ALREADY_EXISTS, PLAYER_REGISTERED, UPDATE_CHARACTER_POSITION, DICE_THROWN
    }
}