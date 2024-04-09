package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.responseHandler.Action;

import at.aau.payloads.Payload;

/**
 * This is a fallback action that is executed when the response type is not present in the predefined map.
 */
public class DefaultAction implements Action {
    @Override
    public void execute(Payload payload) {
        Log.e("App", "Response type not recognized");
    }
}
