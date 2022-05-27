package info.ahaha.home.util;

import info.ahaha.home.Home;
import info.ahaha.home.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.List;

public class AddSubmitNumItem {

    public static void addMaxSubmitNum(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemData data = getItemData(item);
        if (data == null) return;
        if (data.getSubmitNum() == -1) return;
        if (!data.isSubmit()) return;
        PlayerData playerData = Home.plugin.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.getMaxSubmitNum() + 1 != data.getSubmitNum()) {
            player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + "このアイテムは枠上限を" + data.getSubmitNum() + "に増やすアイテムです！");
            return;
        }
        if (!playerData.addSubmitNum()) {
            player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + "登録枠の上限が最大値のため枠を増やすことができませんでした！");
            return;
        }
        item.setAmount(item.getAmount() - 1);
        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + "登録枠の上限が" + playerData.getMaxSubmitNum() + "になりました！");
    }

    public static ItemData getItemData(ItemStack item) {
        for (ItemData data : Home.itemData) {
            if (data.getItem().isSimilar(item)) return data;
        }
        return null;
    }

    public static ItemData getItemData(String id) {
        for (ItemData data : Home.itemData) {
            if (data.getId().equalsIgnoreCase(id)) return data;
        }
        return null;
    }

    public static class ItemData {

        private final String id;
        private final String name;
        private final List<String> lore;
        private boolean glow;
        private final Material material;
        private boolean submit;
        private int submitNum = -1;
        private final ItemStack item;

        public ItemData(String id, Material material, String name, List<String> lore, boolean glow, boolean submit, int submitNum) {
            this.id = id;
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.glow = glow;
            this.submit = submit;
            if (submitNum != 0)
                this.submitNum = submitNum;
            item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            if (glow) {
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

        public boolean isGlow() {
            return glow;
        }

        public Material getMaterial() {
            return material;
        }

        public boolean isSubmit() {
            return submit;
        }

        public int getSubmitNum() {
            return submitNum;
        }

        public ItemStack getItem() {
            return item;
        }
    }
}
