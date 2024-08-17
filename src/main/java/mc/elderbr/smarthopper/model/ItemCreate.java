package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemCreate implements VGlobal {
    private ItemCreate() {
    }

    public static List<String> Create() {
        for (Material material : Material.values()) {

            ItemStack itemStack = new ItemStack(material);

            if (!itemStack.getType().isItem() || itemStack.getType().isAir()) continue;

            // Se item não pode está na lista
            switch (material) {
                case POTION:
                case SPLASH_POTION:
                case LINGERING_POTION:
                case ENCHANTED_BOOK:
                    continue;
            }
            ITEM_NAME_LIST_DEFAULT.add(Item.TO_ItemStack(itemStack));
        }
        // Criando livros encantados
        LivroEncantado.Create();
        // Criando Poções
        Pocao.Create();
        return ITEM_NAME_LIST_DEFAULT;
    }
}
