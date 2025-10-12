package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.Drawable;
import ru.mipt.bit.platformer.graphics.GraphicsObject;

import java.util.Objects;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Obstacle extends GraphicsObject implements Drawable {
    private final GridPoint2 position;
    private final ObstacleType type;

    public Obstacle(GridPoint2 position, ObstacleType type, TiledMapTileLayer layer) {
        this.position = position;
        this.type = type;
        this.rotation = 0f;
        this.draw(type.getPathToTexture());
        moveRectangleAtTileCenter(layer, this.rectangle, this.position);
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
