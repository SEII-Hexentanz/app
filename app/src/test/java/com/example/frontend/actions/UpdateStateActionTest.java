package com.example.frontend.actions;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.frontend.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.TreeSet;

import at.aau.models.GameData;
import at.aau.models.Player;
import at.aau.payloads.Payload;
import at.aau.payloads.UpdateStatePayload;
import at.aau.values.Color;
import at.aau.values.GameState;

public class UpdateStateActionTest {
    private UpdateStateAction updateStateAction;
    private Game game;

    @BeforeEach
    public void setUp() {
        updateStateAction = new UpdateStateAction();
        game = Game.INSTANCE;
    }

    @org.junit.jupiter.api.Test
    public void execute_updatesGameStateAndPlayers_whenPayloadIsUpdateStatePayload() {
        var players = new TreeSet<Player>();
        var player = new Player("Player", 20, Color.RED, false, new ArrayList<>());
        players.add(player);
        var gameData = new GameData(players, GameState.RUNNING, 2);
        var payload = new UpdateStatePayload(gameData);

        // When
        updateStateAction.execute(game, payload);

        // Then
        assertSame(GameState.RUNNING, game.gameState());
        assertSame(players.size(), game.players().size());
    }

    @Test
    public void execute_throwsException_whenPayloadIsNotUpdateStatePayload() {
        // Given
        Payload payload = Mockito.mock(Payload.class);
        Game mockedGame = Mockito.mock(Game.class);
        // When
        updateStateAction.execute(mockedGame, payload);
        verify(mockedGame, never()).setGameState(any());
        verify(mockedGame, never()).setPlayers(any());
    }
}
