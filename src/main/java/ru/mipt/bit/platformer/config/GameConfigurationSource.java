package ru.mipt.bit.platformer.config;

import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.impl.FileLevelLoader;
import ru.mipt.bit.platformer.level.impl.RandomLevelLoader;

public enum GameConfigurationSource {
    RANDOM_LEVEL {
        @Override
        public LevelLoader createLevelLoader() {
            return new RandomLevelLoader();
        }
    },

    FILE_LEVEL {
        @Override
        public LevelLoader createLevelLoader() {
            return new FileLevelLoader("levels/1.txt");
        }
    };

    public abstract LevelLoader createLevelLoader();

    public static GameConfigurationSource getDefault() {
        return FILE_LEVEL;
    }
}
