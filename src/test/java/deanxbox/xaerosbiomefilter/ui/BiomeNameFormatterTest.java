package deanxbox.xaerosbiomefilter.ui;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiomeNameFormatterTest {
    @Test
    void createsFriendlyFallbackNameFromBiomePath() {
        assertEquals("Old Growth Birch Forest",
                BiomeNameFormatter.fallbackName(Identifier.parse("minecraft:old_growth_birch_forest")));
    }
}
