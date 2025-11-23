package ru.mipt.bit.platformer.logic.shooting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.logic.collision.impl.BulletCollisionDetector;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerShootingProcessor {
    private final BulletCollisionDetector collisionDetector;

    @Autowired
    public PlayerShootingProcessor(BulletCollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    public void updateBulletsState(World world, float deltaTime) {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : world.getBullets()) {
            bullet.update(deltaTime);
            List<BulletCollisionDetector.CollisionResult> collisions = collisionDetector.getCollisionStatus(world, bullet);
            if (!collisions.isEmpty()) {
                BulletCollisionDetector.CollisionResult collision = collisions.get(0);
                bulletsToRemove.add(bullet);

                if (collision.getCollisionType() == BulletCollisionDetector.CollisionType.TANK && collision.getHittedPlayer() != null) {
                    Player hittedTank = collision.getHittedPlayer();
                    hittedTank.takeHit(bullet.getDamage());
                    if (!hittedTank.isAlive()) {
                        if (hittedTank == world.getPlayer()) {
                            world.getPlayer().takeHit(0);
                        } else {
                            world.getBotTanks().remove(hittedTank);
                        }
                    }
                }

                if (collision.getCollisionType() == BulletCollisionDetector.CollisionType.BULLET) {
                    for (Bullet other : world.getBullets()) {
                        if (other != bullet && other.getPosition().equals(bullet.getPosition())) {
                            bulletsToRemove.add(other);
                        }
                    }
                }
            }
        }

        for (Bullet bullet : bulletsToRemove) {
            world.removeBullet(bullet);
        }
    }
}
