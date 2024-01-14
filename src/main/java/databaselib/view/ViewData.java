package databaselib.view;



import databaselib.select.SelectColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewData {

    private final String viewName;
    private List<Table> tables;
    private List<SelectColumn> selectColumns;

    public ViewData(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public ViewData addTable(Table table) {
        if (this.tables == null) {
            this.tables = new ArrayList<>();
        }
        this.tables.add(table);
        return this;
    }

    public ViewData addSelectColumn(SelectColumn column) {
        if (this.selectColumns == null) {
            this.selectColumns = new ArrayList<>();
        }
        selectColumns.add(column);
        return this;
    }

    public String convert() {
        StringBuilder builder = new StringBuilder("create view " + viewName + " as select ");
        AtomicInteger sel = new AtomicInteger(1);
        AtomicInteger tab = new AtomicInteger(1);
        if (selectColumns == null) {
            return null;
        }
        selectColumns.forEach(s -> {
            builder.append(s.convert());
            if (sel.get() != selectColumns.size()) {
                builder.append(", ");
                sel.getAndIncrement();
            }
        });
        if (tables == null) {
            return null;
        }
        builder.append(" from ");
        tables.forEach(t -> {
            builder.append(t.convert());
            if (tab.get() != tables.size()) {
                builder.append(", ");
                tab.getAndIncrement();
            }
        });
        return builder.toString();
    }

    public static class Table {
        private final String key;
        private final String table;

        public Table(String key, String table) {
            this.key = key;
            this.table = table;
        }

        public Table(String table) {
            this.key = null;
            this.table = table;
        }

        public String convert() {
            return key == null ? table : table + " " + key;
        }

        public String getKey() {
            return key;
        }
    }

}
