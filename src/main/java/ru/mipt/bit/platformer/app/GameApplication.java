package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.render.impl.BulletRenderer;
import ru.mipt.bit.platformer.graphics.render.impl.HealthStatRenderer;
import ru.mipt.bit.platformer.logic.command.Command;
import ru.mipt.bit.platformer.logic.command.CommandContext;
import ru.mipt.bit.platformer.logic.command.impl.MoveCommand;
import ru.mipt.bit.platformer.level.config.GameConfigurationSource;
import ru.mipt.bit.platformer.graphics.DrawableUpdater;
import ru.mipt.bit.platformer.graphics.GraphicsObject;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.impl.KeyboardInputController;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.logic.collision.impl.PlayerCollisionDetector;
import ru.mipt.bit.platformer.logic.command.impl.ShootCommand;
import ru.mipt.bit.platformer.logic.command.impl.ToggleHealthStatCommand;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.util.TileMovement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.random;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameApplication implements ApplicationListener {
    private Batch batch;
    private InputController input;
    private DrawableUpdater drawableUpdater;
    private HealthStatRenderer healthStatRenderer;
    private BulletRenderer bulletRenderer;

    private TiledMap level;
    private World world;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private CollisionDetector collisionDetector;

    private final List<GraphicsObject> objectsToUpdateWhileRender = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        input = new KeyboardInputController();
        drawableUpdater = new DrawableUpdater(this.batch);


        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        TileGrid tileGrid = new TileGrid(groundLayer);
        LevelLoader levelLoader = GameConfigurationSource.getDefault().createLevelLoader();
        //dependency injection for movementProcessor
        collisionDetector = new PlayerCollisionDetector();

        try {
            world = levelLoader.loadLevel(tileGrid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        spawnBots(tileGrid);
        objectsToUpdateWhileRender.addAll(world.getAllTanks());
        objectsToUpdateWhileRender.addAll(world.getObstacles());
        healthStatRenderer = new HealthStatRenderer(world.isHealthStatsVisible());
        bulletRenderer = new BulletRenderer(16f);
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        List<Command> commands = getCommands();
        CommandContext context = new CommandContext(world);
        for (Command command : commands) {
            command.execute(world, context);
        }

        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(world.getPlayer().getRectangle(), world.getPlayer().getPlayerCoordinates(), world.getPlayer().getPlayerDestinationCoordinates(), world.getPlayer().getPlayerMovementProgress());

        // render each tile of the level
        batch.begin();
        levelRenderer.render();
        healthStatRenderer.setHealthStatsVisible(world.isHealthStatsVisible());
        for (Bullet b : world.getBullets()) bulletRenderer.render(b, batch, tileMovement);
        for (Player p : world.getAllTanks()) healthStatRenderer.render(p, batch, tileMovement);

        drawableUpdater.update(this.objectsToUpdateWhileRender, batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        healthStatRenderer.dispose();
        bulletRenderer.dispose();
        world.getObstacles().forEach(o -> o.getTexture().dispose());
        world.getPlayer().getTexture().dispose();
        level.dispose();
        batch.dispose();
    }

    private void spawnBots(TileGrid tileGrid) {
        int botsToCreate = 3;
        float botSpeed = world.getPlayer().getPlayerMovementProgress();
        for (int i = 0; i < botsToCreate; i++) {
            GridPoint2 pos = findFreeCell(tileGrid);
            if (pos == null) break;
            world.getBotTanks().add(new Player(pos, botSpeed));
        }
    }

    private GridPoint2 findFreeCell(TileGrid grid) {
        GridPoint2 size = grid.getGridSize();
        for (int attempts = 0; attempts < size.x * size.y * 2; attempts++) {
            int x = random.nextInt(size.x);
            int y = random.nextInt(size.y);
            GridPoint2 p = new GridPoint2(x, y);
            if (!grid.isValidPosition(p)) continue;
            if (world.hasObstacleAt(p)) continue;
            if (world.getPlayer().getPlayerCoordinates().equals(p)) continue;
            boolean occupied = false;
            for (Player bot : world.getBotTanks()) {
                if (bot.getPlayerCoordinates().equals(p)) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) return p;
        }
        return null;
    }

    private List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        for (var event : input.poll()) {
            switch (event.getAction()) {
                case MOVE -> event.getDirection().ifPresent(direction -> commands.add(new MoveCommand(world.getPlayer(), direction, this.collisionDetector)));
                case TOGGLE_HEALTH -> commands.add(new ToggleHealthStatCommand());
                case SHOOT -> commands.add(new ShootCommand(world.getPlayer()));
                default -> throw new RuntimeException("Unrecognized command");
            }
        }

        for (Player bot : world.getBotTanks()) {
            if (bot.isAlive()) {
                if (random.nextFloat() > 0.5f) {
                    commands.add(new MoveCommand(bot, Direction.getRandomDirection(), collisionDetector));
                } else {
                    commands.add(new ShootCommand(bot));
                }
            }

        }

        return commands;
    }
}