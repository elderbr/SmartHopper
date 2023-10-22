package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Collections;
import java.util.List;

public class ItemCreate implements VGlobal {
    private ItemCreate() {
    }

    public static List<String> Create() {
        String name = null;
        for (Material material : Material.values()) {
            ItemStack itemStack = new ItemStack(material);
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType().isItem()) {

                name = Item.ToName(itemStack).toLowerCase();

                // Se item não pode está na lista
                switch (material) {
                    case POTION:
                    case SPLASH_POTION:
                    case LINGERING_POTION:
                    case ENCHANTED_BOOK:
                        continue;
                }
                if(!ITEM_NAME_LIST_DEFAULT.contains(name)) {
                    ITEM_NAME_LIST_DEFAULT.add(name);
                }
            }
        }

        // Criando item de poções
        for (PotionType potion : PotionType.values()) {
            name = "potion " + potion.name().replaceAll("_", " ").toLowerCase();
            for(int i = 1; i <= potion.getMaxLevel(); i++) {
                String potionName = name.concat(" "+ i);

                if (!ITEM_NAME_LIST_DEFAULT.contains(potionName)) {
                    ITEM_NAME_LIST_DEFAULT.add(potionName);
                }
                if (!ITEM_NAME_LIST_DEFAULT.contains("splash " + potionName)) {
                    ITEM_NAME_LIST_DEFAULT.add("splash " + potionName);
                }
                if (!ITEM_NAME_LIST_DEFAULT.contains("lingering " + potionName)) {
                    ITEM_NAME_LIST_DEFAULT.add("lingering " + potionName);
                }
            }
        }

        // Criando livros encantados
        for (Enchantment enchantment : Enchantment.values()) {
            name = "enchanted book " + enchantment.getKey().getKey().replaceAll("_", " ");
            for(int i = 1; i <= enchantment.getMaxLevel(); i++){
                if (!ITEM_NAME_LIST_DEFAULT.contains(name.concat(" "+ i))) {
                    ITEM_NAME_LIST_DEFAULT.add(name.concat(" "+ i));
                }
            }
        }
        Collections.sort(ITEM_NAME_LIST_DEFAULT);
        return ITEM_NAME_LIST_DEFAULT;
    }
}
