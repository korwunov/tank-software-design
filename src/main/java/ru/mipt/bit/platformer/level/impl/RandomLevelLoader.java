package ru.mipt.bit.platformer.level.impl;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLevelLoader implements LevelLoader {
    private final Random random;
    private final float playerMovementSpeed;
    private final int minObstacles;
    private final int maxObstacles;

    public RandomLevelLoader() {
        this(new Random(), 0.4f, 3, 8);
    }

    public RandomLevelLoader(Random random, float playerMovementSpeed, int minObstacles, int maxObstacles) {
        this.random = random;
        this.playerMovementSpeed = playerMovementSpeed;
        this.minObstacles = minObstacles;
        this.maxObstacles = maxObstacles;
    }

    @Override
    public World loadLevel(TileGrid tileGrid) {
        GridPoint2 gridSize = tileGrid.getGridSize();

        int obstacleCount = minObstacles + random.nextInt(maxObstacles - minObstacles + 1);

        List<GridPoint2> obstaclePositions = generateRandomPositions(
                gridSize, obstacleCount, List.of()
        );

        List<Obstacle> obstacles = new ArrayList<>();
        for (GridPoint2 position : obstaclePositions) {
            obstacles.add(new Obstacle(position, ObstacleType.TREE, tileGrid));
        }

        List<GridPoint2> playerPositions = generateRandomPositions(
                gridSize, 1, obstaclePositions
        );
        GridPoint2 playerPosition = playerPositions.iterator().next();

        Player player = new Player(playerPosition, playerMovementSpeed);

        return new World(player, obstacles, tileGrid);
    }

    private List<GridPoint2> generateRandomPositions(GridPoint2 gridSize, int count, List<GridPoint2> excludePositions) {
        List<GridPoint2> positions = new ArrayList<>();
        int maxAttempts = gridSize.x * gridSize.y * 2;
        int attempts = 0;

        while (positions.size() < count && attempts < maxAttempts) {
            GridPoint2 position = new GridPoint2(random.nextInt(gridSize.x), random.nextInt(gridSize.y));

            if (!excludePositions.contains(position) && !positions.contains(position)) {
                positions.add(position);
            }
            attempts++;
        }

        return positions;
    }
}
