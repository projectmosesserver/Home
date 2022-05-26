package info.ahaha.home;

import info.ahaha.home.cmd.Cmd;
import info.ahaha.home.listener.*;
import info.ahaha.home.util.AddSubmitNumItem;
import info.ahaha.home.util.ItemUtil;
import info.ahaha.home.util.SaveandLoad;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Home extends JavaPlugin {

    public static Home plugin;
    public static List<PlayerData> data = new ArrayList<>();
    public static List<AddSubmitNumItem.ItemData>itemData = new ArrayList<>();
    public static List<UUID>addPlayers = new ArrayList<>();

    private DataManager manager;
    private ItemUtil itemUtil;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        manager = new DataManager();
        manager.saveDefaultConfig();
        itemUtil = new ItemUtil();
        itemUtil.createItemData();
        itemUtil.addRecipe();

        for (Player player : Bukkit.getOnlinePlayers()){
            SaveandLoad.load(player);
        }

        getServer().getPluginManager().registerEvents(new SubmitListener(),this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(),this);
        getServer().getPluginManager().registerEvents(new AddHomeListener(),this);
        getServer().getPluginManager().registerEvents(new TPMenuListener(),this);
        getServer().getPluginManager().registerEvents(new RemoveHomeListener(),this);
        getServer().getPluginManager().registerEvents(new MenuListener(),this);

        getCommand("home").setExecutor(new Cmd());

    }

    public DataManager getManager() {
        return manager;
    }

    public ItemUtil getItemUtil() {
        return itemUtil;
    }

    public PlayerData getPlayerData(Player player){
        for (PlayerData data : data){
            if (data.getUuid().equals(player.getUniqueId()))return data;
        }
        return null;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        itemUtil.removeRecipe();
        for (Player player : Bukkit.getOnlinePlayers()){
            SaveandLoad.save(player);
        }
    }
}
