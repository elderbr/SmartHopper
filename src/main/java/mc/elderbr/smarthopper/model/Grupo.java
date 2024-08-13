package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.IItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_NAME;

public class Grupo implements IItem, Cloneable {

    private int id;
    private String name;
    private boolean blocked;
    private Map<String, String> translation = new HashMap<>();
    private List<String> items = new ArrayList<>();

    public Grupo() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Grupo setId(Integer code) {
        id = code;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Grupo setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public Grupo setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    @Override
    public Map<String, String> getTranslations() {
        return translation;
    }

    public List<String> getItems() {
        return items;
    }

    public Grupo addItems(String name){
        items.add(name);
        return this;
    }

    public Grupo addItems(ItemStack itemStack){
        items.add(Item.TO_ItemStack(itemStack));
        return this;
    }

    public Grupo removeItems(String name){
        items.remove(name);
        return this;
    }

    public Grupo removeItems(ItemStack itemStack){
        items.remove(Item.TO_ItemStack(itemStack));
        return this;
    }

    public boolean containsItem(Item item){
        for(String name : items){
            if(item.getName().equalsIgnoreCase(name.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean containsItem(ItemStack itemStack){
        for(String name : items){
            if(Item.TO_ItemStack(itemStack).equalsIgnoreCase(name.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Grupo clone() throws CloneNotSupportedException {
        Grupo clone = (Grupo) super.clone();
        clone.items = new ArrayList<>(items);
        return clone;
    }
}