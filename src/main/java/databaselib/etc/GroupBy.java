package databaselib.etc;

public class GroupBy {

    private final String[] columns;
    private Having having;

    public GroupBy(String... columns) {
        this.columns = columns;
    }

    public GroupBy(Having having, String... columns) {
        this.having = having;
        this.columns = columns;
    }

    public String convert() {
        StringBuilder builder = new StringBuilder("group by ");
        int i = 0;
        int length = columns.length;
        for (String s : columns) {
            if (!s.contains(".")) {
                builder.append("`").append(s).append("` ");
            } else {
                builder.append(s);
            }
            i++;
            if (i + 1 < length) {
                builder.append(",");
            }
        }
        if (having != null) {
            builder.append(having.convert());
        }
        return builder.toString();
    }

    public static class Having {
        private final String terms;

        public Having(String terms) {
            this.terms = terms;
        }

        public String convert() {
            return "having " + terms;
        }
    }
}
