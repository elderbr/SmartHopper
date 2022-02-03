package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryCustom {

    private Inventory inventory;
    private String name;
    private ItemStack itemStack;
    private Item item;
    private Pocao potion;


    public void create(Object name) {
        if (name instanceof List) {

        }
        this.name = Msg.Color("$8$lGrupo: $r" + name);
        inventory = Bukkit.createInventory(null, 54, this.name);
    }

    public void createNewGrupo(String name) {
        this.name = Msg.Color("$5$lGrupo Novo: $r" + name);
        inventory = Bukkit.createInventory(null, 54, this.name);
    }

    public void createSmartHopper() {
        inventory = Bukkit.createInventory(null, 54, Msg.Color("$6SmartHopper"));
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addItem(Material item) {
        inventory.addItem(new ItemStack(item));
    }

    public void addItem(ItemStack item) {
        potion = new Pocao();
        itemStack = new ItemStack(item);
        inventory.addItem(item);
    }


}
