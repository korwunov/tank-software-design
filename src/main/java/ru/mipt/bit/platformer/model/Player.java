package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.Drawable;
import ru.mipt.bit.platformer.graphics.GraphicsObject;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Player extends GraphicsObject implements Drawable {
    public GridPoint2 playerDestinationCoordinates;
    public GridPoint2 playerCoordinates;
    private float playerMovementProgress;
    private int health;
    private final int maxHealth;

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final String PATH_TO_PLAYER_TEXTURE = "images/tank_blue.png";

    public Player(GridPoint2 startCoordinates, float playerMovementProgress) {
        this.playerDestinationCoordinates = new GridPoint2(startCoordinates);
        this.playerCoordinates = new GridPoint2(startCoordinates);
        this.rotation = 0f;
        this.health = 100;
        this.maxHealth = 100;
        this.playerMovementProgress = playerMovementProgress;
        this.draw(PATH_TO_PLAYER_TEXTURE);
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
        this.rotation = playerRotation;
    }

    public GridPoint2 getPlayerDestinationCoordinates() {
        return playerDestinationCoordinates;
    }

    public GridPoint2 getPlayerCoordinates() {
        return playerCoordinates;
    }

    public float getPlayerRotation() {
        return rotation;
    }

    public void updateProgress(float time) {
        this.playerMovementProgress = clamp(this.playerMovementProgress + time / MOVEMENT_SPEED, 0f, 1f);
        if (isEqual(playerMovementProgress, 1f)) {
            // record that the player has reached his/her destination
            playerCoordinates.set(playerDestinationCoordinates);
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Direction getFacingDirection() {
        for (Direction dir : Direction.values()) {
            if (Math.abs(dir.getRotation() - rotation) < 0.1f) {
                return dir;
            }
        }
        return Direction.RIGHT;
    }

    public Bullet shoot() {
        Direction facing = getFacingDirection();
        GridPoint2 bulletStart = new GridPoint2(playerCoordinates).add(facing.getX(), facing.getY());;
        return new Bullet(bulletStart, facing);
    }
}
