package deanxbox.xaerosbiomefilter.config;

import deanxbox.xaerosbiomefilter.XaerosBiomeFilterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BiomeFilterConfigStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(XaerosBiomeFilterClient.MOD_ID);
    private static final Pattern ENABLED_PATTERN = Pattern.compile("\\\"enabled\\\"\\s*:\\s*true");
    private static final Pattern MODE_PATTERN = Pattern.compile("\\\"mode\\\"\\s*:\\s*\\\"(SHOW_SELECTED|HIDE_SELECTED)\\\"");
    private static final Pattern SELECTED_ARRAY_PATTERN = Pattern.compile("\\\"selectedBiomes\\\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
    private static final Pattern STRING_PATTERN = Pattern.compile("\\\"((?:\\\\.|[^\\\"])*)\\\"");

    private final Path configPath;

    public BiomeFilterConfigStore(Path configDir) {
        this.configPath = configDir.resolve("xaeros-biome-filter.json");
    }

    public BiomeFilterConfig load() {
        if (!Files.exists(configPath)) {
            return new BiomeFilterConfig();
        }
        try {
            String json = Files.readString(configPath, StandardCharsets.UTF_8);
            boolean enabled = ENABLED_PATTERN.matcher(json).find();
            Matcher modeMatcher = MODE_PATTERN.matcher(json);
            BiomeFilterMode mode = modeMatcher.find()
                    ? BiomeFilterMode.valueOf(modeMatcher.group(1))
                    : BiomeFilterMode.SHOW_SELECTED;
            Set<String> selected = new LinkedHashSet<>();
            Matcher arrayMatcher = SELECTED_ARRAY_PATTERN.matcher(json);
            if (arrayMatcher.find()) {
                Matcher stringMatcher = STRING_PATTERN.matcher(arrayMatcher.group(1));
                while (stringMatcher.find()) {
                    selected.add(unescapeJsonString(stringMatcher.group(1)));
                }
            }
            return new BiomeFilterConfig(enabled, mode, selected);
        } catch (IOException exception) {
            LOGGER.warn("Failed to load biome filter config from {}", configPath, exception);
            return new BiomeFilterConfig();
        }
    }

    public void save(BiomeFilterConfig config) {
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, toJson(config), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            LOGGER.warn("Failed to save biome filter config to {}", configPath, exception);
        }
    }

    private static String toJson(BiomeFilterConfig config) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"enabled\": ").append(config.isEnabled()).append(",\n");
        builder.append("  \"mode\": \"").append(config.getMode()).append("\",\n");
        builder.append("  \"selectedBiomes\": [");
        boolean first = true;
        for (String biomeId : config.getSelectedBiomes()) {
            if (!first) {
                builder.append(", ");
            }
            builder.append('"').append(escapeJsonString(biomeId)).append('"');
            first = false;
        }
        builder.append("]\n");
        builder.append("}\n");
        return builder.toString();
    }

    private static String escapeJsonString(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescapeJsonString(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
