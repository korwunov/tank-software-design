package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Player {
    public GridPoint2 playerDestinationCoordinates;
    public GridPoint2 playerCoordinates;
    public float playerRotation;
    private float playerMovementProgress;
    private static final float MOVEMENT_SPEED = 0.4f;

    public Player(GridPoint2 startCoordinates) {
        this.playerDestinationCoordinates = new GridPoint2(startCoordinates);
        this.playerCoordinates = new GridPoint2(startCoordinates);
        this.playerRotation = 0f;
        this.playerMovementProgress = 1f;
    }

    public float getPlayerMovementProgress() {
        return playerMovementProgress;
    }

    public void setPlayerMovementProgress(float playerMovementProgress) {
        this.playerMovementProgress = playerMovementProgress;
    }

    public void setPlayerDestinationCoordinates(GridPoint2 playerDestinationCoordinates) {
        this.playerDestinationCoordinates = playerDestinationCoordinates;
    }

    public void setPlayerCoordinates(GridPoint2 playerCoordinates) {
        this.playerCoordinates = playerCoordinates;
    }

    public void setPlayerRotation(float playerRotation) {
        this.playerRotation = playerRotation;
    }

    public GridPoint2 getPlayerDestinationCoordinates() {
        return playerDestinationCoordinates;
    }

    public GridPoint2 getPlayerCoordinates() {
        return playerCoordinates;
    }

    public float getPlayerRotation() {
        return playerRotation;
    }

    public void updateProgress(float time) {
        this.playerMovementProgress = clamp(this.playerMovementProgress + time / MOVEMENT_SPEED, 0f, 1f);
        if (isEqual(playerMovementProgress, 1f)) {
            // record that the player has reached his/her destination
            playerCoordinates.set(playerDestinationCoordinates);
        }
    }
}
