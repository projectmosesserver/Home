package info.ahaha.home.cmd;

import info.ahaha.home.Home;
import info.ahaha.home.HomeMasterData;
import info.ahaha.home.MasterData;
import info.ahaha.home.PlayerData;
import info.ahaha.home.gui.CreateGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.List;

public class Cmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length == 0) {
            CreateGUI gui = new CreateGUI();
            if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
            gui.getMenuGUI().openGUI(player);
            else{
                FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                floodgatePlayer.sendForm(gui.getHomeMenuForm(player));
            }
            return true;
        } else {
            if (args[0].equalsIgnoreCase("cost")) {
                MasterData data = Home.plugin.getMasterData(player);
                if (data == null) {
                    data = new MasterData(player.getUniqueId());
                    Home.data.add(data);
                    Home.plugin.getDatabaseUtil().insert(data);
                }
                data.setCostMaterial();
                Home.plugin.getDatabaseUtil().update(data);
                if (data.isCostMaterial()) {
                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + "コストが素材消費に変更されました！");
                }else {
                    player.sendMessage(ChatColor.GOLD + "[ Home ] " + ChatColor.GREEN + "コストが経験値に変更されました！");
                }
            }
        }
        return true;
    }

}
