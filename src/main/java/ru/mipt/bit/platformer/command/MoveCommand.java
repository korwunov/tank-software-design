package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.logic.collision.impl.PlayerCollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class MoveCommand implements Command {
    private final Player player;
    private final Direction direction;
    private final CollisionDetector collisionDetector;

    public MoveCommand(Player player, Direction direction, CollisionDetector collisionDetector) {
        this.player = player;
        this.direction = direction;
        this.collisionDetector = collisionDetector;
    }

    @Override
    public void execute(World world, CommandContext context) {
        if (isEqual(player.getPlayerMovementProgress(), 1f)) {
            //Проверка на коллизию
            if (collisionDetector.isMovePossible(player.getPlayerCoordinates(), direction, world.getObstacles()) && context.isFree(player.getPlayerCoordinates(), direction)) {
                player.playerDestinationCoordinates.add(direction.getX(), direction.getY());
                player.setPlayerMovementProgress(0f);
            }
            player.setPlayerRotation(direction.getRotation());
        }
        player.updateProgress(Gdx.graphics.getDeltaTime());
    }
}
