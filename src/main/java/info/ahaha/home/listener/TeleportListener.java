package info.ahaha.home.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import info.ahaha.home.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class TeleportListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equalsIgnoreCase("playermanagement:spigot")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String channel = in.readUTF();
        if (!channel.equalsIgnoreCase("Home-Warp")) return;
        UUID uuid = UUID.fromString(in.readUTF());
        String worldName = in.readUTF();
        World world = Bukkit.getWorld(worldName);
        double x = in.readDouble();
        double y = in.readDouble();
        double z = in.readDouble();
        float yaw = in.readFloat();
        float pitch = in.readFloat();
        Location loc = new Location(world, x, y, z, yaw, pitch);
        Player p = Bukkit.getPlayer(uuid);
        if (p == null){
            Home.plugin.putQueueLocation(uuid, loc);
        }else {
            p.teleport(loc);
        }
    }
}
