package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MoveHopper implements Listener {

    private ItemStack itemStack;

    private Item item;
    private IItem itemSmart;

    private Inventory inventoryInicial;
    private Inventory inventory;
    private Inventory destination;

    private Block blockDown;

    private SmartHopper smartHopper;
    private Hopper hopper;

    private ItemController itemController = new ItemController();

    public MoveHopper() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void moveItemHopper(InventoryMoveItemEvent event) {

        try {

            // Item que está sendo transferido
            itemStack = event.getItem();
            item = itemController.getItem(itemStack);

            // Pegando o inventorio onde está o item
            inventory = event.getSource();

            // Primeiro inventario que o item vai
            inventoryInicial = event.getInitiator();

            // Destino do item
            destination = event.getDestination();// Inventorio de destino

            // Pega o bloco de baixo de onde o item está
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (destination.getType() == InventoryType.HOPPER) {
                event.setCancelled(true);// Cancela o movimento do item

                hopper = (Hopper) destination.getLocation().getBlock().getState();
                smartHopper = new SmartHopper(hopper);
                Object hopperObj = smartHopper.getType();

                if(hopperObj == null){
                    event.setCancelled(false);
                    return;
                }
                List<Hopper> hoppers = getBlockDownHoppers();
                if (!hoppers.isEmpty()) {
                    for (Hopper downHopper : hoppers) {
                        event.setCancelled(hopperEqualsItem(downHopper));
                    }
                } else if (hopperObj instanceof Item itemSM) {
                    if (Objects.equals(itemSM, item)) {
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                    }
                } else if (hopperObj instanceof Grupo grup) {
                    if (grup.containsItem(item)) {
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                    }
                } else if (hopperObj instanceof List<?> list) {
                    List<IItem> itemList = (List<IItem>) list;
                    for (IItem listItem : itemList) {
                        if (Objects.equals(listItem.getId(), item.getId())) {
                            event.setCancelled(false);
                        }
                    }
                }
            } else {
                event.setCancelled(false);// Cancela o movimento do item
            }
        } catch (Exception e) {
            event.setCancelled(false);
            if (e.getClass() == ItemException.class) {
                Msg.ServidorRed(e.getMessage());
            }
        }

    }

    private boolean hopperEqualsItem(Hopper hopper) {
        try {
            smartHopper = new SmartHopper(hopper);
            Object hopperObj = smartHopper.getType();
            if (hopperObj instanceof Item itemSM) {
                Msg.ServidorGreen("Item SM: " + itemSM.getName()+" - item: "+ item.getName(), getClass());
                if (Objects.equals(itemSM.getId(), item.getId())) {
                    return false;
                }
            } else if (hopperObj instanceof Grupo grup) {
                if (grup.containsItem(item)) {
                    return false;
                }
            } else if (hopperObj instanceof List<?> list) {
                List<IItem> itemList = (List<IItem>) list; // Cast seguro, pois todos são IItem
                // Agora você pode trabalhar com itemList como List<IItem>
                for (IItem listItem : itemList) {
                    if (Objects.equals(listItem, item)) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            Msg.ServidorRed(e.getMessage());
        }
        return true;
    }


    private List<Hopper> getBlockDownHoppers() {
        List<Hopper> list = new ArrayList<>();
        while (blockDown.getType() == Material.HOPPER) {
            list.add((Hopper) blockDown.getState());
            blockDown = blockDown.getRelative(BlockFace.DOWN);
        }
        return list;
    }
}