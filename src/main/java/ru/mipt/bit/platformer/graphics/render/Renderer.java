package ru.mipt.bit.platformer.graphics.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.util.TileMovement;

public interface Renderer {
    Rectangle render(Player player, Batch batch, TileMovement tileMovement);
}
