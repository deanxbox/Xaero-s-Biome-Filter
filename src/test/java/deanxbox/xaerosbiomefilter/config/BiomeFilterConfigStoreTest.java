package deanxbox.xaerosbiomefilter.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiomeFilterConfigStoreTest {
    @Test
    void savesAndLoadsHideSelectedMode(@TempDir Path configDir) {
        BiomeFilterConfigStore store = new BiomeFilterConfigStore(configDir);
        BiomeFilterConfig config = new BiomeFilterConfig();
        config.setMode(BiomeFilterMode.HIDE_SELECTED);

        store.save(config);

        assertEquals(BiomeFilterMode.HIDE_SELECTED, store.load().getMode());
    }
}
