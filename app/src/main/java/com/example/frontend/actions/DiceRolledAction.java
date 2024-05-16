package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.payloads.DicePayload;
import at.aau.payloads.Payload;
import at.aau.payloads.YourTurnPayload;

public class DiceRolledAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof DicePayload){

            Log.i("App", "Dice rolled received");
        }
        Log.e("App", "Response type not recognized");
    }
}