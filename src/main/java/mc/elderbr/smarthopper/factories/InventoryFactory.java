package mc.elderbr.smarthopper.factories;

import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryFactory implements Botao, VGlobal {

    public static InventoryFactory instance;

    private InventoryFactory() {
    }

    public static InventoryFactory getInstance() {
        if (Objects.isNull(instance)) {
            instance = new InventoryFactory();
        }
        return instance;
    }

    public Inventory InventoryConfigurationHopper(){
        Inventory inventory = Bukkit.createInventory(null, 18, NAME_RECIPE);
        for(int i = 10; i < 17; i++){
            inventory.setItem(i, BtnBlocked());
        }
        inventory.setItem(17, BtnSalva());
        return inventory;
    }
}
