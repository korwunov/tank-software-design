import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.mipt.bit.platformer.level.impl.RandomLevelLoader;
import ru.mipt.bit.platformer.model.*;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

class RandomLevelLoaderTest {

    private Random mockRandom;
    private TileGrid mockGrid;

    @BeforeEach
    void setUp() {
        mockRandom = new Random(12345L);
        TiledMap level = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        mockGrid = new TileGrid(groundLayer);
    }

    @Test
    @DisplayName("loadLevel: Генерирует уровень с правильным количеством препятствий")
    void loadLevel_GeneratesCorrectObstacleCount() {
        RandomLevelLoader loader = new RandomLevelLoader(mockRandom, 1.0f, 2, 4);

        World world = loader.loadLevel(mockGrid);

        int obstacleCount = world.getObstacles().size();
        assertThat(obstacleCount).isBetween(2, 4);
    }

    @Test
    @DisplayName("loadLevel: Генерирует уровень с правильной позицией игрока")
    void loadLevel_GeneratesCorrectPlayerPosition() {
        RandomLevelLoader loader = new RandomLevelLoader(mockRandom, 1.0f, 0, 0); // Без препятствий

        World world = loader.loadLevel(mockGrid);

        GridPoint2 playerPos = world.getPlayer().getPlayerCoordinates();
        assertThat(playerPos.x).isBetween(0, 4);
        assertThat(playerPos.y).isBetween(0, 4);
    }

    @Test
    @DisplayName("loadLevel: Позиция игрока не совпадает с позицией препятствия")
    void loadLevel_PlayerPositionDoesNotOverlapWithObstacle() {
        RandomLevelLoader loader = new RandomLevelLoader(mockRandom, 1.0f, 3, 3); // 3 препятствия

        World world = loader.loadLevel(mockGrid);

        GridPoint2 playerPos = world.getPlayer().getPlayerCoordinates();
        List<GridPoint2> obstaclePositions = world.getObstacles().stream()
                .map(Obstacle::getPosition)
                .toList();

        assertThat(obstaclePositions).doesNotContain(playerPos);
    }

    @Test
    @DisplayName("loadLevel: Использует заданную скорость игрока")
    void loadLevel_UsesGivenPlayerSpeed() {
        float customSpeed = 2.5f;
        RandomLevelLoader loader = new RandomLevelLoader(mockRandom, customSpeed, 1, 1);

        World world = loader.loadLevel(mockGrid);

        assertThat(world.getPlayer().getPlayerMovementProgress()).isEqualTo(customSpeed);
    }

    @Test
    @DisplayName("loadLevel: Позиции препятствий уникальны")
    void loadLevel_ObstaclePositionsAreUnique() {
        RandomLevelLoader loader = new RandomLevelLoader(mockRandom, 1.0f, 5, 5); // 5 препятствий

        World world = loader.loadLevel(mockGrid);

        List<GridPoint2> obstaclePositions = world.getObstacles().stream()
                .map(Obstacle::getPosition)
                .toList();

        assertThat(obstaclePositions).hasSize(obstaclePositions.stream().distinct().toArray().length);
    }

    @Test
    @DisplayName("loadLevel: Позиции препятствий находятся в пределах сетки")
    void loadLevel_ObstaclePositionsAreWithinGrid() {
        RandomLevelLoader loader = new RandomLevelLoader(mockRandom, 1.0f, 10, 10); // 10 препятствий

        World world = loader.loadLevel(mockGrid);

        List<GridPoint2> obstaclePositions = world.getObstacles().stream()
                .map(Obstacle::getPosition)
                .toList();

        for (GridPoint2 pos : obstaclePositions) {
            assertThat(pos.x).isBetween(0, 4);
            assertThat(pos.y).isBetween(0, 4);
        }
    }
}