package deanxbox.xaerosbiomefilter.filter;

import deanxbox.xaerosbiomefilter.config.BiomeFilterConfig;
import deanxbox.xaerosbiomefilter.config.BiomeFilterMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiomeFilterTest {
    @Test
    void disabledFilterAllowsEveryBiome() {
        BiomeFilter filter = new BiomeFilter(new BiomeFilterConfig(false, SetFactory.of("minecraft:plains")));

        assertTrue(filter.allows("minecraft:desert"));
    }

    @Test
    void enabledFilterWithEmptySelectionAllowsEveryBiome() {
        BiomeFilter filter = new BiomeFilter(new BiomeFilterConfig(true, SetFactory.empty()));

        assertTrue(filter.allows("minecraft:desert"));
    }

    @Test
    void enabledFilterAllowsSelectedBiome() {
        BiomeFilter filter = new BiomeFilter(new BiomeFilterConfig(true, SetFactory.of("minecraft:plains")));

        assertTrue(filter.allows("minecraft:plains"));
    }

    @Test
    void enabledFilterRejectsUnselectedBiome() {
        BiomeFilter filter = new BiomeFilter(new BiomeFilterConfig(true, SetFactory.of("minecraft:plains")));

        assertFalse(filter.allows("minecraft:desert"));
    }

    @Test
    void hideSelectedModeRejectsSelectedBiome() {
        BiomeFilterConfig config = new BiomeFilterConfig(true, SetFactory.of("minecraft:plains"));
        config.setMode(BiomeFilterMode.HIDE_SELECTED);
        BiomeFilter filter = new BiomeFilter(config);

        assertFalse(filter.allows("minecraft:plains"));
    }

    @Test
    void hideSelectedModeAllowsUnselectedBiome() {
        BiomeFilterConfig config = new BiomeFilterConfig(true, SetFactory.of("minecraft:plains"));
        config.setMode(BiomeFilterMode.HIDE_SELECTED);
        BiomeFilter filter = new BiomeFilter(config);

        assertTrue(filter.allows("minecraft:desert"));
    }
}
