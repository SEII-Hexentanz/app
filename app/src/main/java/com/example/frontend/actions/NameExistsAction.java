package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.payloads.Payload;

public class NameExistsAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        game.nameAlreadyExists();
        Log.e("App", "Name already exists received from server");
    }
}