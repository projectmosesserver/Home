package info.ahaha.home.listener;

import info.ahaha.home.Home;
import info.ahaha.home.PlayerData;
import info.ahaha.home.util.SaveandLoad;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        SaveandLoad.load(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        SaveandLoad.save(e.getPlayer());
    }
}
