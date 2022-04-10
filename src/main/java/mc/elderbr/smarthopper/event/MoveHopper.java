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
    private SmartHopper smartHopperAux;
    private List<Hopper> hopperList;
    private List<Boolean> isBloqueio;


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

            // Pega o bloco de baixo de onde o item está
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (blockDown.getState().getType() == Material.HOPPER) {// Verifica se o bloco de baixo é um hopper
                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {
                    smartHopper = new SmartHopper(hoppers);

                    String[] lista = smartHopper.getNameHopper().split(";");
                    for (String values : lista) {
                        SmartHopper smart = new SmartHopper(hoppers);
                        if (smart.getType() instanceof Item items) {
                            if (items.getDsItem().equalsIgnoreCase(item.getDsItem())) {
                                event.setCancelled(false);
                                return;
                            }
                        }
                        if (smart.getType() instanceof Grupo grupo) {
                            if (grupo.getListItem().contains(item)) {
                                event.setCancelled(false);
                                return;
                            }
                        }
                    }


                    if (smartHopper.getType() instanceof Item itemSmart) {
                        Msg.ServidorGreen("item negado >> "+ smartHopper.getNameHopper()+" - igual >> "+ (item.getCdItem() == itemSmart.getCdItem()), getClass());
                        if (smartHopper.getNameHopper().contains("#")) {
                            if (item.getCdItem() == itemSmart.getCdItem()) {
                                event.setCancelled(true);
                                return;
                            }
                            event.setCancelled(false);
                            return;
                        }
                        if (item.getCdItem() == itemSmart.getCdItem()) {
                            event.setCancelled(false);
                            return;
                        }
                    }
                    if (smartHopper.getType() instanceof Grupo grupoSmart) {
                        if (smartHopper.getNameHopper().contains("#")) {
                            if (grupoSmart.contentItem(item)) {
                                event.setCancelled(true);
                                return;
                            }
                            event.setCancelled(false);
                            return;
                        }
                        if (grupoSmart.contentItem(item)) {
                            event.setCancelled(false);
                            return;
                        }
                    }
                }
            }
            if (destination.getType() == InventoryType.HOPPER && new SmartHopper(destination).toSmartHopper().equalsIgnoreCase("HOPPER")) {
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
