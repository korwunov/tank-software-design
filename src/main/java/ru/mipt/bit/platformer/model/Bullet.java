package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.input.InputEvent.move;

public class Bullet {
    private GridPoint2 position;
    private final Direction direction;
    private final float speed = 1f;
    private final int damage = 5;
    private float movementProgress = 0f;

    public Bullet(GridPoint2 position, Direction direction) {
        this.direction = direction;
        this.position = position;
    }

    public GridPoint2 getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public void update(float deltaTime) {
        if (movementProgress < 1f) {
            movementProgress = Math.min(1f, movementProgress + deltaTime / speed);
            if (movementProgress >= 1f) {
                position.set(new GridPoint2(position).add(direction.getX(), direction.getY()));
                movementProgress = 0f;
            }
        }
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(position).add(direction.getX(), direction.getY());
    }
}
