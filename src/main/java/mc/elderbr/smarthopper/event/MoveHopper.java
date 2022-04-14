package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
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

    private Inventory inventory;
    private Inventory destination;

    private Block blockDown;

    private SmartHopper smartHopper;
    private List<Hopper> hopperList;

    private SmartHopper smartHopperDestino;

    private List<Boolean> bloqueia;

    public MoveHopper() {

    }

    @EventHandler
    public void moveItemHopper(InventoryMoveItemEvent event) {
        try {
            event.setCancelled(true);// Cancela o movimento do item

            // Item que está sendo transferido
            itemStack = event.getItem();
            item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getDsItem());

            // Pegando o inventorio onde está o item
            inventory = event.getSource();

            // Destino do item
            destination = event.getDestination();// Inventorio de destino
            smartHopperDestino = new SmartHopper(destination);

            // Pega o bloco de baixo de onde o item está
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (blockDown.getState().getType() == Material.HOPPER) {// Verifica se o bloco de baixo é um hopper
                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {

                    smartHopper = new SmartHopper(hoppers);

                    // VERIFICA SE EXISTE MAIS DE UM ITEM OU GRUPO CONFIGURADO PARA O MESMO FUNIL E SE FOI SETADO PARA BLOQUEIA
                    if (smartHopper.getNameHopper().contains(";") && smartHopper.getNameHopper().contains("#")) {
                        String[] lista = smartHopper.getNameHopper().split(";");
                        bloqueia = new ArrayList<>();
                        for (String values : lista) {

                            SmartHopper smart = new SmartHopper(hoppers.getBlock(), values);

                            if (smart.getNameHopper().contains("#")) {
                                if (smart.getType() instanceof Item items) {
                                    if (items.getCdItem() == item.getCdItem()) {
                                        bloqueia.add(true);
                                    }
                                }
                                if (smart.getType() instanceof Grupo grupo) {
                                    if (grupo.contentsItem(item)) {
                                        bloqueia.add(true);
                                    }
                                }
                            }
                        }
                        if (bloqueia.contains(true)) {
                            event.setCancelled(true);
                        } else {
                            event.setCancelled(false);
                        }
                    } else if (smartHopper.getNameHopper().contains(";")) {// VERIFICA SE EXISTE MAIS DE UM ITEM OU GRUPO CONFIGURADO PARA O MESMO FUNIL
                        String[] lista = smartHopper.getNameHopper().split(";");
                        for (String values : lista) {

                            SmartHopper smart = new SmartHopper(hoppers.getBlock(), values);

                            if (smart.getType() instanceof Item items) {
                                if (items.getCdItem() == item.getCdItem()) {
                                    event.setCancelled(false);
                                }
                            }
                            if (smart.getType() instanceof Grupo grupo) {
                                if (grupo.contentsItem(item)) {
                                    event.setCancelled(false);
                                }
                            }
                        }
                    } else {

                        if (smartHopper.getType() instanceof Item itemSmart) {
                            if (smartHopper.getNameHopper().contains("#")) {
                                if (item.getCdItem() == itemSmart.getCdItem()) {
                                    event.setCancelled(true);
                                    return;
                                }
                                event.setCancelled(false);
                                return;
                            } else if (item.getCdItem() == itemSmart.getCdItem()) {
                                event.setCancelled(false);
                                return;
                            }
                        }

                        if (smartHopper.getType() instanceof Grupo grupoSmart) {
                            if (smartHopper.getNameHopper().contains("#")) {
                                if (grupoSmart.contentsItem(item)) {
                                    event.setCancelled(true);
                                }else {
                                    event.setCancelled(false);
                                }
                            } else {
                                if (grupoSmart.contentsItem(item)) {
                                    event.setCancelled(false);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if (destination.getType() == InventoryType.HOPPER && smartHopperDestino.getNameHopper().equalsIgnoreCase("HOPPER")) {
                event.setCancelled(false);// Ativa o movimento do item
            }
            if (destination.getType() != InventoryType.HOPPER) {
                event.setCancelled(false);// Ativa o movimento do item
            }
        } catch (
                Exception e) {
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
