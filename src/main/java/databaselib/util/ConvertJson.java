package databaselib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConvertJson {

    private Gson gson;
    private boolean includeNull = false;
    private boolean prettyPrinting = false;

    public ConvertJson() {
    }

    public String objectToJson(Object object) {
        if (this.gson == null) {
            GsonBuilder builder = new GsonBuilder();
            if (includeNull) {
                builder.serializeNulls();
            }
            if (prettyPrinting) {
                builder.setPrettyPrinting();
            }
            this.gson = builder.create();
        }
        return gson.toJson(object);
    }

    public <T> T jsonToClass(String json, Class<T> clazz) {
        if (this.gson == null) {
            GsonBuilder builder = new GsonBuilder();
            if (includeNull) {
                builder.serializeNulls();
            }
            if (prettyPrinting) {
                builder.setPrettyPrinting();
            }
            this.gson = builder.create();
        }
        return gson.fromJson(json, clazz);
    }

    public ConvertJson includeNull() {
        this.includeNull = true;
        return this;
    }

    public ConvertJson prettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }

}
