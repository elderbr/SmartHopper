package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.interfaces.IItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Grupo implements IItem, Cloneable {

    private int id;
    private String name;
    private boolean blocked;
    private Map<String, String> translation = new HashMap<>();
    private List<Item> items = new ArrayList<>();

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

    public List<Item> getItems() {
        return items;
    }

    public List<String> getItemsNames() {
        List<String> list = items.stream().map(Item::getName).collect(Collectors.toList());
        Collections.sort(list);
        return list;
    }

    public Grupo addItems(Item item) {
        items.add(item);
        return this;
    }

    public Grupo addItems(ItemStack itemStack) {
        items.add(new ItemController().findByItemStack(itemStack));
        return this;
    }

    public Grupo addItems(Material material) {
        items.add(new ItemController().findByItemStack(new ItemStack(material)));
        return this;
    }

    public Grupo removeItems(String name) {
        items.remove(name);
        return this;
    }

    public Grupo removeItems(ItemStack itemStack) {
        items.remove(Item.TO_ItemStack(itemStack));
        return this;
    }

    public boolean containsItem(Item item) {
        for (Item value : items) {
            if (value.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsItem(ItemStack itemStack) {
        for (Item item : items) {
            if (Item.TO_ItemStack(itemStack).equalsIgnoreCase(item.getName())) {
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