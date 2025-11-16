package ru.mipt.bit.platformer.graphics.render.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.util.TileMovement;

public class BulletRenderer {
    private final ShapeRenderer shapeRenderer;
    private final float bulletSize;

    public BulletRenderer(float bulletSize) {
        this.bulletSize = bulletSize;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(Bullet bullet, Batch batch, TileMovement tileMovement) {
        Rectangle bulletRect = new Rectangle(0, 0, bulletSize, bulletSize);
        tileMovement.moveRectangleBetweenTileCenters(bulletRect, bullet.getPosition(), bullet.getDestinationCoordinates(), bullet.getMovementProgress());

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.circle(bulletRect.x + bulletRect.width / 2, bulletRect.y + bulletRect.height / 2, bulletSize / 2);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
