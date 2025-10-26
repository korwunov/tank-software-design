package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.TileGrid;
import ru.mipt.bit.platformer.model.World;

import java.io.IOException;

public interface LevelLoader {
    World loadLevel(TileGrid tileGrid)  throws IOException;
}
