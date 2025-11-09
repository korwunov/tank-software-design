package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class World {
    private final Player player;
    private final List<Obstacle> obstacles;
    private final TileGrid tileGrid;
    private final Set<Player> botTanks;

    public World(Player player, List<Obstacle> obstacles, TileGrid tileGrid) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.botTanks = new HashSet<>();
    }

    public World(Player player, List<Obstacle> obstacles, TileGrid tileGrid, Set<Player> botTanks) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.botTanks = botTanks;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Player> getBotTanks() {
        return botTanks;
    }

    public List<Player> getAllTanks() {
        List<Player> players = new ArrayList<>(botTanks);
        players.add(player);
        return players;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Set<GridPoint2> getObstaclePositions() {
        return obstacles.stream()
                .map(Obstacle::getPosition)
                .collect(Collectors.toSet());
    }

    public TileGrid getTileGrid() {
        return tileGrid;
    }

    public boolean hasObstacleAt(GridPoint2 position) {
        return obstacles.stream()
                .anyMatch(obstacle -> obstacle.getPosition().equals(position));
    }

    public Obstacle getObstacleAt(GridPoint2 position) {
        return obstacles.stream()
                .filter(obstacle -> obstacle.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }
}
