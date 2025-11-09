import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.ObstacleType;
import ru.mipt.bit.platformer.model.TileGrid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class ObstacleTest {
    private TileGrid mockGrid;

    @BeforeEach
    void setUp() {
        TiledMap level = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        mockGrid = new TileGrid(groundLayer);
    }

    @Test
    void testCreateObstacle() {
        GridPoint2 position = new GridPoint2(2, 3);
        Obstacle obstacle = new Obstacle(position, ObstacleType.TREE, mockGrid);
        assertEquals(position, obstacle.getPosition());
        assertEquals(ObstacleType.TREE, obstacle.getType());
    }

    @Test
    void testGetPosition() {
        GridPoint2 originalPosition = new GridPoint2(8, 2);
        Obstacle obstacle = new Obstacle(originalPosition, ObstacleType.TREE, mockGrid);
        assertEquals(originalPosition, obstacle.getPosition());
    }

    @Test
    void testObstaclesEquals() {
        Obstacle obstacle1 = new Obstacle(new GridPoint2(2, 3), ObstacleType.TREE, mockGrid);
        Obstacle obstacle2 = new Obstacle(new GridPoint2(2, 3), ObstacleType.TREE, mockGrid);
        Obstacle obstacle3 = new Obstacle(new GridPoint2(4, 5), ObstacleType.TREE, mockGrid);
        Obstacle obstacle4 = new Obstacle(new GridPoint2(4, 5), ObstacleType.WALL, mockGrid);

        assertEquals(obstacle1, obstacle2);
        assertNotEquals(obstacle1, obstacle3);
        assertNotEquals(obstacle1, obstacle4);
        assertNotEquals(obstacle1, null);
        assertNotEquals(obstacle1, "string");
    }
}
