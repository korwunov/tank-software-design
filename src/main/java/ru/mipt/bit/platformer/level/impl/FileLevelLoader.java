package ru.mipt.bit.platformer.level.impl;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLevelLoader implements LevelLoader {

    private static class LevelData {
        GridPoint2 playerPosition = null;
        List<Obstacle> obstacles = null;

        LevelData(GridPoint2 player, List<Obstacle> obstaclesList) {
            this.playerPosition = player;
            this.obstacles = obstaclesList;
        }
    }

    private final String filePath;
    private final float playerSpeed;

    public FileLevelLoader(String filePath) {
        this(filePath, 1f);
    }

    public FileLevelLoader(String filePath, float playerSpeed) {
        this.filePath = filePath;
        this.playerSpeed = playerSpeed;
    }

    @Override
    public World loadLevel(TileGrid tileGrid) throws IOException {
        List<String> lines = readLevelFile();
        LevelData levelData = getLevelDataFromFile(lines, tileGrid);
        Player player = new Player(levelData.playerPosition, playerSpeed);
        return new World(player, levelData.obstacles, tileGrid);
    }

    private LevelData getLevelDataFromFile(List<String> lines, TileGrid grid) {
        GridPoint2 gridSize = grid.getGridSize();
        int expectedHeight = gridSize.y;
        int expectedWidth = gridSize.x;

        if (lines.size() != expectedHeight) throw new RuntimeException("Level lines number doesn't match with grid height");
        List<Obstacle> obstacles = new ArrayList<>();
        GridPoint2 playerPosition = null;
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            if (line.length() != expectedWidth) throw new RuntimeException("Level line size doesn't match with grid width");

            for (int x = 0; x < line.length(); x++) {
                char type = line.charAt(x);
                switch (type) {
                    case 'T':
                        obstacles.add(new Obstacle(new GridPoint2(x, y), ObstacleType.TREE, grid));
                        break;
                    case 'X':
                        if (playerPosition != null) throw new RuntimeException("Multiple players not supported yet");
                        playerPosition = new GridPoint2(x, y);
                        break;
                    case '_':
                        break;
                    default:
                        throw new RuntimeException("Unrecognized char " + type + " at " + x + " " + y);
                }
            }
        }

        if (playerPosition == null) throw new RuntimeException("Player was not set, add char 'X' to level file");
        return new LevelData(playerPosition, obstacles);
    }

    private List<String> readLevelFile() throws IOException {
        List<String> lines = new ArrayList<>();

        if (filePath.startsWith("/") || filePath.contains(":")) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.trim());
                }
            }
        } else {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
                assert inputStream != null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line.trim());
                    }
                }
            }
        }

        if (lines.isEmpty()) {
            throw new IOException("Level file is empty: " + filePath);
        }

        return lines;
    }
}
