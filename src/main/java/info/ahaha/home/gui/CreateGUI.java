package info.ahaha.home.gui;

import info.ahaha.home.*;
import info.ahaha.home.util.Config;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.component.DropdownComponent;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;

public class CreateGUI {


    public SimpleForm getHomeMenuForm(Player player) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        return SimpleForm.builder()
                .title(ChatColor.GOLD + "Home")
                .button("Home", FormImage.Type.URL, Config.getTPMenuURL())
                .button("AddHome", FormImage.Type.URL, Config.getAddMenuURL())
                .button("RemoveHome", FormImage.Type.URL, Config.getRemoveMenuURL())
                .responseHandler((form, s) -> {
                    SimpleFormResponse response = form.parseResponse(s);
                    if (response.isCorrect()) {
                        if (response.getClickedButtonId() == 0) {
                            floodgatePlayer.sendForm(getHomeForm(player));
                        } else if (response.getClickedButtonId() == 1) {
                            floodgatePlayer.sendForm(getAddHomeForm(player));
                        } else if (response.getClickedButtonId() == 2) {
                            floodgatePlayer.sendForm(getRemoveHomeForm(player));
                        }
                    }
                })
                .build();
    }

    public CustomForm getAddHomeForm(Player player) {
        CustomForm.Builder builder = CustomForm.builder();
        builder.title(ChatColor.GOLD + "AddHome");
        builder.input("登録するホームの名前を入力してください");
        builder.responseHandler((form, s) -> {
            CustomFormResponse response = form.parseResponse(s);
            if (response.isCorrect()) {
                if (response.getInput(0) != null) {
                    MasterData data = Home.plugin.getMasterData(player);
                    if (data == null) return;
                    for (HomeData homeData : data.getHomeMasterData()) {
                        if (homeData.getName().equalsIgnoreCase(response.getInput(0))) {
                            player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + response.getInput(0) + "はすでに登録されています！");
                            return;
                        }
                    }
                    if (data.addMasterHome(response.getInput(0), player.getLocation())){
                        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + response.getInput(0) + "を登録しました！");
                        Home.plugin.getDatabaseUtil().update(data);
                    } else {
                        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + "登録枠が上限値のため登録できませんでした！");
                    }
                }
            }
        });

        return builder.build();
    }
    public SimpleForm getHomeForm(Player player) {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title(ChatColor.GOLD + "Home");
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null){
            data = new MasterData(player.getUniqueId());
            Home.plugin.addMasterData(data);
            Home.plugin.getDatabaseUtil().insert(data);
        }
        if (data.getHomeMasterData().isEmpty()) {
            builder.content(ChatColor.GREEN + "まだHomeが登録されていません！");
            builder.responseHandler((form, s) -> {
                SimpleFormResponse response = form.parseResponse(s);
            });
        } else {
            builder.content(ChatColor.GREEN + "移動先のボタンを選択してください！\n\n");
            for (HomeMasterData homeData : data.getHomeMasterData()) {
                builder.button(homeData.getName());
            }
            MasterData finalData = data;
            builder.responseHandler((form, s) -> {
                SimpleFormResponse response = form.parseResponse(s);
                if (response.isCorrect()) {
                    HomeMasterData homeData = null;
                    for (int i = 0; i < finalData.getHomeMasterData().size(); i++) {
                        if (response.getClickedButtonId() == i) {
                            homeData = finalData.getHomeMasterData().get(i);
                            break;
                        }
                    }
                    if (homeData != null){
                        if (Config.isCostEnable()) {
                            if (finalData.getPlayerData().isCostMaterial()) {
                                Map<Material, Integer> currents = new HashMap<>();
                                for (Material material : Config.getCostMaterials().keySet()) {
                                    ItemStack item = new ItemStack(material);
                                    currents.putIfAbsent(material, 0);
                                    for (ItemStack items : player.getInventory().getContents()) {
                                        if (items == null) continue;
                                        if (items.isSimilar(item))
                                            currents.put(material, currents.get(material) + items.getAmount());
                                    }
                                }
                                Map<Material, Boolean> checkAmounts = new HashMap<>();
                                for (Material material : currents.keySet()) {
                                    if (currents.get(material) >= Config.getCostMaterials().get(material)) {
                                        checkAmounts.put(material, true);
                                    } else {
                                        checkAmounts.put(material, false);
                                    }
                                }
                                for (Material material : checkAmounts.keySet()) {
                                    if (!checkAmounts.get(material)) {
                                        int cost = Config.getCostMaterials().get(material);
                                        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + material.name() + "が" + cost + "個必要です！");
                                        return;
                                    }
                                }
                                for (Material material : currents.keySet()) {
                                    player.getInventory().removeItem(new ItemStack(material, Config.getCostMaterials().get(material)));

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
                        HomeMasterData finalHomeData = homeData;
                        new BukkitRunnable() {
                            int time = Config.getTpTime();

                            @Override
                            public void run() {
                                if (!Config.isSpecifyTime()) time = 0;
                                if (time < 1) {
                                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + finalHomeData.getName() + "にテレポートしました！");
                                    finalHomeData.warp(player);
                                    player.playEffect(EntityEffect.TELEPORT_ENDER);
                                    player.playSound(player.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,2f,1f);
                                    this.cancel();
                                    return;
                                }
                                player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + time + "秒後にテレポートします！");
                                time--;
                            }
                        }.runTaskTimer(Home.plugin, 0, 20);
                    }
                }
            });
        }

        return builder.build();
    }

    public CustomForm getRemoveHomeForm(Player player) {
        CustomForm.Builder builder = CustomForm.builder();
        builder.title(ChatColor.GOLD + "RemoveHome");
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null){
            data = new MasterData(player.getUniqueId());
            Home.plugin.addMasterData(data);
            Home.plugin.getDatabaseUtil().insert(data);
        }
        if (data.getHomeMasterData().isEmpty()) {
            builder.label(ChatColor.GREEN + "まだHomeが登録されていません！");
            builder.responseHandler((form, s) -> {
                CustomFormResponse response = form.parseResponse(s);
            });

        } else {
            DropdownComponent.Builder builder1 = DropdownComponent.builder().text("削除する項目を選択してください");

            for (HomeMasterData homeData : data.getHomeMasterData()) {
                builder1.option(homeData.getName());
            }
            builder.dropdown(builder1);
            MasterData finalData = data;
            builder.responseHandler((form, s) -> {
                CustomFormResponse response = form.parseResponse(s);
                if (response.isCorrect()) {
                    int i = 0;
                    for (HomeMasterData homeData : finalData.getHomeMasterData()) {
                        if (response.getDropdown(0) == i){
                            finalData.removeMasterHome(homeData.getName());
                            Home.plugin.getDatabaseUtil().update(finalData);
                            player.sendMessage(ChatColor.GOLD+"[ HomePlugin ] "+ChatColor.GREEN+homeData.getName()+"を削除しました！");
                            break;
                        }
                        i++;
                    }
                }
            });
        }
        return builder.build();
    }


    public GUI getMenuGUI() {

        MenuGUI gui = new MenuGUI(null);
        Inventory inv = Bukkit.createInventory(gui, InventoryType.HOPPER, ChatColor.BLUE + "" + ChatColor.BOLD + "Home Menu");

        ItemStack home = new ItemStack(Material.ENDER_EYE);
        ItemStack add = new ItemStack(Material.ANVIL);
        ItemStack remove = new ItemStack(Material.COMPOSTER);

        ItemMeta hmeta = home.getItemMeta();
        ItemMeta addmeta = add.getItemMeta();
        ItemMeta remmeta = remove.getItemMeta();

        hmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Home");
        addmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "AddHome");
        remmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "RemoveHome");

        home.setItemMeta(hmeta);
        add.setItemMeta(addmeta);
        remove.setItemMeta(remmeta);

        inv.setItem(1, home);
        inv.setItem(2, add);
        inv.setItem(3, remove);

        return new GUI(inv);
    }

    public GUI getRemoveGUI(Player player) {
        RemoveGUI gui = new RemoveGUI(null);
        Inventory inv = Bukkit.createInventory(gui, 54, ChatColor.BLUE + "" + ChatColor.BOLD + player.getName() + "'s RemoveMenu");
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null) {
            data = new MasterData(player.getUniqueId());
            Home.plugin.getDatabaseUtil().insert(data);
            Home.plugin.addMasterData(data);
        }
        for (HomeMasterData homeData : data.getHomeMasterData()) {
            ItemStack home = new ItemStack(Material.ENDER_EYE);
            ItemMeta meta = home.getItemMeta();
            meta.setDisplayName("§6§n" + homeData.getName());

            List<String> lore = new ArrayList<>();
            String world = homeData.getWorld();
            int x = (int) homeData.getX();
            int y = (int) homeData.getY();
            int z = (int) homeData.getZ();
            lore.add(ChatColor.YELLOW + "World : " + ChatColor.AQUA + world);
            lore.add(ChatColor.YELLOW + "X : " + ChatColor.AQUA + x);
            lore.add(ChatColor.YELLOW + "Y : " + ChatColor.AQUA + y);
            lore.add(ChatColor.YELLOW + "Z : " + ChatColor.AQUA + z);
            meta.setLore(lore);
            home.setItemMeta(meta);
            inv.addItem(home);
        }
        return new GUI(inv);
    }

    public GUI getTPMenuGUI(Player player) {
        TPMenuGUI gui = new TPMenuGUI(null);
        Inventory inv = Bukkit.createInventory(gui, 54, ChatColor.BLUE + "" + ChatColor.BOLD + player.getName() + "'s Home");
        MasterData data = Home.plugin.getMasterData(player);
        if (data == null) {
            data = new MasterData(player.getUniqueId());
            Home.plugin.getDatabaseUtil().insert(data);
            Home.plugin.addMasterData(data);
        }
        for (HomeMasterData homeData : data.getHomeMasterData()) {
            ItemStack home = new ItemStack(Material.ENDER_EYE);
            ItemMeta meta = home.getItemMeta();
            meta.setDisplayName("§6§n" + homeData.getName());

            List<String> lore = new ArrayList<>();
            String world = homeData.getWorld();
            int x = (int) homeData.getX();
            int y = (int) homeData.getY();
            int z = (int) homeData.getZ();
            lore.add(ChatColor.GRAY + "-----------------");
            lore.add("");
            lore.add(ChatColor.YELLOW + "World : " + ChatColor.AQUA + world);
            lore.add(ChatColor.YELLOW + "X : " + ChatColor.AQUA + x);
            lore.add(ChatColor.YELLOW + "Y : " + ChatColor.AQUA + y);
            lore.add(ChatColor.YELLOW + "Z : " + ChatColor.AQUA + z);
            lore.add("");
            lore.add(ChatColor.GRAY + "-----------------");
            meta.setLore(lore);
            home.setItemMeta(meta);
            inv.addItem(home);
        }
        return new GUI(inv);
    }


}
