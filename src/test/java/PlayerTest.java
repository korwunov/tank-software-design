import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Player;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private GridPoint2 position;

    @BeforeEach
    void beforeEachTest() {
        this.position = new GridPoint2(1, 2);
        this.player = new Player(this.position);
    }

    @Test
    void testDefaultPlayerCoordinates() {
        assertEquals(position, player.getPlayerCoordinates());
        assertEquals(position, player.getPlayerDestinationCoordinates());
        assertEquals(1.0f, player.getPlayerMovementProgress(), 0.001f);
        assertEquals(0.0f, player.getPlayerRotation(), 0.001f);
    }

    @Test
    void testPlayerRotation() {
        float newRotation = 45.5f;
        player.setPlayerRotation(newRotation);
        assertEquals(newRotation, player.getPlayerRotation(), 0.001f);
    }

    @Test
    void testPlayerStaticProgress() {
        float initialProgress = player.getPlayerMovementProgress();
        player.updateProgress(0.5f);
        assertEquals(initialProgress, player.getPlayerMovementProgress(), 0.001f);
        assertEquals(this.position, player.getPlayerCoordinates());
    }

    @Test
    void testPlayerMovingProgress() {
        GridPoint2 destination = new GridPoint2(3, 3);
        player.setPlayerDestinationCoordinates(destination);
        player.updateProgress(1.0f);
        assertEquals(1.0f, player.getPlayerMovementProgress(), 0.001f);
        assertEquals(destination, player.getPlayerCoordinates());
    }
}
