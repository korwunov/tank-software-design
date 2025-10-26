package ru.mipt.bit.platformer.logic.movements;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Player;

import java.util.ArrayList;
import java.util.List;

public interface MovementsProcessor {
    void processMoveCommand(Player player, Direction direction, List<Obstacle> obstacles);
}
