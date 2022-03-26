package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Item extends Traducao {

    private int cdItem = 0;
    private String dsItem;

    public Item() {
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

    public List<Item> create_item() {
        List<Item> list = new ArrayList<>();
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack != null) {
                Item item = new Item().setItemName(itemStack);
                list.add(item);
                if (!VGlobal.ITEM_NAME_LIST.contains(item.getDsItem())) {
                    VGlobal.ITEM_NAME_LIST.add(item.getDsItem());
                }
            }
        }
        return list;
    }

    public Item setItemName(ItemStack itemStack) {
        dsItem = itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();
        return this;
    }

    public ItemStack parseItemStack(Item item){
        return new ItemStack(Material.getMaterial(item.getDsItem().replaceAll("\\s","_").toUpperCase()));
    }
    public ItemStack getItemStack(){
        return new ItemStack(Material.getMaterial(dsItem.replaceAll("\\s","_").toUpperCase()));
    }

}
