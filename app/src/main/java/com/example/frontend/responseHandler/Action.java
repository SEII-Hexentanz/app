package com.example.frontend.responseHandler;

import com.example.frontend.Game;

import at.aau.payloads.Payload;

@FunctionalInterface
public interface Action {
    void execute(Game game, Payload payload);
}
