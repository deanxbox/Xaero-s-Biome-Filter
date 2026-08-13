package deanxbox.xaerosbiomefilter.filter;

import deanxbox.xaerosbiomefilter.config.BiomeFilterConfig;
import deanxbox.xaerosbiomefilter.config.BiomeFilterMode;

public final class BiomeFilter {
    private final BiomeFilterConfig config;

    public BiomeFilter(BiomeFilterConfig config) {
        this.config = config;
    }

    public boolean allows(String biomeId) {
        if (!config.isEnabled()) {
            return true;
        }
        if (config.getSelectedBiomes().isEmpty()) {
            return true;
        }
        boolean selected = config.isSelected(biomeId);
        return config.getMode() == BiomeFilterMode.SHOW_SELECTED
                ? selected
                : !selected;
    }
}
