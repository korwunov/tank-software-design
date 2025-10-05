package ru.mipt.bit.platformer.model;

public enum ObstacleType {
    TREE("images/greenTree.png",true);

    private final String pathToTexture;
    private final boolean isPassable;

    ObstacleType(String pathToTexture, boolean isPassable) {
        this.pathToTexture = pathToTexture;
        this.isPassable = isPassable;
    }

    public String getPathToTexture() { return this.pathToTexture; }

    public boolean isPassable() { return this.isPassable; }
}
