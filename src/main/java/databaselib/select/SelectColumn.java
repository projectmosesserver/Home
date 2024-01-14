package databaselib.select;

public class SelectColumn {

    private String column;
    private String asName;
    private boolean all = false;
    private boolean count = false;
    private boolean notQuotation = false;

    public SelectColumn(String column) {
        this.column = column;
    }

    public SelectColumn() {
    }

    public SelectColumn as(String as) {
        this.asName = as;
        return this;
    }

    public SelectColumn all() {
        this.all = true;
        return this;
    }

    public SelectColumn count() {
        this.count = true;
        return this;
    }

    public SelectColumn notQuotation() {
        this.notQuotation = true;
        return this;
    }

    public String convert() {
        StringBuilder builder = new StringBuilder();
        if (all) {
            this.column = "*";
        }
        if (count) {
            builder.append("count(").append(column).append(")");
            if (asName != null) {
                builder.append(" as ").append("`").append(asName).append("`");
            }
            return builder.toString();
        }
        if (!notQuotation) {
            builder.append("`").append(column).append("`");
        } else {
            builder.append(column);
        }
        if (asName != null && !all) {
            builder.append(" as ").append("`").append(asName).append("`");
        }
        return builder.toString();
    }
}
