package info.ahaha.home.listener;

import info.ahaha.home.Home;
import info.ahaha.home.MasterData;
import info.ahaha.home.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AddHomeListener implements Listener {

    @EventHandler
    public void onAddHome(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null)return;
        if (!Home.addPlayers.contains(player.getUniqueId()))return;
        e.setCancelled(true);
        if (!data.addMasterHome(e.getMessage(),player.getLocation())){
            player.sendMessage(ChatColor.GOLD+"[ Home ] "+ChatColor.RED+"登録枠が上限値のため登録できませんでした！");
        }else {
            player.sendMessage(ChatColor.GOLD+"[ Home ] "+ChatColor.GREEN+e.getMessage()+" を登録しました！");
            Home.plugin.getDatabaseUtil().update(data);
        }
        Home.addPlayers.remove(player.getUniqueId());
    }
}
