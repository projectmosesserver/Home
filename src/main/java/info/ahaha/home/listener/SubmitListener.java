package info.ahaha.home.listener;

import info.ahaha.home.util.AddSubmitNumItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SubmitListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)return;
        if (e.getHand() != EquipmentSlot.HAND)return;
        Player player = e.getPlayer();
        AddSubmitNumItem.addMaxSubmitNum(player);
    }
}
