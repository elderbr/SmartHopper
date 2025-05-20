package mc.elderbr.smarthopper.recipes;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HopperRecipe {

    private static ShapedRecipe recipe;
    public static ItemStack SmartHopper = new ItemStack(Material.HOPPER);
    private static ItemMeta meta;
    private static List<String> lore = new ArrayList<>();

    public HopperRecipe() {

    }

    public static ShapedRecipe createSmartHopper() {
        SmartHopper = new ItemStack(Material.HOPPER);
        meta = SmartHopper.getItemMeta();
        meta.setDisplayName("§aSmart Hopper");
        meta.setLore(Arrays.asList(new String[]{"§f§lSmart Hopper"}));
        SmartHopper.setItemMeta(meta);
        recipe = new ShapedRecipe(new NamespacedKey(VGlobal.SMARTHOPPER, "smart_hopper"), SmartHopper);
        recipe.shape(new String[]{"%B%", "%C%", " % "});
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('%', Material.IRON_INGOT);
        return recipe;
    }

}
