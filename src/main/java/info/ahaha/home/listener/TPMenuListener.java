package info.ahaha.home.listener;

import info.ahaha.home.*;
import info.ahaha.home.gui.CreateGUI;
import info.ahaha.home.gui.GUI;
import info.ahaha.home.gui.RemoveGUI;
import info.ahaha.home.gui.TPMenuGUI;
import info.ahaha.home.util.Config;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TPMenuListener implements Listener {

    private List<Player> tpPlayers = new ArrayList<>();

    @EventHandler
    public void onTp(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof TPMenuGUI)) return;
        e.setCancelled(true);
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;
        Player player = (Player) e.getWhoClicked();
        if (tpPlayers.contains(player)) return;
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null) {
            data = new MasterData(player.getUniqueId());
            Home.plugin.addMasterData(data);
            Home.plugin.getDatabaseUtil().insert(data);
        }
        HomeMasterData homeData = data.getHomeMasterData().get(e.getSlot());
        if (homeData == null) return;
        if (Config.isCostEnable()) {
            if (data.getPlayerData().isCostMaterial()) {
                Map<Material, Integer> currents = new HashMap<>();
                for (Material s : Config.getCostMaterials().keySet()) {
                    ItemStack item = new ItemStack(s);
                    currents.putIfAbsent(s, 0);
                    for (ItemStack items : player.getInventory().getContents()) {
                        if (items == null) continue;
                        if (items.isSimilar(item))
                            currents.put(s, currents.get(s) + items.getAmount());
                    }
                }
                Map<Material, Boolean> checkAmounts = new HashMap<>();
                for (Material s : currents.keySet()) {
                    if (currents.get(s) >= Config.getCostMaterials().get(s)) {
                        checkAmounts.put(s, true);
                    } else {
                        checkAmounts.put(s, false);
                    }
                }
                for (Material s : checkAmounts.keySet()) {
                    if (!checkAmounts.get(s)) {
                        int cost = Config.getCostMaterials().get(s);
                        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + s.name() + "が" + cost + "個必要です！");
                        return;
                    }
                }
                for (Material s : currents.keySet()) {
                    player.getInventory().removeItem(new ItemStack(s, Config.getCostMaterials().get(s)));

                }
            } else {
                if (player.getLevel() < Config.getCostLevel()) {
                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + "Levelが " + Config.getCostLevel() + " 必要です！");
                    return;
                } else {
                    player.setLevel(player.getLevel() - Config.getCostLevel());
                }
            }
        }
        tpPlayers.add(player);
        player.closeInventory();
        new BukkitRunnable() {
            int time = Config.getTpTime();

            @Override
            public void run() {
                if (!Config.isSpecifyTime()) time = 0;
                if (time < 1) {
                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + homeData.getName() + "にテレポートしました！");
                    homeData.warp(player);
                    player.playEffect(EntityEffect.TELEPORT_ENDER);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2f,1f);
                    tpPlayers.remove(player);
                    this.cancel();
                    return;
                }
                player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + time + "秒後にテレポートします！");
                time--;
            }
        }.runTaskTimer(Home.plugin, 0, 20);
    }
}
