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

            // Destino do item
            destination = event.getDestination();// Inventorio de destino

            // Pega o bloco de baixo de onde o item está
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (destination.getType() == InventoryType.HOPPER) {
                event.setCancelled(true);// Cancela o movimento do item
                hopper = (Hopper) destination.getLocation().getBlock().getState();
                smartHopper = new SmartHopper(hopper);
                if (smartHopper.getTypes().isEmpty()) {
                    event.setCancelled(false);
                    return;
                }
                List<Hopper> hoppers = getBlockDownHoppers();
                if (!hoppers.isEmpty()) {
                    boolean cancelled = true;
                    for (Hopper downHopper : hoppers) {
                        smartHopper = new SmartHopper(downHopper);
                        List<IItem> smartHopperListTypes = smartHopper.getTypes();
                        if (smartHopperListTypes.isEmpty()) {
                            cancelled = false;
                            break;
                        } else {
                            for (IItem itemDown : smartHopperListTypes) {
                                if (itemDown instanceof Item itemSM) {
                                    boolean itemContains = Objects.equals(itemSM.getId(), item.getId());
                                    if (itemSM.isBlocked()) {
                                        if (!itemContains) {
                                            cancelled = false;
                                        }else{
                                            cancelled = true;
                                            break;
                                        }
                                    } else if (itemContains) {
                                        cancelled = false;
                                        break;
                                    }
                                } else if (itemDown instanceof Grupo grup) {
                                    boolean grupContains = grup.containsItem(item);
                                    if (grup.isBlocked()) {
                                        if (!grupContains) {
                                            cancelled = false;
                                        }else{
                                            cancelled = true;
                                            break;
                                        }
                                    } else if (grupContains) {
                                        cancelled = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    event.setCancelled(cancelled);
                } else {
                    event.setCancelled(false);
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

    private List<Hopper> getBlockDownHoppers() {
        List<Hopper> list = new ArrayList<>();
        while (blockDown.getType() == Material.HOPPER) {
            list.add((Hopper) blockDown.getState());
            blockDown = blockDown.getRelative(BlockFace.DOWN);
        }
        return list;
    }
}