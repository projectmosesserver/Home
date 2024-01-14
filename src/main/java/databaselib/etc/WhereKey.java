package databaselib.etc;

import java.util.UUID;

public class WhereKey {

    private final String key;
    private final Object value;

    public WhereKey(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String convert() {
        return "where `" + key + "` = " + (value instanceof String || value instanceof UUID ? "'" + value + "'" : value);
    }
}
