package info.ahaha.home.listener;

import info.ahaha.home.Home;
import info.ahaha.home.gui.CreateGUI;
import info.ahaha.home.gui.MenuGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;
        if (!(e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "" + ChatColor.BOLD + "Home Menu"))) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        CreateGUI createGUI = new CreateGUI();
        if (e.getSlot() == 1) {
            createGUI.getTPMenuGUI(player).openGUI(player);
        } else if (e.getSlot() == 2) {
            player.closeInventory();
            Home.addPlayers.add(player.getUniqueId());
            player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + "登録する名前を入力してください！");
        } else if (e.getSlot() == 3) {
            createGUI.getRemoveGUI(player).openGUI(player);
        }
    }
}
