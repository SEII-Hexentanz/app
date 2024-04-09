package com.example.frontend.responseHandler;

import at.aau.payloads.Payload;

@FunctionalInterface
public interface Action {
    void execute(Payload payload);
}
