package databaselib.select;



import databaselib.util.ConvertJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResultData {

    private final Map<String, Object> results;

    public ResultData(Map<String, Object> results) {
        this.results = results;
    }

    public List<String> getKeys() {
        return new ArrayList<>(results.keySet());
    }

    public String getString(String key) {
        Object res = results.get(key);
        if (res instanceof String v) {
            return v;
        }
        return null;
    }

    public int getInt(String key) {
        Object res = results.get(key);
        if (res instanceof Integer v) {
            return v;
        }
        return 0;
    }

    public long getLong(String key) {
        Object res = results.get(key);
        if (res instanceof Long l) {
            return l;
        }
        return 0;
    }

    public double getDouble(String key) {
        Object res = results.get(key);
        if (res instanceof Double v) {
            return v;
        }
        return 0;
    }

    public float getFloat(String key) {
        Object res = results.get(key);
        if (res instanceof Float v) {
            return v;
        }
        return 0;
    }

    public boolean getBoolean(String key) {
        Object res = results.get(key);
        if (res instanceof Boolean v) {
            return v;
        }
        return false;
    }

    public Date getDate(String key) {
        Object res = results.get(key);
        if (res instanceof Date v) {
            return v;
        }
        return null;
    }

    public <T> T getJsonStringToCustomClass(String key, Class<T> clazz) {
        Object res = results.get(key);
        if (res instanceof String v) {
            return new ConvertJson().jsonToClass(v, clazz);

        }
        return null;
    }

}
