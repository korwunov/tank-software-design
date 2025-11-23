package ru.mipt.bit.platformer.logic.collision.impl;

import com.badlogic.gdx.math.GridPoint2;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

import java.util.ArrayList;
import java.util.List;

@Component
public class BulletCollisionDetector {
    public enum CollisionType {
        TANK, OBSTACLE, BULLET, BOUND
    }

    public static class CollisionResult {
        private CollisionType collisionType;
        private Player player;

        public CollisionResult(CollisionType type, Player hittedPlayer) {
            this.collisionType = type;
            this.player = hittedPlayer;
        }

        public CollisionType getCollisionType() {
            return collisionType;
        }

        public Player getHittedPlayer() {
            return player;
        }
    }

    public List<CollisionResult> getCollisionStatus(World world, Bullet bullet) {
        List<CollisionResult> results = new ArrayList<>();
        GridPoint2 bulletPosition = bullet.getPosition();
        if (!world.getTileGrid().isValidPosition(bulletPosition)) {
            results.add(new CollisionResult(CollisionType.BOUND, null));
            return results;
        }

        if (world.hasObstacleAt(bulletPosition)) {
            results.add(new CollisionResult(CollisionType.OBSTACLE, null));
            return results;
        }

        Player hitTank = world.getTankAt(bulletPosition);
        if (hitTank != null) {
            results.add(new CollisionResult(CollisionType.TANK, hitTank));
            return results;
        }

        for (Bullet otherBullet : world.getBullets()) {
            if (otherBullet != bullet && otherBullet.getPosition().equals(bulletPosition)) {
                results.add(new CollisionResult(CollisionType.BULLET, null));
                return results;
            }
        }

        return results;
    }
}
