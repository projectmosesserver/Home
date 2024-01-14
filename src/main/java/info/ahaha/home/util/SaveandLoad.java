package info.ahaha.home.util;

import info.ahaha.home.Home;
import info.ahaha.home.MasterData;
import info.ahaha.home.PlayerData;
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
        File data = new File(Home.plugin.getDataFolder()+"/data", player.getUniqueId()+".data");
        if (!data.exists()) {
            MasterData masterData = Home.plugin.getDatabaseUtil().getMasterData(player.getUniqueId());
            if (masterData == null){
                masterData = new MasterData(player.getUniqueId());
            }
            Home.plugin.addMasterData(masterData);
            Home.plugin.getDatabaseUtil().insert(masterData);
            return;
        }
        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new FileInputStream(data));
            PlayerData playerData = (PlayerData) inputStream.readObject();
            MasterData masterData = new MasterData(playerData);
            Home.plugin.addMasterData(masterData);
            Home.plugin.getDatabaseUtil().insert(masterData);
            data.delete();
        } catch (IOException | ClassNotFoundException ev) {
            ev.printStackTrace();
        }
    }

}
