package ru.mipt.bit.platformer.logic.collision.impl;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedX;

public class PlayerCollisionDetector implements CollisionDetector {
    @Override
    public boolean isMovePossible(GridPoint2 fromPosition, Direction direction, ArrayList<GridPoint2>obstacles) {
        GridPoint2 candidate = switch (direction) {
            case UP -> incrementedY(fromPosition);
            case LEFT -> decrementedX(fromPosition);
            case DOWN -> decrementedY(fromPosition);
            case RIGHT -> incrementedX(fromPosition);
        };
        return !obstacles.contains(candidate);
    }
}
