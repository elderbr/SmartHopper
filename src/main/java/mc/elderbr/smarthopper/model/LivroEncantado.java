package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class LivroEncantado implements VGlobal {
    private static final String Livro = "enchanted book ";
    private final ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

    public LivroEncantado() {
    }

    public static void Create() {
        for (Enchantment enchantment : Enchantment.values()) {
            String name = Livro + enchantment.getKey().getKey().replaceAll("_", " ");
            for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                String nameEnchantment = name.concat(" " + i);
                if (ITEM_NAME_LIST_DEFAULT.contains(nameEnchantment)) {
                    ITEM_NAME_LIST_DEFAULT.add(nameEnchantment);
                }
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                meta.addStoredEnchant(enchantment, i, true);
                itemStack.setItemMeta(meta);
                ENCHANTEMENT_BOOK_MAP.put(nameEnchantment, itemStack);
            }
        }
    }

    public static ItemStack getItemStack(String name){
        return ENCHANTEMENT_BOOK_MAP.get(name);
    }

    public static Item getItem(ItemStack itemStack) throws ItemException {
        if(itemStack.getType() == Material.ENCHANTED_BOOK){
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            Map<Enchantment, Integer> store = meta.getStoredEnchants();
            Enchantment enchantment = store.keySet().iterator().next();
            int nivel = store.values().iterator().next();
            String name = Livro + enchantment.getKey().getKey().toLowerCase().replaceAll("_"," ")+" "+nivel;
            Item item = ITEM_MAP_NAME.get(name);
            if (item==null){
                throw new ItemException("Item não existe!!!");
            }
            return item;
        }
        throw new ItemException("Item não existe!!!");
    }

    public static Item getItem(String name) throws ItemException {
        if(name.isBlank()){
            throw new ItemException("Segure o item na mão ou digite o nome do item ou ID!!!");
        }
        Item item = ITEM_MAP_NAME.get(name);
        if(item == null){
            throw new ItemException(String.format("O item %s não existe!!!", name));
        }
        return item;
    }
}
