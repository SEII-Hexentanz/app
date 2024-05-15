package com.example.frontend.actions;

import android.util.Log;
import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;
import at.aau.payloads.Payload;
import at.aau.payloads.TimerPayload;

public class TimerRunningAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof TimerPayload timerRunningPayload) {
            Log.i("App", "Timer running");
            // Handle timer running action in game
        } else {
            Log.e("App", "Payload is not an instance of TimerRunningPayload");
        }
    }
}
