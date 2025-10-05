import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.ObstacleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ObstacleTest {
    @Test
    void testCreateObstacle() {
        GridPoint2 position = new GridPoint2(2, 3);
        Obstacle obstacle = new Obstacle(position, ObstacleType.TREE);
        assertEquals(position, obstacle.getPosition());
        assertEquals(ObstacleType.TREE, obstacle.getType());
    }

    @Test
    void testGetPosition() {
        GridPoint2 originalPosition = new GridPoint2(8, 2);
        Obstacle obstacle = new Obstacle(originalPosition, ObstacleType.TREE);
        assertEquals(originalPosition, obstacle.getPosition());
    }

    @Test
    void testObstaclesEquals() {
        Obstacle obstacle1 = new Obstacle(new GridPoint2(2, 3), ObstacleType.TREE);
        Obstacle obstacle2 = new Obstacle(new GridPoint2(2, 3), ObstacleType.TREE);
        Obstacle obstacle3 = new Obstacle(new GridPoint2(4, 5), ObstacleType.TREE);
        Obstacle obstacle4 = new Obstacle(new GridPoint2(4, 5), ObstacleType.WALL);

        assertEquals(obstacle1, obstacle2);
        assertNotEquals(obstacle1, obstacle3);
        assertNotEquals(obstacle1, obstacle4);
        assertNotEquals(obstacle1, null);
        assertNotEquals(obstacle1, "string");
    }
}
