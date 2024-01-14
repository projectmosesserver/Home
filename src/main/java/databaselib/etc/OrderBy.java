package databaselib.etc;

public class OrderBy {

    private final String column;
    private final SortType sortType;

    public OrderBy(String column, SortType sortType) {
        this.column = column;
        this.sortType = sortType;
    }

    public String convert() {
        return column.contains(".") ? "order by " + column + " " + sortType.sort : "order by `" + column + "` " + sortType.sort;
    }

    public enum SortType {
        DESC("desc"),
        ASC("asc");

        private final String sort;

        SortType(String sort) {
            this.sort = sort;
        }

        public String getSort() {
            return sort;
        }
    }
}
