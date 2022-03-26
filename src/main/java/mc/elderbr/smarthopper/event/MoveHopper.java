package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Pocao;
import mc.elderbr.smarthopper.model.SmartHopper;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MoveHopper implements Listener {

    private ItemStack itemStack;

    private Item item;
    private Pocao potion;

    private Inventory initiator;
    private Inventory inventory;
    private Inventory destination;

    private Block blockDown;

    private SmartHopper smartHopper;
    private SmartHopper smartHopperAux;
    private List<Hopper> hopperList;
    private List<Boolean> isBloqueio;


    public MoveHopper() {

    }

    @EventHandler
    public void moveItemHopper(InventoryMoveItemEvent event) {

        destination = event.getDestination();
        inventory = event.getSource();
        itemStack = event.getItem();
        item = new Item(itemStack);

        if (inventory.getType() == InventoryType.HOPPER) {
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (blockDown.getType() == Material.HOPPER) {
                event.setCancelled(true);
                isBlockDownHopper();
                for (Hopper hoppers : hopperList) {
                    smartHopper = new SmartHopper(hoppers);
                    if(smartHopper.equals(item.getName())){
                        event.setCancelled(false);
                    }
                }
            }
            smartHopper = new SmartHopper(destination);
            if(smartHopper.getName().equals("chest") || smartHopper.getName().equals("hopper")){
                event.setCancelled(false);
            }

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
