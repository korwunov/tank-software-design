package ru.mipt.bit.platformer.logic.movements.impl;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.logic.collision.impl.PlayerCollisionDetector;
import ru.mipt.bit.platformer.logic.movements.MovementsProcessor;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Player;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedX;

public class PlayerMovementsProcessor implements MovementsProcessor {
    private PlayerCollisionDetector collisionDetector = new PlayerCollisionDetector();

    @Override
    public void processMoveCommand(Player player, Direction direction, ArrayList<Obstacle> obstacles) {
        if (isEqual(player.getPlayerMovementProgress(), 1f)) {
            //Проверка на коллизию
            if (collisionDetector.isMovePossible(player.getPlayerCoordinates(), direction, obstacles)) {
                player.playerDestinationCoordinates.add(direction.getX(), direction.getY());
                player.setPlayerMovementProgress(0f);
            }
            player.setPlayerRotation(direction.getRotation());
        }
    }
}
