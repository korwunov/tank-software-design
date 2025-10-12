package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public abstract class GraphicsObject {
    private Texture texture;
    private TextureRegion graphics;
    protected Rectangle rectangle;
    protected float rotation;

    public void draw(String pathToTexture) {
        this.texture = new Texture(pathToTexture);
        this.graphics = new TextureRegion(this.texture);
        this.rectangle = createBoundingRectangle(this.graphics);
    }

    public Texture getTexture() {
        return texture;
    }

    public TextureRegion getGraphics() {
        return graphics;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public float getRotation() {
        return rotation;
    }
}
