package mc.elderbr.smarthopper.factories;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemFactory implements VGlobal {

    public static Item vazio() {
        Item item = new Item();
        item.setId(0);
        item.setName("vazio");
        return item;
    }

    public static ItemStack getIronHopper() {
        ItemStack item = new ItemStack(Material.HOPPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TITLE_RECIPE);
        meta.setLore(List.of(NAME_RECIPE));
        meta.setCustomModelData(14);
        item.setItemMeta(meta);
        return item;
    }
}
