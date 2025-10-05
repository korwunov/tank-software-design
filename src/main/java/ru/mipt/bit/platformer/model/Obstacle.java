package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Obstacle {
    private final GridPoint2 position;
    private final ObstacleType type;

    public Obstacle(GridPoint2 position, ObstacleType type) {
        this.position = position;
        this.type = type;
    }

    public void addObstacleToLevel(TiledMapTileLayer layer) {
        Texture obstacleTexture = new Texture(this.type.getPathToTexture());
        Rectangle treeObstacleRectangle = createBoundingRectangle(new TextureRegion(obstacleTexture));
        moveRectangleAtTileCenter(layer, treeObstacleRectangle, this.position);
    }

    public GridPoint2 getPosition() {
        return position;
    }

    public ObstacleType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != Obstacle.class) return false;
        Obstacle obst = (Obstacle) obj;
        return this.position.equals(obst.position) && obst.type == this.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, type);
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "position=" + position +
                ", type=" + type +
                '}';
    }
}
