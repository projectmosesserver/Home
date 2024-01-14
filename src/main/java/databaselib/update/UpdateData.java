package databaselib.update;

public class UpdateData {

    private final String column;
    private final Object value;

    public UpdateData(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String convert() {
        return "`" + column + "` = " + (value instanceof String ? "'" + value + "'" : value);
    }
}
