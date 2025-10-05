package ru.mipt.bit.platformer.model;

public enum Direction {
    UP(0, 1, 90f),
    DOWN(0, -1, -90f),
    LEFT(-1, 0, -180f),
    RIGHT(1, 0, 0f);

    private final int x;
    private final int y;
    private final float rotation;

    Direction(int x, int y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }
}
