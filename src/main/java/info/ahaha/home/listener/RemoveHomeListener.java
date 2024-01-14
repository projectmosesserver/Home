package info.ahaha.home.listener;

import info.ahaha.home.Home;
import info.ahaha.home.HomeMasterData;
import info.ahaha.home.MasterData;
import info.ahaha.home.gui.CreateGUI;
import info.ahaha.home.gui.RemoveGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RemoveHomeListener implements Listener {

    @EventHandler
    public void onRemove(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof RemoveGUI)) return;
        e.setCancelled(true);
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;
        Player player = (Player) e.getWhoClicked();
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null) {
            data = new MasterData(player.getUniqueId());
            Home.plugin.addMasterData(data);
            Home.plugin.getDatabaseUtil().insert(data);
        }
        HomeMasterData homeMasterData = data.getHomeMasterData().get(e.getSlot());
        if (homeMasterData == null) return;
        if (data.removeMasterHome(homeMasterData.getName())){
            player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + homeMasterData.getName() + "を削除しました！");
            Home.plugin.getDatabaseUtil().update(data);
        }
        CreateGUI gui = new CreateGUI();
        gui.getRemoveGUI(player).openGUI(player);
    }
}
