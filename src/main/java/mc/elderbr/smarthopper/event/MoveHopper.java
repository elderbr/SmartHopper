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

    private Inventory inventoryInicial;
    private Inventory inventory;
    private Inventory destination;

    private Block blockDown;

    private SmartHopper smartHopper;
    private List<Hopper> hopperList;

    private SmartHopper smartHopperDestino;
    private boolean isBloqueia;

    public MoveHopper() {

    }

    @EventHandler
    public void moveItemHopper(InventoryMoveItemEvent event) {
        try {

            // Item que está sendo transferido
            itemStack = event.getItem();
            item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getName());

            // Pegando o inventorio onde está o item
            inventory = event.getSource();

            // Primeiro inventario que o item vai
            inventoryInicial = event.getInitiator();

            // Destino do item
            destination = event.getDestination();// Inventorio de destino
            smartHopperDestino = new SmartHopper(destination);

            // Pega o bloco de baixo de onde o item está
            if (inventory.getType() == InventoryType.CHEST) {
                blockDown = inventoryInicial.getLocation().getBlock().getRelative(BlockFace.DOWN);
            } else {
                blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);
            }

            if (blockDown.getState().getType() == Material.HOPPER) {// Verifica se o bloco de baixo é um hopper

                event.setCancelled(true);// Cancela o movimento do item

                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {
                    smartHopper = new SmartHopper(hoppers);

                    if (smartHopper.isBloqueado()) {
                        event.setCancelled(smartHopper.isCancelled(item));
                        break;
                    }
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
                if (smartHopperDestino.getName().equals("hopper")) {
                    event.setCancelled(false);
                }
                event.setCancelled(false);// Ativa o movimento do item
            }

        } catch (Exception e) {
            event.setCancelled(false);
            Msg.ServidorErro("Erro ao movimentar o item", "", getClass(), e);
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