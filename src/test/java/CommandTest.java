import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.mipt.bit.platformer.logic.command.CommandContext;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.TileGrid;
import ru.mipt.bit.platformer.model.World;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

class CommandTest {
    private TileGrid mockGrid;
    private World mockWorld;
    private Player mockPlayer1;
    private Player mockPlayer2;
    private CommandContext context;

    @BeforeEach
    void setUp() {
        TiledMap level = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        mockGrid = new TileGrid(groundLayer);
        mockPlayer1 = new Player(new GridPoint2(1, 1), 1f);
        mockPlayer2 = new Player(new GridPoint2(3, 2), 1f);
        mockWorld = new World(mockPlayer1, List.of(), mockGrid, Set.of(mockPlayer2));
        context = new CommandContext(mockWorld);
    }

    @Test
    @DisplayName("Конструктор: Инициализирует занятые ячейки из мира")
    void constructor_InitializesReservedCells() {
        Set<GridPoint2> expectedReserved = Set.of(new GridPoint2(1, 1), new GridPoint2(3, 2));

        // Проверяем, что внутреннее состояние содержит правильные ячейки
        // Так как reservedCells приватное, мы косвенно проверяем через isFree
        assertThat(context.isFree(new GridPoint2(1, 1), Direction.UP)).isFalse(); // Ячейка игрока 1
        assertThat(context.isFree(new GridPoint2(3, 1), Direction.UP)).isFalse(); // Ячейка игрока 2
    }

    @Test
    @DisplayName("isFree: Возвращает false для ячейки, занятой танком")
    void isFree_ReturnsFalseForOccupiedCell() {
        GridPoint2 occupiedCell = new GridPoint2(1, 1);

        boolean isFree = context.isFree(occupiedCell.add(Direction.UP.getX(), Direction.UP.getY()), Direction.DOWN); // Проверяем ячейку (1,1) как кандидат

        // Проверим, что ячейка (1,1) изначально занята
        // isFree проверяет кандидата: fromPosition + direction
        // Если fromPosition = (1,2), direction = DOWN(0,-1), то кандидат = (1,1)
        // или если fromPosition = (1,0), direction = UP(0,1), то кандидат = (1,1)
        boolean isCellFree = context.isFree(new GridPoint2(1, 0), Direction.UP);

        assertThat(isCellFree).isFalse();
    }

    @Test
    @DisplayName("isFree: Возвращает true для свободной ячейки")
    void isFree_ReturnsTrueForFreeCell() {
        boolean isFree = context.isFree(new GridPoint2(0, 0), Direction.UP); // Кандидат (0,1) - свободен

        assertThat(isFree).isTrue();
    }

    @Test
    @DisplayName("isFree: Возвращает false для ячейки, занятой после резервации")
    void isFree_ReturnsFalseForReservedCell() {
        GridPoint2 cellToReserve = new GridPoint2(4, 4);
        context.reserveCell(cellToReserve);

        boolean isFree = context.isFree(cellToReserve.add(Direction.UP.getX(), Direction.UP.getY()), Direction.DOWN); // Проверяем (4,4)

        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("reserveCell: Добавляет ячейку в список занятых")
    void reserveCell_AddsToReserved() {
        GridPoint2 cell = new GridPoint2(2, 2);
        context.reserveCell(cell);

        boolean isFree = context.isFree(cell.add(Direction.UP.getX(), Direction.UP.getY()), Direction.DOWN); // Проверяем (2,2)

        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("reserveCell: Копирует GridPoint2 при добавлении")
    void reserveCell_CopiesGridPoint2() {
        GridPoint2 originalPoint = new GridPoint2(5, 5);
        context.reserveCell(originalPoint);

        // Изменим исходную точку
        originalPoint.set(99, 99);

        // Проверим, что в контексте осталась старая точка
        boolean isFree = context.isFree(new GridPoint2(4, 5), Direction.RIGHT); // Кандидат (5,5)

        assertThat(isFree).isFalse(); // Должно быть false, так как (5,5) была зарезервирована до изменения
    }
}