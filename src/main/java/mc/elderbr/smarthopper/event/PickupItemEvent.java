package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PickupItemEvent implements Listener {

    private Inventory inventory;
    private ItemStack itemStack;
    private Item item;
    private ItemController itemController = new ItemController();
    private SmartHopper smartHopper;

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        try {
            itemStack = event.getItem().getItemStack();// Item que está sendo transferido
            item = itemController.findByItemStack(itemStack);
            inventory = event.getInventory();
            if (inventory.getType() == InventoryType.HOPPER) {
                event.setCancelled(true);
                // Verifica se o holder é um carrinho com funil (HopperMinecart)
                if (inventory.getHolder() instanceof HopperMinecart minecart) {
                    smartHopper = new SmartHopper(minecart);
                }else if(inventory.getHolder() instanceof Hopper hopper) {
                    smartHopper = new SmartHopper(hopper);
                }else{
                    event.setCancelled(false);
                    return;
                }

                if (smartHopper.getTypes().isEmpty()) {
                    event.setCancelled(false);
                    return;
                }
                boolean canceled = true;
                for (IItem hopper : smartHopper.getTypes()) {
                    if (hopper instanceof Item itemSM) {
                        if (itemSM.isBlocked()) {
                            if (Objects.equals(itemSM.getId(), item.getId())) {
                                canceled = true;
                            } else {
                                canceled = false;
                            }
                        } else if (Objects.equals(itemSM.getId(), item.getId())) {
                            canceled = false;
                        }
                    } else if (hopper instanceof Grupo grup) {
                        if (grup.isBlocked()) {
                            if (grup.containsItem(item)) {
                                canceled = true;
                            } else {
                                canceled = false;
                            }
                        } else if (grup.containsItem(item)) {
                            canceled = false;
                        }
                    }
                }
                event.setCancelled(canceled);
            }
        } catch (Exception e) {
            Msg.ServidorErro(e, e.getMessage(), getClass());
            Msg.ServidorRed("Local do erro: " + inventory.getLocation().getBlock());
            event.setCancelled(false);
        }
    }
}
