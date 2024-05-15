package com.example.frontend.actions;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.frontend.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.SortedSet;
import java.util.TreeSet;

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
        // Given
        GameState gameState = Mockito.mock(GameState.class);
        SortedSet<Player> players = new TreeSet<>();
        Player mockPlayer = Mockito.mock(Player.class);
        when(mockPlayer.color()).thenReturn(Color.RED);
        players.add(mockPlayer);
        at.aau.models.GameData mockGame = Mockito.mock(at.aau.models.GameData.class);
        when(mockGame.gameState()).thenReturn(gameState);
        when(mockGame.players()).thenReturn(players);
        UpdateStatePayload payload = new UpdateStatePayload(mockGame);

        // When
        updateStateAction.execute(game, payload);

        // Then
        assertSame(gameState, game.gameState());
        assertSame(players.size(), game.players().size());
    }

    @Test
    public void execute_throwsException_whenPayloadIsNotUpdateStatePayload() {
        // Given
        Payload payload = Mockito.mock(Payload.class);
        Game game = Mockito.mock(Game.class);
        // When
        updateStateAction.execute(game, payload);
        verify(game, never()).setGameState(any());
        verify(game, never()).setPlayers(any());
    }
}
