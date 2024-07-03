package info.ahaha.home;

import databaselib.DatabaseLib;
import databaselib.table.ColumnField;
import info.ahaha.home.cmd.Cmd;
import info.ahaha.home.listener.*;
import info.ahaha.home.util.AddSubmitNumItem;
import info.ahaha.home.util.DatabaseUtil;
import info.ahaha.home.util.ItemUtil;
import info.ahaha.home.util.SaveandLoad;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

public final class Home extends JavaPlugin {

    public static Home plugin;
    public static List<PlayerData> data = new ArrayList<>();
    private static List<MasterData> masterData = new ArrayList<>();
    public static List<AddSubmitNumItem.ItemData> itemData = new ArrayList<>();
    public static List<UUID> addPlayers = new ArrayList<>();

    private DataManager manager;
    private ItemUtil itemUtil;
    private DatabaseUtil databaseUtil;

    private Map<UUID, Location> queuePlayers = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        manager = new DataManager();
        manager.saveDefaultConfig();

        FileConfiguration config = manager.getConfig();
        String host = config.getString("MySQL.Host");
        String port = config.getString("MySQL.Port");
        String dbname = config.getString("MySQL.DBName");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        String user = config.getString("MySQL.User");
        String pass = config.getString("MySQL.Password");
        String table = config.getString("MySQL.Table");
        DatabaseLib databaseLib = new DatabaseLib(url, user, pass);
        try {
            databaseLib.createTable(
                    table,
                    new ColumnField("uuid", ColumnField.ColumnType.VAR_CHAR.num(50)).key(),
                    new ColumnField("data", ColumnField.ColumnType.JSON)
            );
        } catch (SQLException e) {
            getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(e);
        }
        this.databaseUtil = new DatabaseUtil(databaseLib, table);
        itemUtil = new ItemUtil();
        itemUtil.createItemData();
        itemUtil.addRecipe();

        if (SaveandLoad.containFiles()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SaveandLoad.load(player);
            }
        }else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                MasterData masterData1 = databaseUtil.getMasterData(player.getUniqueId());
                if (masterData1 == null){
                    masterData1 = new MasterData(player.getUniqueId());
                    databaseUtil.insert(masterData1);
                }
                addMasterData(masterData1);
            }
        }

        getServer().getPluginManager().registerEvents(new SubmitListener(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
        getServer().getPluginManager().registerEvents(new AddHomeListener(), this);
        getServer().getPluginManager().registerEvents(new TPMenuListener(), this);
        getServer().getPluginManager().registerEvents(new RemoveHomeListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "playermanagement:bungee");
        getServer().getMessenger().registerIncomingPluginChannel(this, "playermanagement:spigot", new TeleportListener());

        getCommand("home").setExecutor(new Cmd());
        getCommand("home").setTabCompleter(new Cmd());

    }

    public DataManager getManager() {
        return manager;
    }

    public ItemUtil getItemUtil() {
        return itemUtil;
    }

    public PlayerData getPlayerData(Player player) {
        for (PlayerData data : data) {
            if (data.getUuid().equals(player.getUniqueId())) return data;
        }
        return null;
    }

    public MasterData getMasterData(Player player) {
        for (MasterData data : masterData) {
            if (data.getPlayerData().getUuid().equals(player.getUniqueId())) return data;
        }
        return null;
    }

    public void removeMasterData(MasterData data){
        masterData.remove(data);
    }

    public boolean containQueue(UUID uuid) {
        return queuePlayers.containsKey(uuid);
    }

    public void putQueueLocation(UUID uuid, Location loc) {
        queuePlayers.put(uuid, loc);
    }

    public void teleportQueuePlayer(UUID uuid) {
        if (containQueue(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            player.teleport(queuePlayers.get(uuid));
            queuePlayers.remove(uuid);
        }
    }

    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    public void addMasterData(MasterData data) {
        masterData.add(data);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        itemUtil.removeRecipe();
        if (databaseUtil != null)
            databaseUtil.shutdown();
    }
}
