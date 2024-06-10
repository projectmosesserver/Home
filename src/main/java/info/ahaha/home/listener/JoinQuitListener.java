package info.ahaha.home.listener;

import info.ahaha.home.Home;
import info.ahaha.home.MasterData;
import info.ahaha.home.util.SaveandLoad;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        MasterData data = Home.plugin.getDatabaseUtil().getMasterData(e.getPlayer().getUniqueId());
        if (data == null){
            SaveandLoad.load(e.getPlayer());
            return;
        }
        Home.plugin.addMasterData(data);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        MasterData data = Home.plugin.getMasterData(e.getPlayer());
        if (data == null)return;
        Home.plugin.removeMasterData(data);
    }

}
