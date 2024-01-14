package databaselib.select;


public class SelectData {

    private final String table;
    private final SelectColumn[] columns;

    public SelectData(String table, SelectColumn... columns) {
        this.table = table;
        this.columns = columns;
    }

    public String convert() {
        StringBuilder builder = new StringBuilder("select ");
        int i = 0;
        int length = columns.length;
        for (SelectColumn column : columns) {
            builder.append(column.convert());
            i++;
            if (i < length) {
                builder.append(" , ");
            }
        }
        builder.append(" from ").append(table);
        return builder.toString();
    }
}
