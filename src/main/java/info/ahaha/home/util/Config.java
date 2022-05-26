package info.ahaha.home.util;

import info.ahaha.home.DataManager;
import info.ahaha.home.Home;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private final static int tpTime = Home.plugin.getManager().getConfig().getInt("TpTime");
    private final static boolean specifyTime = Home.plugin.getManager().getConfig().getBoolean("SpecifyTime");
    private final static boolean costEnable = Home.plugin.getManager().getConfig().getBoolean("CostEnable");
    private final static Map<Material, Integer> costMaterials = new HashMap<>();
    private final static int costLevel = Home.plugin.getManager().getConfig().getInt("Level");
    private final static String addMenuURL = Home.plugin.getManager().getConfig().getString("AddMenuImageURL");
    private final static String removeMenuURL = Home.plugin.getManager().getConfig().getString("RemoveMenuImageURL");
    private final static String TPMenuURL = Home.plugin.getManager().getConfig().getString("TPMenuImageURL");

    public static Map<Material, Integer> getCostMaterials() {
        if (costMaterials.isEmpty()) {
            for (String s : Home.plugin.getManager().getConfig().getStringList("Materials")) {
                costMaterials.put(org.bukkit.Material.valueOf(s), Home.plugin.getManager().getConfig().getInt(s + ".Amount"));
            }
        }
        return costMaterials;
    }

    public static int getTpTime() {
        return tpTime;
    }

    public static boolean isSpecifyTime() {
        return specifyTime;
    }

    public static boolean isCostEnable() {
        return costEnable;
    }

    public static int getCostLevel() {
        return costLevel;
    }

    public static String getAddMenuURL() {
        return addMenuURL;
    }

    public static String getRemoveMenuURL() {
        return removeMenuURL;
    }

    public static String getTPMenuURL() {
        return TPMenuURL;
    }
}
