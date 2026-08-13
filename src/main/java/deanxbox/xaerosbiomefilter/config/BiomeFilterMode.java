package deanxbox.xaerosbiomefilter.config;

public enum BiomeFilterMode {
    SHOW_SELECTED,
    HIDE_SELECTED;

    public BiomeFilterMode next() {
        return this == SHOW_SELECTED ? HIDE_SELECTED : SHOW_SELECTED;
    }
}
