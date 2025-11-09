import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import ru.mipt.bit.platformer.level.impl.FileLevelLoader;
import ru.mipt.bit.platformer.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

class FileLevelLoaderTest {

    @TempDir
    Path tempDir;

    private TileGrid mockGrid;
    private Path validLevelFile;
    private Path invalidSizeFile;
    private Path noPlayerFile;
    private Path multiplePlayerFile;
    private Path unrecognizedCharFile;
    private Path emptyFile;

    @BeforeEach
    void setUp() throws IOException {
        TiledMap level = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        mockGrid = new TileGrid(groundLayer);

        validLevelFile = tempDir.resolve("valid_level.txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(validLevelFile))) {
            writer.println("_____");
            writer.println("_T_X_");
            writer.println("_____");
        }

        invalidSizeFile = tempDir.resolve("invalid_size.txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(invalidSizeFile))) {
            writer.println("_____");
            writer.println("_T_X_");
        }

        noPlayerFile = tempDir.resolve("no_player.txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(noPlayerFile))) {
            writer.println("_____");
            writer.println("_T__");
            writer.println("_____");
        }

        multiplePlayerFile = tempDir.resolve("multiple_player.txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(multiplePlayerFile))) {
            writer.println("X____");
            writer.println("_T_X_");
            writer.println("_____");
        }

        unrecognizedCharFile = tempDir.resolve("unrecognized_char.txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(unrecognizedCharFile))) {
            writer.println("_____");
            writer.println("_T_P_");
            writer.println("_____");
        }

        emptyFile = tempDir.resolve("empty.txt");
    }

    @Test
    @DisplayName("loadLevel: Успешная загрузка уровня из файла")
    void loadLevel_LoadsSuccessfully() throws IOException {
        FileLevelLoader loader = new FileLevelLoader(validLevelFile.toString());

        World world = loader.loadLevel(mockGrid);

        assertThat(world.getPlayer().getPlayerCoordinates()).isEqualTo(new GridPoint2(3, 1));
        assertThat(world.getObstacles()).hasSize(1);
        assertThat(world.getObstacles().get(0).getPosition()).isEqualTo(new GridPoint2(1, 1));
        assertThat(world.getObstacles().get(0).getType()).isEqualTo(ObstacleType.TREE);
    }

    @Test
    @DisplayName("loadLevel: Выбрасывает RuntimeException если высота файла не совпадает с сеткой")
    void loadLevel_ThrowsOnHeightMismatch() {
        FileLevelLoader loader = new FileLevelLoader(invalidSizeFile.toString());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> loader.loadLevel(mockGrid))
                .withMessage("Level lines number doesn't match with grid height");
    }

    @Test
    @DisplayName("loadLevel: Выбрасывает RuntimeException если игрок не найден")
    void loadLevel_ThrowsIfNoPlayer() {
        FileLevelLoader loader = new FileLevelLoader(noPlayerFile.toString());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> loader.loadLevel(mockGrid))
                .withMessage("Player was not set, add char 'X' to level file");
    }

    @Test
    @DisplayName("loadLevel: Выбрасывает RuntimeException если несколько игроков")
    void loadLevel_ThrowsIfMultiplePlayers() {
        FileLevelLoader loader = new FileLevelLoader(multiplePlayerFile.toString());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> loader.loadLevel(mockGrid))
                .withMessage("Multiple players not supported yet");
    }

    @Test
    @DisplayName("loadLevel: Выбрасывает RuntimeException если неизвестный символ")
    void loadLevel_ThrowsOnUnrecognizedChar() {
        FileLevelLoader loader = new FileLevelLoader(unrecognizedCharFile.toString());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> loader.loadLevel(mockGrid))
                .withMessage("Unrecognized char P at 2 1");
    }

    @Test
    @DisplayName("loadLevel: Выбрасывает IOException если файл пустой")
    void loadLevel_ThrowsIfFileEmpty() {
        FileLevelLoader loader = new FileLevelLoader(emptyFile.toString());

        assertThatExceptionOfType(IOException.class)
                .isThrownBy(() -> loader.loadLevel(mockGrid))
                .withMessage("Level file is empty: " + emptyFile.toString());
    }

    @Test
    @DisplayName("loadLevel: Использует заданную скорость игрока")
    void loadLevel_UsesGivenPlayerSpeed() throws IOException {
        float customSpeed = 2.5f;
        FileLevelLoader loader = new FileLevelLoader(validLevelFile.toString(), customSpeed);

        World world = loader.loadLevel(mockGrid);

        assertThat(world.getPlayer().getPlayerMovementProgress()).isEqualTo(customSpeed);
    }
}