package deanxbox.xaerosbiomefilter.ui;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public final class BiomeNameFormatter {
    private BiomeNameFormatter() {
    }

    public static String displayName(Identifier biomeId) {
        String translationKey = biomeId.toLanguageKey("biome");
        if (Language.getInstance().has(translationKey)) {
            return Component.translatable(translationKey).getString();
        }
        return fallbackName(biomeId);
    }

    static String fallbackName(Identifier biomeId) {
        return Arrays.stream(biomeId.getPath().split("[_/.-]+"))
                .filter(part -> !part.isEmpty())
                .map(BiomeNameFormatter::capitalize)
                .collect(Collectors.joining(" "));
    }

    private static String capitalize(String value) {
        if (value.isEmpty()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1).toLowerCase(Locale.ROOT);
    }
}
