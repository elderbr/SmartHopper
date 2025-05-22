package mc.elderbr.smarthopper.recipes;

import mc.elderbr.smarthopper.factories.ItemFactory;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HopperRecipe implements VGlobal {

    private static ShapedRecipe recipe;
    public static ItemStack hopper = new ItemStack(Material.HOPPER);
    private static ItemMeta meta;
    private static List<String> lore = new ArrayList<>();

    public HopperRecipe() {
    }

    public static ShapedRecipe createSmartHopper() {
        recipe = new ShapedRecipe(new NamespacedKey(SMARTHOPPER, "iron_smart_hopper"), ItemFactory.getIronHopper());
        recipe.setGroup("minecraft:redstone");
        recipe.shape(new String[]{"%B%", "%C%", " % "});
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('%', Material.IRON_INGOT);
        return recipe;
    }
}
