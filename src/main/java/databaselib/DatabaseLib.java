package databaselib;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import databaselib.etc.GroupBy;
import databaselib.etc.OrderBy;
import databaselib.etc.WhereKey;
import databaselib.insert.ColumnData;
import databaselib.select.ResultData;
import databaselib.select.SelectData;
import databaselib.table.ColumnField;
import databaselib.update.UpdateData;
import databaselib.view.ViewData;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseLib {
    private final String user, pass, url;
    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;

    public DatabaseLib(String url) {
        this.url = url;
        this.user = null;
        this.pass = null;
        init();
    }

    public DatabaseLib(String url, String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;
        init();
    }

    private void init() {
        hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        if (user != null && pass != null) {
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(pass);
        }
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        hikariConfig.addDataSourceProperty("useLocalSessionState", true);
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", true);
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", true);
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", true);
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", true);
        hikariConfig.addDataSourceProperty("maintainTimeStats", false);
        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void createTable(String tableName, ColumnField... columns) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            StringBuilder builder = new StringBuilder("create table if not exists `" + tableName + "`(");
            int i = 0;
            for (ColumnField field : columns) {
                i++;
                builder.append(field.convert());
                if (columns.length > i) {
                    builder.append(", ");
                }
            }
            builder.append(");");
            PreparedStatement ps = connection.prepareStatement(builder.toString());
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert();
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, OrderBy orderBy) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + orderBy.convert();
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, GroupBy group) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + group.convert();
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, WhereKey whereKey) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + whereKey.convert() + ";";
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, WhereKey whereKey, OrderBy orderBy) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + whereKey.convert() + " " + orderBy.convert() + ";";
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, WhereKey whereKey, GroupBy groupBy) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + whereKey.convert() + " " + groupBy.convert() + ";";
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, String where) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + where + ";";
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, String where, OrderBy orderBy) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + where + " " + orderBy.convert() + ";";
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void createView(ViewData data, String where, GroupBy groupBy) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            String drop = "DROP VIEW IF EXISTS " + data.getViewName() + ";";
            String builder = data.convert() + " " + where + " " + groupBy.convert() + ";";
            PreparedStatement ps = connection.prepareStatement(drop);
            ps.executeUpdate();
            ps = connection.prepareStatement(builder);
            ps.executeUpdate();
        }
    }

    public void insert(String table, ColumnData... datas) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            StringBuilder builder = new StringBuilder("insert into `" + table + "` values(");
            int i = 0;
            for (ColumnData data : datas) {
                i++;
                builder.append("?");
                if (datas.length > i) {
                    builder.append(", ");
                }
            }
            builder.append(") ;");
            PreparedStatement ps = connection.prepareStatement(builder.toString());
            for (ColumnData data : datas) {
                ps.setObject(data.number(), data.data());
            }
            ps.executeUpdate();
        }
    }

    public void update(String table, WhereKey whereKey, UpdateData... datas) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            StringBuilder builder = new StringBuilder("update `" + table + "` set ");
            int i = 0;
            for (UpdateData data : datas) {
                i++;
                builder.append(data.convert());
                if (datas.length > i) {
                    builder.append(", ");
                }
            }
            builder.append(" ").append(whereKey.convert()).append(" ;");
            PreparedStatement ps = connection.prepareStatement(builder.toString());
            ps.executeUpdate();
        }
    }

    public void delete(String table, WhereKey key) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("delete from `" + table + "`" + " " + key.convert() + " ;");
            ps.executeUpdate();
        }
    }

    public void delete(String table) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("delete from `" + table + "`;");
            ps.executeUpdate();
        }
    }

    public List<ResultData> select(SelectData selectData) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            List<ResultData> resultDataList = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(selectData.convert());
            ResultSet set = ps.executeQuery();
            ResultSetMetaData meta = set.getMetaData();
            int columnLength = meta.getColumnCount();
            while (set.next()) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 1; i <= columnLength; i++) {
                    result.put(meta.getColumnName(i), set.getObject(i));
                }
                resultDataList.add(new ResultData(result));
            }
            return resultDataList;
        }
    }

    public List<ResultData> select(SelectData selectData, WhereKey whereKey) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            List<ResultData> resultDataList = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(selectData.convert() + " " + whereKey.convert());
            ResultSet set = ps.executeQuery();
            ResultSetMetaData meta = set.getMetaData();
            int columnLength = meta.getColumnCount();
            while (set.next()) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 1; i <= columnLength; i++) {
                    result.put(meta.getColumnName(i), set.getObject(i));
                }
                resultDataList.add(new ResultData(result));
            }
            return resultDataList;
        }
    }

    public List<ResultData> select(SelectData selectData, WhereKey whereKey, OrderBy orderBy) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            List<ResultData> resultDataList = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(selectData.convert() + " " + whereKey.convert() + " " + orderBy.convert());
            ResultSet set = ps.executeQuery();
            ResultSetMetaData meta = set.getMetaData();
            int columnLength = meta.getColumnCount();
            while (set.next()) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 1; i <= columnLength; i++) {
                    result.put(meta.getColumnName(i), set.getObject(i));
                }
                resultDataList.add(new ResultData(result));
            }
            return resultDataList;
        }
    }

    public List<ResultData> select(SelectData selectData, WhereKey whereKey, GroupBy group) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            List<ResultData> resultDataList = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(selectData.convert() + " " + whereKey.convert() + " " + group.convert());
            ResultSet set = ps.executeQuery();
            ResultSetMetaData meta = set.getMetaData();
            int columnLength = meta.getColumnCount();
            while (set.next()) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 1; i <= columnLength; i++) {
                    result.put(meta.getColumnName(i), set.getObject(i));
                }
                resultDataList.add(new ResultData(result));
            }
            return resultDataList;
        }
    }

    public List<ResultData> select(String sql) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            List<ResultData> resultDataList = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet set = ps.executeQuery();
            ResultSetMetaData meta = set.getMetaData();
            int columnLength = meta.getColumnCount();
            while (set.next()) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 1; i <= columnLength; i++) {
                    result.put(meta.getColumnName(i), set.getObject(i));
                }
                resultDataList.add(new ResultData(result));
            }
            return resultDataList;
        }
    }

    public void shutdown() {
        if (this.hikariDataSource != null) {
            hikariDataSource.close();
        }
    }

}