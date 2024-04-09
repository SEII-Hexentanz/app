package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.responseHandler.Action;

import at.aau.payloads.Payload;

public class PongAction implements Action {
    @Override
    public void execute(Payload payload) {
        Log.i("App", "Pong received from server");
    }
}
