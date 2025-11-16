package ru.mipt.bit.platformer.graphics.render.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.graphics.render.Renderer;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class HealthStatRenderer implements Renderer, Disposable {
    private boolean isHealthStatsVisible;
    private final Texture texture;
    private final TextureRegion textureRegion;

    public HealthStatRenderer(boolean isHealthStatsVisible) {
        this.isHealthStatsVisible = isHealthStatsVisible;
        this.texture = createPixelTexture();
        this.textureRegion = new TextureRegion(this.texture);
    }

    @Override
    public Rectangle render(Player player, Batch batch, TileMovement tileMovement) {
        Rectangle bounds = createBoundingRectangle(this.textureRegion);
        if (!this.isHealthStatsVisible) return bounds;
        float barHeight = bounds.height * 0.1f;
        float y = bounds.y + bounds.height + barHeight * 0.3f;
        float width = bounds.width;
        int healthPercent = player.getHealth() / player.getMaxHealth();

        Color previous = new Color(batch.getColor());
        batch.setColor(0f, 0f, 0f, previous.a);
        batch.draw(textureRegion, bounds.x, y, width, barHeight);

        batch.setColor(0.95f, 0.15f, 0.15f, previous.a);
        batch.draw(textureRegion, bounds.x, y, width * healthPercent, barHeight);

        batch.setColor(previous);
        return bounds;
    }

    public void setHealthStatsVisible(boolean healthStatsVisible) {
        isHealthStatsVisible = healthStatsVisible;
    }

    private static Texture createPixelTexture() {
        Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
