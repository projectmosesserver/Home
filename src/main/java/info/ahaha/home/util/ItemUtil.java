package info.ahaha.home.util;

import info.ahaha.home.Home;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemUtil {

    private List<NamespacedKey> keys = new ArrayList<>();

    public void createItemData() {

        for (String id : Home.plugin.getManager().getItem().getStringList("Items")) {
            Material material = Material.valueOf(Home.plugin.getManager().getItem().getString(id + ".Material"));
            String name = Home.plugin.getManager().getItem().getString(id + ".Name");
            List<String> lore = Home.plugin.getManager().getItem().getStringList(id + ".Lore");
            boolean glow = Home.plugin.getManager().getItem().getBoolean(id + ".Glow");
            boolean submit = Home.plugin.getManager().getItem().getBoolean(id + ".Submit");
            int submitNum = Home.plugin.getManager().getItem().getInt(id + ".SubmitNum");
            Home.itemData.add(new AddSubmitNumItem.ItemData(id, material, name, lore, glow, submit, submitNum));
        }
    }

    public void addRecipe() {
        for (String id : Home.plugin.getManager().getRecipe().getKeys(false)) {
            NamespacedKey key = new NamespacedKey(Home.plugin, id);
            keys.add(key);
            AddSubmitNumItem.ItemData data = AddSubmitNumItem.getItemData(id);
            if (data == null) continue;
            String craft = Home.plugin.getManager().getRecipe().getString(id + ".RecipeType");
            if (craft.equalsIgnoreCase("CRAFT")) {
                ShapedRecipe recipe = new ShapedRecipe(key, data.getItem());
                String first = Home.plugin.getManager().getRecipe().getString(id + ".First");
                String second = Home.plugin.getManager().getRecipe().getString(id + ".Second");
                String third = Home.plugin.getManager().getRecipe().getString(id + ".Third");
                recipe.shape(first, second, third);
                Set<Character> chars = new HashSet<>();
                for (Character c : first.toCharArray()) {
                    if (c.toString().equalsIgnoreCase(" ")) continue;
                    chars.add(c);
                }
                for (Character c : second.toCharArray()) {
                    if (c.toString().equalsIgnoreCase(" ")) continue;
                    chars.add(c);
                }
                for (Character c : third.toCharArray()) {
                    if (c.toString().equalsIgnoreCase(" ")) continue;
                    chars.add(c);
                }
                for (Character c : chars) {
                    Material material = null;
                    ItemStack item = null;
                    String type = Home.plugin.getManager().getRecipe().getString(id + "." + c + ".Type");
                    String itemPath = Home.plugin.getManager().getRecipe().getString(id + "." + c + ".Item");
                    if (type == null) continue;
                    else if (type.equalsIgnoreCase("Material")) {
                        material = Material.valueOf(itemPath);
                    } else if (type.equalsIgnoreCase("Item")) {
                        AddSubmitNumItem.ItemData itemData = AddSubmitNumItem.getItemData(itemPath);
                        if (itemData != null) {
                            item = itemData.getItem();
                        }
                    }
                    if (material != null) {
                        recipe.setIngredient(c, material);
                    } else if (item != null) {
                        recipe.setIngredient(c, new RecipeChoice.ExactChoice(item));
                    }
                }
                Bukkit.addRecipe(recipe);
            } else if (craft.equalsIgnoreCase("FURNACE")) {
                Material material = null;
                ItemStack item = null;
                String type = Home.plugin.getManager().getRecipe().getString(id + ".Type");
                if (type.equalsIgnoreCase("Material")) {
                    material = Material.valueOf(Home.plugin.getManager().getRecipe().getString(id + ".Item"));
                } else if (type.equalsIgnoreCase("Item")) {
                    AddSubmitNumItem.ItemData itemData = AddSubmitNumItem.getItemData(Home.plugin.getManager().getRecipe().getString(id + ".Item"));
                    item = itemData.getItem();
                }
                FurnaceRecipe recipe = null;
                if (material != null) {
                    int cookingTime = Home.plugin.getManager().getRecipe().getInt(id + ".CookingTime") * 20;
                    float exp = Home.plugin.getManager().getRecipe().getInt(id + ".exp");
                    recipe = new FurnaceRecipe(key, data.getItem(), material, exp, cookingTime);
                } else if (item != null) {
                    int cookingTime = Home.plugin.getManager().getRecipe().getInt(id + ".CookingTime") * 20;
                    float exp = Home.plugin.getManager().getRecipe().getInt(id + ".exp");
                    recipe = new FurnaceRecipe(key, data.getItem(), new RecipeChoice.ExactChoice(item), exp, cookingTime);
                }
                if (recipe != null){
                    Bukkit.addRecipe(recipe);
                }

            }
        }
    }

    public void removeRecipe() {
        for (NamespacedKey key : keys) {
            Bukkit.removeRecipe(key);
        }
    }
}
