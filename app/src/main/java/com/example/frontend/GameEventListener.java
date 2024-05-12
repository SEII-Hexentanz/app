package com.example.frontend;

import at.aau.models.Player;

public interface GameEventListener {

    void onPlayerPositionChanged(Player player, int oldPos, int newPos);
}
