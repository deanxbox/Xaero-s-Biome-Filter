package deanxbox.xaerosbiomefilter.filter;

import java.util.LinkedHashSet;
import java.util.Set;

final class SetFactory {
    private SetFactory() {
    }

    static Set<String> of(String... values) {
        Set<String> result = new LinkedHashSet<>();
        for (String value : values) {
            result.add(value);
        }
        return result;
    }

    static Set<String> empty() {
        return new LinkedHashSet<>();
    }
}
