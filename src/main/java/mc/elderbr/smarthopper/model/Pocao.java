package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Pocao implements VGlobal {

    public Pocao() {

    }

    public static void Create() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = null;

        // POÇÕES E SEU EFEITOS
        for (PotionType potion : PotionType.values()) {
            String name = "potion " + potion.name().replaceAll("_", " ").toLowerCase();
            if (!ITEM_NAME_LIST_DEFAULT.contains(name)) {
                ITEM_NAME_LIST_DEFAULT.add(name);
            }
            itemStack = new ItemStack(Material.POTION);
            meta = (PotionMeta) itemStack.getItemMeta();
            meta.setBasePotionData(new PotionData(potion));
            itemStack.setItemMeta(meta);
            POTION_MAP.put(name, itemStack);

            if (!ITEM_NAME_LIST_DEFAULT.contains("splash " + name)) {
                ITEM_NAME_LIST_DEFAULT.add("splash " + name);
            }
            itemStack = new ItemStack(Material.SPLASH_POTION);
            meta = (PotionMeta) itemStack.getItemMeta();
            meta.setBasePotionData(new PotionData(potion));
            itemStack.setItemMeta(meta);
            POTION_MAP.put("splash " + name, itemStack);

            if (!ITEM_NAME_LIST_DEFAULT.contains("lingering " + name)) {
                ITEM_NAME_LIST_DEFAULT.add("lingering " + name);
            }
            itemStack = new ItemStack(Material.LINGERING_POTION);
            meta = (PotionMeta) itemStack.getItemMeta();
            meta.setBasePotionData(new PotionData(potion));
            itemStack.setItemMeta(meta);
            POTION_MAP.put("lingering " + name, itemStack);
        }
    }

    public static Item getItem(ItemStack itemStack) throws ItemException {
        String name = null;
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        String potion = meta.getBasePotionData().getType().name().replaceAll("_", " ").toLowerCase();
        switch (itemStack.getType()) {
            case SPLASH_POTION:
                name = "splash potion " + potion;
                break;
            case LINGERING_POTION:
                name = "lingering potion " + potion;
                break;
            default:
                name = "potion " + potion;
        }
        Item item = ITEM_MAP_NAME.get(name);
        if (item == null) {
            throw new ItemException("O item não existe!!!");
        }
        return item;
    }

    public static ItemStack getItemStack(String name) {
        return POTION_MAP.get(name);
    }
}
