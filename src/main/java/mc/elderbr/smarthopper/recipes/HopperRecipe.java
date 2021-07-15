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

    private ShapedRecipe recipe;
    public ItemStack SmartHopper = new ItemStack(Material.HOPPER);
    private ItemMeta meta;
    private List<String> lore = new ArrayList<>();

    public HopperRecipe() {

    }

    public ShapedRecipe createSmartHopper() {

        // ITEM PADRÃO DO SMARTHOPPER
        SmartHopper = new ItemStack(Material.HOPPER);
        meta = SmartHopper.getItemMeta();
        meta.setDisplayName("SmartHopper§e");
        meta.setLore(Arrays.asList("SmartHopper"));
        SmartHopper.setItemMeta(meta);

        recipe = new ShapedRecipe(new NamespacedKey(VGlobal.SMARTHOPPER, "hopper0"), SmartHopper);

        recipe.shape("%B%", "%C%", " % ");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('%', Material.IRON_INGOT);
        return recipe;
    }

    public ShapedRecipe createSmartHopper1() {

        // ITEM PADRÃO DO SMARTHOPPER
        SmartHopper = new ItemStack(Material.HOPPER);
        meta = SmartHopper.getItemMeta();
        meta.setDisplayName("SmartHopper§e");
        meta.setLore(Arrays.asList("SmartHopper"));
        SmartHopper.setItemMeta(meta);

        recipe = new ShapedRecipe(new NamespacedKey(VGlobal.SMARTHOPPER, "hopper1"), SmartHopper);
        recipe.shape("B", "H");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('H', Material.HOPPER);
        return recipe;
    }

    public ShapedRecipe createSmartHopper3() {

        // ITEM PADRÃO DO SMARTHOPPER
        SmartHopper = new ItemStack(Material.HOPPER, 3);
        meta = SmartHopper.getItemMeta();
        meta.setDisplayName("SmartHopper§e");
        meta.setLore(Arrays.asList("SmartHopper"));
        SmartHopper.setItemMeta(meta);

        recipe = new ShapedRecipe(new NamespacedKey(VGlobal.SMARTHOPPER, "hopper3"), SmartHopper);
        recipe.shape("BBB", "HHH");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('H', Material.HOPPER);
        return recipe;
    }

    private void createItem() {
        // ITEM PADRÃO DO SMARTHOPPER
        SmartHopper = new ItemStack(Material.HOPPER);
        meta = SmartHopper.getItemMeta();
        meta.setDisplayName("SmartHopper§e");
        meta.setLore(Arrays.asList("SmartHopper"));
        SmartHopper.setItemMeta(meta);
    }


}
