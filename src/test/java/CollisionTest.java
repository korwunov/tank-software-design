import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mipt.bit.platformer.logic.collision.CollisionDetector;
import ru.mipt.bit.platformer.logic.collision.impl.PlayerCollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.ObstacleType;
import ru.mipt.bit.platformer.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollisionTest {
    @Mock
    private PlayerCollisionDetector collisionDetector;

    @Test
    void testCanMove() {
        List<Obstacle> obstacles = List.of(
                new Obstacle(new GridPoint2(2, 3), ObstacleType.TREE)
        );

        assertTrue(collisionDetector.isMovePossible(new GridPoint2(1, 3), Direction.UP, obstacles));
        assertFalse(collisionDetector.isMovePossible(new GridPoint2(2, 3), Direction.DOWN, obstacles));

        verify(collisionDetector).isMovePossible(new GridPoint2(2, 1), Direction.UP, obstacles);
        verify(collisionDetector).isMovePossible(new GridPoint2(2, 3), Direction.DOWN, obstacles);
    }
}
