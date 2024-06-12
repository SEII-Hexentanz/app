package com.example.frontend;

import at.aau.models.Character;
import at.aau.values.MoveType;

public class UpdatePositionObject {
    private final Character character;
    private final Player player;
    private final MoveType moveType;
    private final int oldPosition;

    public UpdatePositionObject(Character character, Player player, MoveType moveType, int oldPosition) {
        this.character = character;
        this.player = player;
        this.moveType = moveType;
        this.oldPosition = oldPosition;
    }

    public Character getCharacter() {
        return character;
    }

    public Player getPlayer() {
        return player;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public int getOldPosition() {
        return oldPosition;
    }
}
