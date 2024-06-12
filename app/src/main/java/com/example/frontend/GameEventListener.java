package com.example.frontend;

import at.aau.models.Player;

public interface GameEventListener {

    void onPlayerPositionChanged(com.example.frontend.Player player, int oldPos, int newPos);
}
