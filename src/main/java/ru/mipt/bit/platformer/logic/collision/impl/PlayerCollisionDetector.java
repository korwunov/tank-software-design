package ru.mipt.bit.platformer.logic.collision.impl;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;

import java.util.List;

public class PlayerCollisionDetector implements CollisionDetector {
    @Override
    public boolean isMovePossible(GridPoint2 fromPosition, Direction direction, List<Obstacle> obstacles) {
        GridPoint2 candidate = fromPosition.add(direction.getX(), direction.getY());
        return obstacles.stream().filter(obstacle -> !obstacle.getType().isPassable()).anyMatch(obstacle -> obstacle.getPosition().equals(candidate));
    }
}
