package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class DrawableUpdater {
    private Batch batch;

    public DrawableUpdater(Batch batch) {
        this.batch = batch;
    }

    public void update(List<GraphicsObject> objectsToUpdate, Batch batch) {
        batch.begin();
        objectsToUpdate.forEach(o -> drawTextureRegionUnscaled(this.batch, o.getGraphics(), o.getRectangle(), o.getRotation()));
        batch.end();
    }
}
