package databaselib.table;

public class ColumnField {

    private String columnName;
    private ColumnType columnType;
    private Object defaultValue;
    private boolean isPrimaryKey = false;
    private boolean notNull = false;


    public ColumnField(String columnName, ColumnType columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public ColumnField key() {
        isPrimaryKey = true;
        return this;
    }

    public ColumnField notNull() {
        notNull = true;
        return this;
    }

    public ColumnField defaultValue(String value) {
        this.defaultValue = value;
        return this;
    }

    public ColumnField defaultValue(int value) {
        this.defaultValue = value;
        return this;
    }

    public ColumnField defaultValue(double value) {
        this.defaultValue = value;
        return this;
    }

    public ColumnField defaultValue(float value) {
        this.defaultValue = value;
        return this;
    }

    public String convert() {
        StringBuilder builder = new StringBuilder("`" + columnName + "`" + " " + columnType.getResult());
        if (notNull) {
            builder.append(" not null ");
        }
        if (defaultValue != null) {
            builder.append(" default ").append(defaultValue instanceof String ? "'" + defaultValue + "'" : defaultValue);
        }
        if (isPrimaryKey) {
            builder.append(" primary key");
        }
        return builder.toString();
    }

    public enum ColumnType {
        CHAR("char"),
        VAR_CHAR("varchar"),
        TINYINT("tinyint"),
        SMALLINT("smallint"),
        MEDIUMINT("mediumint"),
        INT("int"),
        BIGINT("bigint"),
        FLOAT("float"),
        DOUBLE("double"),
        DECIMAL("decimal"),
        NUMERIC("numeric"),
        DATE("date"),
        TIME("time"),
        DATETIME("datetime"),
        TIMESTAMP("timestamp"),
        YEAR("year"),
        TINYTEXT("tinytext"),
        TEXT("text"),
        MEDIUMTEXT("mediumtext"),
        LONGTEXT("longtext"),
        JSON("json");

        private String type;
        private String selectNum = "";
        private DecimalOption decimalOption;

        ColumnType(String type) {
            this.type = type;
        }

        public ColumnType num(int num) {
            this.selectNum = "(" + num + ")";
            return this;
        }

        public ColumnType decimal(int total, int scale) {
            this.decimalOption = new DecimalOption(total, scale);
            return this;
        }

        public ColumnType unsigned() {
            if (decimalOption == null) {
                return this;
            }
            decimalOption.unsigned();
            return this;
        }

        public ColumnType zerofill() {
            if (decimalOption == null) {
                return this;
            }
            decimalOption.zerofill();
            return this;
        }

        public String getResult() {
            StringBuilder builder = new StringBuilder(type);
            if (type.equalsIgnoreCase("decimal") && decimalOption != null) {
                builder.append("(").append(decimalOption.total).append(",").append(decimalOption.scale).append(")");
                if (decimalOption.unsigned) {
                    builder.append(" unsigned");
                }
                if (decimalOption.zerofill) {
                    builder.append(" zerofill");
                }
            } else {
                builder.append(selectNum);
            }
            return builder.toString();
        }

    }

    public static class DecimalOption {
        private final int total, scale;
        private boolean unsigned = false, zerofill = false;

        public DecimalOption(int total, int scale) {
            this.total = total;
            this.scale = scale;
        }

        public void unsigned() {
            this.unsigned = true;
        }

        public void zerofill() {
            this.zerofill = true;
        }
    }
}
