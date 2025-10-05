import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.mipt.bit.platformer.model.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {
    @ParameterizedTest
    @EnumSource(Direction.class)
    void testDirectionValues(Direction direction) {
        switch (direction) {
            case UP -> {
                assertEquals(0, direction.getX());
                assertEquals(1, direction.getY());
                assertEquals(90f, direction.getRotation(), 0.001f);
            }
            case LEFT -> {
                assertEquals(-1, direction.getX());
                assertEquals(0, direction.getY());
                assertEquals(-180f, direction.getRotation(), 0.001f);
            }
            case DOWN -> {
                assertEquals(0, direction.getX());
                assertEquals(-1, direction.getY());
                assertEquals(-90f, direction.getRotation(), 0.001f);
            }
            case RIGHT -> {
                assertEquals(1, direction.getX());
                assertEquals(0, direction.getY());
                assertEquals(0f, direction.getRotation(), 0.001f);
            }
        }
    }
}
