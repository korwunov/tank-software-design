package ru.mipt.bit.platformer.logic.collision.impl;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;

import java.util.ArrayList;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedX;

public class PlayerCollisionDetector implements CollisionDetector {
    @Override
    public boolean isMovePossible(GridPoint2 fromPosition, Direction direction, ArrayList<Obstacle> obstacles) {
        GridPoint2 candidate = fromPosition.add(direction.getX(), direction.getY());
        return obstacles.stream().anyMatch(obstacle -> obstacle.getPosition().equals(candidate));
    }
}
