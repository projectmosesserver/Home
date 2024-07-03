package info.ahaha.home.cmd;

import info.ahaha.home.Home;
import info.ahaha.home.MasterData;
import info.ahaha.home.gui.CreateGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.List;

public class Cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length == 0) {
            CreateGUI gui = new CreateGUI();
            if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
                gui.getMenuGUI().openGUI(player);
            else {
                FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                floodgatePlayer.sendForm(gui.getHomeMenuForm(player));
            }
            return true;
        } else {
            if (args[0].equalsIgnoreCase("cost")) {
                MasterData data = Home.plugin.getMasterData(player);
                if (data == null) {
                    data = new MasterData(player.getUniqueId());
                    Home.plugin.addMasterData(data);
                    Home.plugin.getDatabaseUtil().insert(data);
                }
                data.getPlayerData().setCostMaterial();
                Home.plugin.getDatabaseUtil().update(data);
                if (data.getPlayerData().isCostMaterial()) {
                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + "コストが素材消費に変更されました！");
                } else {
                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + "コストが経験値に変更されました！");
                }
            }
            if (args[0].equalsIgnoreCase("rename")) {
                if (args.length >= 4) {
                    String old = args[1];
                    String newName = args[2];
                    MasterData data = Home.plugin.getMasterData(player);
                    if (data == null) return true;
                    if (data.renameMasterData(old, newName)) {
                        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + old + " -> " + newName + " に変更しました");
                    } else {
                        player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.RED + "名前変更に失敗しました");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return null;
        if (strings.length == 1) {
            return List.of("cost", "rename");
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("rename")) {
                MasterData data = Home.plugin.getMasterData(player);
                if (data == null)return null;
                List<String> list = new ArrayList<>();
                data.getHomeMasterData().forEach(m-> list.add(m.getName()));
                return list;
            }
        }
        return List.of();
    }
}
