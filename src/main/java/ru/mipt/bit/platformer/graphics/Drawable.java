package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public interface Drawable {
    public TextureRegion getGraphics();
    public Rectangle getRectangle();
}
