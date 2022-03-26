package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
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


    public void create(Grupo grupo) {
        this.name = VGlobal.GRUPO_INVENTORY + grupo.toString()+" §8§lID: §r"+grupo.getCdGrupo();
        inventory = Bukkit.createInventory(null, 54, this.name);
    }

    public void createNewGrupo(String name) {
        this.name = VGlobal.GRUPO_NOVO_INVENTORY + name;
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

    public void addItem(Item item) {
        inventory.addItem(new ItemStack(Utils.ParseItemStack(item.getName())));
    }

    public void addItem(ItemStack item) {
        potion = new Pocao();
        itemStack = new ItemStack(item);
        inventory.addItem(item);
    }


}
