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

            if (!itemStack.getType().isItem() || itemStack.getType().isAir()) continue;

            name = Item.TO_ItemStack(itemStack).toLowerCase();

            // Se item não pode está na lista
            switch (material) {
                case POTION:
                case SPLASH_POTION:
                case LINGERING_POTION:
                case ENCHANTED_BOOK:
                    continue;
            }
            if (!ITEM_NAME_LIST_DEFAULT.contains(name)) {
                ITEM_NAME_LIST_DEFAULT.add(name);
            }
        }
        // Criando livros encantados
        LivroEncantado.Create();
        // Criando Poções
        Pocao.Create();
        // Organizando em ordem alfabetica
        Collections.sort(ITEM_NAME_LIST_DEFAULT);
        return ITEM_NAME_LIST_DEFAULT;
    }
}
