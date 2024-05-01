package com.example.frontend.actions;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.payloads.GameEndPayload;
import at.aau.payloads.Payload;
import at.aau.values.GameState;

public class GameEndAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof GameEndPayload gameEndPayload) {
            game.setGameState(GameState.FINISHED);
            game.setWinner(gameEndPayload.winner());
        }
    }
}
