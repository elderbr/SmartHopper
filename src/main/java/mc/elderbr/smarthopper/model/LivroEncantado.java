package mc.elderbr.smarthopper.model;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_NAME;
import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_NAME_LIST;

public class LivroEncantado {

    private String name;
    private static final String Livro = "enchanted book ";
    private ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

    public LivroEncantado() {
    }

    public LivroEncantado(@NotNull String name) {
        this.name = name;
    }

    public LivroEncantado(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static void Create() {
        ItemStack itemStack = null;
        for (Enchantment enchantment : Enchantment.values()) {
            String name = Livro + enchantment.getKey().getKey().replaceAll("_", " ");
            if (!ITEM_NAME_LIST.contains(name)) {
                ITEM_NAME_LIST.add(name);
            }
        }
    }

    public String getBook() {
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            for (Enchantment key : meta.getStoredEnchants().keySet()) {
                return Livro + key.getKey().getKey().replaceAll("_", " ").toLowerCase();
            }
        }
        return null;
    }

    public ItemStack parseItemStack() {
        String book = name
                .replaceAll(Livro, "").trim()
                .replaceAll("\\s", "_").toUpperCase();
        itemStack = new ItemStack(Material.getMaterial(book));
        return itemStack;
    }

    public Item getItem() {
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            for (Enchantment key : meta.getStoredEnchants().keySet()) {
                name = Livro + key.getKey().getKey().replaceAll("_", " ").toLowerCase();
                break;
            }
        }
        return ITEM_MAP_NAME.get(name);
    }
}
