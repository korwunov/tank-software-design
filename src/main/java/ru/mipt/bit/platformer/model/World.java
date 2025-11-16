package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic.observer.Observer;

import java.util.*;
import java.util.stream.Collectors;

public class World {
    private final Player player;
    private final List<Obstacle> obstacles;
    private final TileGrid tileGrid;
    private final Set<Player> botTanks;
    private final Set<Bullet> bullets;
    private final List<Observer> observers;
    private boolean healthStatsVisible;

    public World(Player player, List<Obstacle> obstacles, TileGrid tileGrid) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.botTanks = new HashSet<>();
        this.bullets = new HashSet<>();
        this.observers = new ArrayList<>();
        this.healthStatsVisible = false;
    }

    public World(Player player, List<Obstacle> obstacles, TileGrid tileGrid, Set<Player> botTanks) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.botTanks = botTanks;
        this.bullets = new HashSet<>();
        this.observers = new ArrayList<>();
        this.healthStatsVisible = false;
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

    public boolean isHealthStatsVisible() {
        return healthStatsVisible;
    }

    public void toggleHealthStats() {
        healthStatsVisible = !healthStatsVisible;
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
        notifyInitObjects(observer);
    }

    public Set<Bullet> getBullets() {
        return bullets;
    }

    public void addBullet(Bullet b) {
        bullets.add(b);
        notifyAddedObject(b);
    }

    public void removeBullet(Bullet b) {
        bullets.remove(b);
        notifyRemovedObject(b);
    }

    public Player getTankAt(GridPoint2 position) {
        if (player.getPlayerCoordinates().equals(position) && player.isAlive()) {
            return player;
        }
        for (Player tank : botTanks) {
            if (tank.getPlayerCoordinates().equals(position) && tank.isAlive()) {
                return tank;
            }
        }
        return null;
    }

    private void notifyInitObjects(Observer observer) {
        observer.onObjectAdded(player);
        for (Obstacle o : obstacles) observer.onObjectAdded(o);
        for (Player bot : botTanks) observer.onObjectAdded(bot);
        for (Bullet b : bullets) observer.onObjectAdded(b);
    }

    private void notifyAddedObject(Object obj) {
        for (Observer o : observers) o.onObjectAdded(obj);
    }

    private void notifyRemovedObject(Object obj) {
        for (Observer o : observers) o.onObjectRemoved(obj);
    }
}
