package info.ahaha.home;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeMasterData extends HomeData{

    private final String server;
    public HomeMasterData(String name, Location loc) {
        super(name, loc);
        this.server = Home.plugin.getManager().getConfig().getString("ServerName");
    }

    public String getServer() {
        return server;
    }

    public void warp(Player player){
        String serverName = Home.plugin.getManager().getConfig().getString("ServerName");;
        if (serverName != null && serverName.equalsIgnoreCase(this.server)){
            teleport(player);
        }else {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Home-Warp");
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(getServer());
            out.writeUTF(getWorld());
            out.writeDouble(getX());
            out.writeDouble(getY());
            out.writeDouble(getZ());
            out.writeFloat(getYaw());
            out.writeFloat(getPitch());
            player.sendPluginMessage(Home.plugin, "playermanagement:bungee", out.toByteArray());
        }
    }
}
