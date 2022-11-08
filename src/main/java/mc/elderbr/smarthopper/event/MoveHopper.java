package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.exceptions.ItemException;
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
    private ItemController itemController;

    public MoveHopper() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void moveItemHopper(InventoryMoveItemEvent event) {

        try {

            // Item que está sendo transferido
            itemStack = event.getItem();
            itemController = new ItemController();
            item = itemController.getItem(itemStack);

            // Pegando o inventorio onde está o item
            inventory = event.getSource();

            // Primeiro inventario que o item vai
            inventoryInicial = event.getInitiator();

            // Destino do item
            destination = event.getDestination();// Inventorio de destino
            smartHopperDestino = new SmartHopper(destination);

            // Pega o bloco de baixo de onde o item está
            blockDown = destination.getLocation().getBlock();

            if (destination.getType() == InventoryType.HOPPER) {

                event.setCancelled(true);// Cancela o movimento do item
                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {
                    smartHopper = new SmartHopper(hoppers);

                    // LISTA DE DE ITEM OU GRUPO
                    if (smartHopper.getType() instanceof List listaFunil) {
                        boolean cancelled = false;
                        boolean exist = false;
                        for (Object objFunil : listaFunil) {
                            // Se o hopper for igual ao item
                            if (objFunil instanceof Item itemSmart) {
                                if (itemSmart.isBloqueado()) {
                                    exist = true;
                                    if (itemSmart.getCodigo() == item.getCodigo()) {
                                        cancelled = true;
                                    }
                                } else if (itemSmart.getCodigo() == item.getCodigo()) {
                                    event.setCancelled(false);
                                }
                            }
                            // Se o hopper for igual ao grupo
                            if (objFunil instanceof Grupo grupoSmart) {
                                if (grupoSmart.isBloqueado()) {
                                    exist = true;
                                    if (grupoSmart.isContains(item)) {
                                        cancelled = true;
                                    }
                                } else if (grupoSmart.isContains(item)) {
                                    event.setCancelled(false);
                                }
                            }
                        }
                        if (exist && !cancelled) {
                            event.setCancelled(false);
                        }
                    }

                    // Se o hopper for igual ao item
                    if (smartHopper.getType() instanceof Item itemSmart) {
                        if (itemSmart.isBloqueado()) {
                            if (!itemSmart.equals(item)) {
                                event.setCancelled(false);
                            } else {
                                event.setCancelled(true);
                            }
                        } else if (itemSmart.equals(item)) {
                            event.setCancelled(false);
                        }
                    }

                    // Se o hopper for igual ao grupo
                    if (smartHopper.getType() instanceof Grupo grupoSmart) {
                        if (grupoSmart.isBloqueado()) {
                            if (!grupoSmart.isContains(item)) {
                                event.setCancelled(false);
                            } else {
                                event.setCancelled(true);
                            }
                        } else if (grupoSmart.isContains(item)) {
                            event.setCancelled(false);
                        }
                    }

                    if (smartHopper.getType() == null) {
                        event.setCancelled(false);
                    }
                }
            }

            if (destination.getType() != InventoryType.HOPPER) {
                event.setCancelled(false);
            }

        } catch (Exception e) {
            event.setCancelled(false);
            if(e.getClass() == ItemException.class) {
                Msg.ServidorRed(e.getMessage());
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