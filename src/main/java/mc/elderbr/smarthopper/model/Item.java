package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Item extends Traducao {

    private int cdItem = 0;
    private String dsItem;

    public Item() {
    }
    public Item(String name) {
        dsItem = name;
    }
    public Item(ItemStack itemStack) {
        parseItem(itemStack);
    }

    public int getCdItem() {
        return cdItem;
    }

    public void setCdItem(int cdItem) {
        this.cdItem = cdItem;
    }

    public String getDsItem() {
        return dsItem;
    }

    public void setDsItem(String dsItem) {
        this.dsItem = dsItem;
    }

    public static List<Item> CreateItem() {
        List<Item> list = new ArrayList<>();
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack != null) {
                Item item = new Item().parseItem(itemStack);
                list.add(item);
                if (!VGlobal.ITEM_NAME_LIST.contains(item.getDsItem())) {
                    VGlobal.ITEM_NAME_LIST.add(item.getDsItem());
                }
            }
        }
        // POÇÕES E SEU EFEITOS
        for (PotionType potion : PotionType.values()) {
            String name = potion.name().replaceAll("_", " ").toLowerCase();
            if(!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add(name);
            }
            if(!VGlobal.ITEM_NAME_LIST.contains("splash " + name)) {
                VGlobal.ITEM_NAME_LIST.add("splash " + name);
            }
            if(!VGlobal.ITEM_NAME_LIST.contains("lingering " + name)) {
                VGlobal.ITEM_NAME_LIST.add("lingering " + name);
            }
        }
        // LIVRO ENCANTADOS
        for (Enchantment enchantment : Enchantment.values()) {
            String name = enchantment.getKey().getKey().replaceAll("_", " ");
            if (!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add(name);
            }
        }
        Collections.sort(VGlobal.ITEM_NAME_LIST);
        return list;
    }

    public Item parseItem(@NotNull ItemStack itemStack) {
        dsItem = itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();
        return this;
    }
    
    public Item parseItem(@NotNull Material material){
        dsItem = material.getKey().getKey().replaceAll("_"," ").toLowerCase();
        return this;
    }

    public ItemStack parseItemStack(@NotNull Item item){
        return new ItemStack(Material.getMaterial(item.getDsItem().replaceAll("\\s","_").toUpperCase()));
    }
    public ItemStack getItemStack(){
        return new ItemStack(Material.getMaterial(dsItem.replaceAll("\\s","_").toUpperCase()));
    }

}
