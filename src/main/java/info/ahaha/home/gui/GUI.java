package info.ahaha.home.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUI implements InventoryHolder {

    private final Inventory gui;

    public GUI(Inventory gui) {
        this.gui = gui;
    }

    @Override
    public Inventory getInventory() {
        return gui;
    }

    public void openGUI(Player player){
        player.openInventory(getInventory());
    }
}
