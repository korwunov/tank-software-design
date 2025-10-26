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
import ru.mipt.bit.platformer.config.GameConfigurationSource;
import ru.mipt.bit.platformer.graphics.DrawableUpdater;
import ru.mipt.bit.platformer.graphics.GraphicsObject;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.impl.KeyboardInputController;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.logic.collision.impl.PlayerCollisionDetector;
import ru.mipt.bit.platformer.logic.movements.MovementsProcessor;
import ru.mipt.bit.platformer.logic.movements.impl.PlayerMovementsProcessor;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.util.TileMovement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GameApplication implements ApplicationListener {
    private Batch batch;

    private InputController input;

    private MovementsProcessor movementsProcessor;

    private DrawableUpdater drawableUpdater;

    private TiledMap level;
    private World world;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private LevelLoader levelLoader;

    private List<GraphicsObject> objectsToUpdateWhileRender = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();

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
        levelLoader = GameConfigurationSource.getDefault().createLevelLoader();
        //dependency injection for movementProcessor
        CollisionDetector collisionDetector = new PlayerCollisionDetector();

        movementsProcessor = new PlayerMovementsProcessor(collisionDetector);

        Obstacle tree = new Obstacle(new GridPoint2(1, 3), ObstacleType.TREE, world.getTileGrid());
        obstacles.add(tree);

//        world = new World(new Player(new GridPoint2(1, 1), 1f), obstacles, tileGrid);
        try {
            world = levelLoader.loadLevel(tileGrid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        objectsToUpdateWhileRender.add(world.getPlayer());
        objectsToUpdateWhileRender.addAll(world.getObstacles());
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        input.move().ifPresent(direction -> movementsProcessor.processMoveCommand(world.getPlayer(), direction, world.getObstacles()));

        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(world.getPlayer().getRectangle(), world.getPlayer().getPlayerCoordinates(), world.getPlayer().getPlayerDestinationCoordinates(), world.getPlayer().getPlayerMovementProgress());

        // render each tile of the level
        levelRenderer.render();

        drawableUpdater.update(this.objectsToUpdateWhileRender);
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
        world.getObstacles().forEach(o -> o.getTexture().dispose());
        world.getPlayer().getTexture().dispose();
        level.dispose();
        batch.dispose();
    }
}
