package info.ahaha.home.listener;

import info.ahaha.home.Home;
import info.ahaha.home.HomeData;
import info.ahaha.home.PlayerData;
import info.ahaha.home.gui.CreateGUI;
import info.ahaha.home.gui.RemoveGUI;
import info.ahaha.home.gui.TPMenuGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RemoveHomeListener implements Listener {

    @EventHandler
    public void onRemove(InventoryClickEvent e){
        if (!(e.getInventory().getHolder() instanceof RemoveGUI))return;
        e.setCancelled(true);
        if (e.getClickedInventory() == null)return;
        if (!e.getClickedInventory().equals(e.getView().getTopInventory()))return;
        Player player = (Player) e.getWhoClicked();
        PlayerData data = Home.plugin.getPlayerData(player);
        if (data == null){
            data = new PlayerData(player.getUniqueId());
            Home.data.add(data);
        }
        HomeData homeData = data.getData().get(e.getSlot());
        if (homeData == null)return;
        if (data.removeHome(homeData.getName()))
        player.sendMessage(ChatColor.GOLD+"[ Home ] "+ChatColor.GREEN+homeData.getName()+"を削除しました！");
        CreateGUI gui = new CreateGUI();
        gui.getRemoveGUI(player).openGUI(player);
    }
}
