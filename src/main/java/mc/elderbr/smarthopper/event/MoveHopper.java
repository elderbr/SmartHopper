package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.SmartHopper;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveHopper implements Listener {

    private ItemStack itemStack;

    private Item item;
    private Item itemDestino;
    private Map<Integer, ItemStack> map;

    private Inventory inventoryInicial;
    private Inventory inventory;
    private Inventory destination;

    private Block blockDown;

    private SmartHopper smartHopper;
    private List<Hopper> hopperList;

    private SmartHopper smartHopperDestino;

    public MoveHopper() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void moveItemHopper(InventoryMoveItemEvent event) {
        try {

            // Item que está sendo transferido
            itemStack = event.getItem();
            item = Item.PARSE(itemStack);

            // Pegando o inventorio onde está o item
            inventory = event.getSource();

            map = new HashMap<>();
            for (int i = 0; i < inventory.getContents().length; i++) {
                map.put(i, inventory.getItem(i));
            }

            // Primeiro inventario que o item vai
            inventoryInicial = event.getInitiator();

            // Destino do item
            destination = event.getDestination();// Inventorio de destino
            smartHopperDestino = new SmartHopper(destination);

            // Pega o bloco de baixo de onde o item está
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (blockDown.getState().getType() == Material.HOPPER) {// Verifica se o bloco de baixo é um hopper
                event.setCancelled(true);// Cancela o movimento do item
                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {
                    smartHopper = new SmartHopper(hoppers);
                    if(!smartHopper.isCancelled(item)) {
                        event.setCancelled(false);
                        break;
                    }
                }
            }

            if (smartHopperDestino.getName().equals("hopper")) {
                event.setCancelled(false);
            }

            if (destination.getType() != InventoryType.HOPPER) {
                event.setCancelled(false);// Ativa o movimento do item
            }

        } catch (Exception e) {
            event.setCancelled(false);
        }

    }


    private void isBlockDownHopper() {
        hopperList = new ArrayList<>();
        while (blockDown.getType() == Material.HOPPER) {
            hopperList.add((Hopper) blockDown.getState());
            blockDown = blockDown.getRelative(BlockFace.DOWN);
        }
    }
}