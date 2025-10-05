package ru.mipt.bit.platformer.logic.collision;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;

import java.util.ArrayList;
import java.util.List;

public interface CollisionDetector {
    boolean isMovePossible(GridPoint2 fromPosition, Direction direction, List<Obstacle> obstacles);
}
