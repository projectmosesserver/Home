package info.ahaha.home;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static info.ahaha.home.Home.plugin;

public class DataManager {
    public static FileConfiguration config, item, recipe = null;
    public static File configFile, itemFile, recipeFile = null;

    public DataManager() {
    }

    public static void reloadConfig() {
        if (configFile == null)
            configFile = new File(plugin.getDataFolder(), "config.yml");
        if (itemFile == null)
            itemFile = new File(plugin.getDataFolder(),"item.yml");
        if (recipeFile == null)
            recipeFile = new File(plugin.getDataFolder(),"recipe.yml");

        config = YamlConfiguration.loadConfiguration(configFile);
        item = YamlConfiguration.loadConfiguration(itemFile);
        recipe = YamlConfiguration.loadConfiguration(recipeFile);

        InputStream configStream = plugin.getResource("config.yml");
        InputStream itemStream = plugin.getResource("item.yml");
        InputStream recipeStream = plugin.getResource("recipe.yml");

        if (configStream != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(configStream));
            config.setDefaults(config);
        }

        if (itemStream != null){
            YamlConfiguration item = YamlConfiguration.loadConfiguration(new InputStreamReader(itemStream));
            item.setDefaults(item);
        }

        if (recipeStream != null){
            YamlConfiguration recipe = YamlConfiguration.loadConfiguration(new InputStreamReader(recipeStream));
            recipe.setDefaults(recipe);
        }

    }


    public FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();
        return config;
    }

    public FileConfiguration getItem(){
        if (item == null)
            reloadConfig();
        return item;
    }

    public FileConfiguration getRecipe(){
        if (recipe == null)
            reloadConfig();
        return recipe;
    }

    public void saveDefaultConfig() {

        if (configFile == null)
            configFile = new File(plugin.getDataFolder(), "config.yml");

        if (itemFile == null)
            itemFile = new File(plugin.getDataFolder(),"item.yml");

        if (recipeFile == null)
            recipeFile = new File(plugin.getDataFolder(),"recipe.yml");

        if (!configFile.exists())
            plugin.saveResource("config.yml", false);

        if (!itemFile.exists())
            plugin.saveResource("item.yml",false);

        if (!recipeFile.exists())
            plugin.saveResource("recipe.yml",false);
    }
}
