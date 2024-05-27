package info.ahaha.home.util;


import databaselib.DatabaseLib;
import databaselib.etc.WhereKey;
import databaselib.insert.ColumnData;
import databaselib.select.ResultData;
import databaselib.select.SelectColumn;
import databaselib.select.SelectData;
import databaselib.update.UpdateData;
import databaselib.util.ConvertJson;
import info.ahaha.home.MasterData;
import info.ahaha.home.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DatabaseUtil {

    private final DatabaseLib lib;
    private final String table;

    public DatabaseUtil(DatabaseLib databaseLib, String table) {
        this.lib = databaseLib;
        this.table = table;
    }

    public MasterData getMasterData(UUID uuid) {
        List<ResultData> resultData = null;
        try {
            resultData = lib.select(new SelectData(table, new SelectColumn().all().notQuotation()), new WhereKey("uuid", uuid.toString()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (resultData.size() == 0) {
            return null;
        }
        return resultData.get(0).getJsonStringToCustomClass("data", MasterData.class);
    }

    public void insert(MasterData data) {
        try {
            lib.insert(table, new ColumnData(1, data.getPlayerData().getUuid().toString()), new ColumnData(2, new ConvertJson().objectToJson(data)));
        } catch (SQLException e) {
            Bukkit.getLogger().info("InsertError... データをアップデートします");
            update(data);
        }
    }

    public void update(MasterData data) {
        try {
            lib.update(table, new WhereKey("uuid", data.getPlayerData().getUuid().toString()), new UpdateData("data", new ConvertJson().objectToJson(data)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(MasterData data) {
        try {
            lib.delete(table, new WhereKey("uuid", data.getPlayerData().getUuid().toString()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        if (lib != null) {
            lib.shutdown();
        }
    }
}
