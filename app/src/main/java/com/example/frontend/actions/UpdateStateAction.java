package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.Player;
import com.example.frontend.responseHandler.Action;

import java.util.TreeSet;
import java.util.stream.Collectors;

import at.aau.payloads.Payload;
import at.aau.payloads.UpdateStatePayload;

public class UpdateStateAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof UpdateStatePayload updateStatePayload) {
            game.setGameState(updateStatePayload.game().gameState());
            game.setPlayers(updateStatePayload.game().players().stream()
                    .map(player -> new Player(player.name(), player.age(), player.characters(), player.color()))
                    .collect(Collectors.toCollection(TreeSet::new)));
        } else Log.e("App", "Payload is not an instance of UpdateStatePayload");
    }
}
