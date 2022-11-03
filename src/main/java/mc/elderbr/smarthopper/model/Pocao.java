package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_NAME;
import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_NAME_LIST;

public class Pocao {


    private String name;
    private String type;
    private ItemStack itemStack;

    public Pocao() {

    }

    public Pocao(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Pocao(@NotNull String name) {
        this.name = name;
        parseItemStack();
    }

    public static void Create() {
        // POÇÕES E SEU EFEITOS
        for (PotionType potion : PotionType.values()) {
            String name = "potion " + potion.name().replaceAll("_", " ").toLowerCase();
            if (!ITEM_NAME_LIST.contains(name)) {
                ITEM_NAME_LIST.add(name);
            }
            if (!ITEM_NAME_LIST.contains("splash " + name)) {
                ITEM_NAME_LIST.add("splash " + name);
            }
            if (!ITEM_NAME_LIST.contains("lingering " + name)) {
                ITEM_NAME_LIST.add("lingering " + name);
            }
        }
    }

    public String getPotion() {
        switch (itemStack.getType()) {
            case POTION:
                type = "potion ";
                break;
            case SPLASH_POTION:
                type = "splash potion";
                break;
            case LINGERING_POTION:
                type = "lingering potion";
                break;
            default:
                return null;
        }
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        name = type + " " + potionMeta.getBasePotionData().getType().name().replaceAll("_", " ").toLowerCase();
        return name;
    }

    public Item getItem() {
        return ITEM_MAP_NAME.get(getPotion());
    }

    public ItemStack parseItemStack() throws ItemException {
        if(name.contains("lingering")){
            itemStack = new ItemStack(Material.LINGERING_POTION);
            type = "lingering potion";
        }else if(name.contains("splash")){
            itemStack = new ItemStack(Material.SPLASH_POTION);
            type = "splash potion";
        }else {
            itemStack = new ItemStack(Material.POTION);
            type = "potion";
        }

        String potion = name.replaceAll("potion", "")
                .replaceAll("splash", "")
                .replaceAll("lingering", "").trim()
                .replaceAll("\\s", "_").toUpperCase();

        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.valueOf(potion)));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
