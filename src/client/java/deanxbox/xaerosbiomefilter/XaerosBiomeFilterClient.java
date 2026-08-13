package deanxbox.xaerosbiomefilter;

import deanxbox.xaerosbiomefilter.config.BiomeFilterConfig;
import deanxbox.xaerosbiomefilter.config.BiomeFilterConfigStore;
import deanxbox.xaerosbiomefilter.filter.BiomeFilter;
import deanxbox.xaerosbiomefilter.render.BiomeFilterRenderOverlay;
import deanxbox.xaerosbiomefilter.render.MinimapRefresh;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XaerosBiomeFilterClient implements ClientModInitializer {
    public static final String MOD_ID = "xaeros-biome-filter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static BiomeFilterConfigStore configStore;
    private static BiomeFilterConfig config = new BiomeFilterConfig();
    private static BiomeFilter filter = new BiomeFilter(config);
    private static int filterRevision;
    private static boolean initialized;

    @Override
    public void onInitializeClient() {
        configStore = new BiomeFilterConfigStore(FabricLoader.getInstance().getConfigDir());
        config = configStore.load();
        refreshFilter();
        initialized = true;
        LOGGER.info("Loaded Xaero's Biome Filter with {} selected biome(s)", config.getSelectedBiomes().size());
    }

    public static BiomeFilterConfig config() {
        return config;
    }

    public static BiomeFilter filter() {
        return filter;
    }

    public static int filterRevision() {
        return filterRevision;
    }

    public static void refreshFilter() {
        filter = new BiomeFilter(config);
        filterRevision++;
        BiomeFilterRenderOverlay.clearCache();
        if (initialized && FabricLoader.getInstance().isModLoaded("xaerominimap")) {
            MinimapRefresh.request();
        }
    }

    public static void saveConfig() {
        if (configStore != null) {
            configStore.save(config);
        }
        refreshFilter();
    }

}
