package info.ahaha.home.util;

import info.ahaha.home.Home;
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

    public static void load(Player player) {
        File dataDirectory = new File(Home.plugin.getDataFolder() + "/data/");
        if (!dataDirectory.exists()){
            dataDirectory.mkdir();
        }
        File data = new File(Home.plugin.getDataFolder()+"/data", player.getUniqueId()+".data");
        if (!data.exists()) {
            try {
                data.createNewFile();
                Home.data.add(new PlayerData(player.getUniqueId()));
                return;
            } catch (IOException ev) {
                ev.printStackTrace();
            }
        }
        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new FileInputStream(data));
            PlayerData playerData = (PlayerData) inputStream.readObject();
            if (!Home.data.contains(playerData))
                Home.data.add(playerData);
        } catch (IOException | ClassNotFoundException ev) {
            ev.printStackTrace();
        }
    }

}
