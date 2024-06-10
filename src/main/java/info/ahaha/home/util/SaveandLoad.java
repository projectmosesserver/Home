package info.ahaha.home.util;

import com.google.gson.Gson;
import info.ahaha.home.Home;
import info.ahaha.home.MasterData;
import info.ahaha.home.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.List;

public class SaveandLoad {

    public static void save(Player player) {
        PlayerData data = Home.plugin.getPlayerData(player);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Home.plugin.getDataFolder() + "/data/" + player.getUniqueId() + ".data"));
            out.writeObject(data);
            out.flush();
            out.close();
            Home.data.remove(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean containFiles(){
        File dataDirectory = new File(Home.plugin.getDataFolder() + "/data/");
        if (!dataDirectory.exists()){
            return false;
        }else {
            File[] files = dataDirectory.listFiles();
            if (files == null){
                return false;
            }
            return files.length != 0;
        }
    }
    public static void load(Player player) {
        Bukkit.getLogger().info(player.getName()+" data load");
        File data = new File(Home.plugin.getDataFolder()+"/data", player.getUniqueId()+".data");
        if (!data.exists()) {
            Bukkit.getLogger().info(player.getName()+" data not exists");
            MasterData masterData = Home.plugin.getDatabaseUtil().getMasterData(player.getUniqueId());
            if (masterData == null){
                masterData = new MasterData(player.getUniqueId());
                Bukkit.getLogger().info(player.getName()+" database not exists");
            }else {
                Bukkit.getLogger().info(player.getName()+" database exists");
            }
            Bukkit.getLogger().info(new Gson().toJson(masterData));
            Home.plugin.addMasterData(masterData);
            Home.plugin.getDatabaseUtil().insert(masterData);
            return;
        }
        try {
            Bukkit.getLogger().info(player.getName()+" data exists");
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new FileInputStream(data));
            PlayerData playerData = (PlayerData) inputStream.readObject();
            Bukkit.getLogger().info(new Gson().toJson(playerData));
            MasterData masterData = new MasterData(playerData);
            Home.plugin.addMasterData(masterData);
            Home.plugin.getDatabaseUtil().insert(masterData);
            data.delete();
        } catch (IOException | ClassNotFoundException ev) {
            ev.printStackTrace();
        }
    }

}
