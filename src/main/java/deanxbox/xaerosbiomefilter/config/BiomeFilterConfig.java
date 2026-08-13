package deanxbox.xaerosbiomefilter.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class BiomeFilterConfig {
    private boolean enabled;
    private BiomeFilterMode mode;
    private final Set<String> selectedBiomes;

    public BiomeFilterConfig() {
        this(false, BiomeFilterMode.SHOW_SELECTED, Collections.emptySet());
    }

    public BiomeFilterConfig(boolean enabled, Set<String> selectedBiomes) {
        this(enabled, BiomeFilterMode.SHOW_SELECTED, selectedBiomes);
    }

    public BiomeFilterConfig(boolean enabled, BiomeFilterMode mode, Set<String> selectedBiomes) {
        this.enabled = enabled;
        this.mode = mode == null ? BiomeFilterMode.SHOW_SELECTED : mode;
        this.selectedBiomes = new LinkedHashSet<>();
        if (selectedBiomes != null) {
            selectedBiomes.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(value -> !value.isEmpty())
                    .forEach(this.selectedBiomes::add);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public BiomeFilterMode getMode() {
        return mode;
    }

    public void setMode(BiomeFilterMode mode) {
        this.mode = mode == null ? BiomeFilterMode.SHOW_SELECTED : mode;
    }

    public Set<String> getSelectedBiomes() {
        return Collections.unmodifiableSet(selectedBiomes);
    }

    public boolean isSelected(String biomeId) {
        return selectedBiomes.contains(biomeId);
    }

    public void setSelected(String biomeId, boolean selected) {
        if (biomeId == null || biomeId.isBlank()) {
            return;
        }
        if (selected) {
            selectedBiomes.add(biomeId.trim());
        } else {
            selectedBiomes.remove(biomeId.trim());
        }
    }

    public void clearSelectedBiomes() {
        selectedBiomes.clear();
    }
}
