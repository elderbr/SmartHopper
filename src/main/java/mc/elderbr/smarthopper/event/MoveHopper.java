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
            event.setCancelled(true);// Cancela o movimento do item

            // Item que está sendo transferido
            itemStack = event.getItem();
            item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getDsItem());

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
                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {

                    smartHopper = new SmartHopper(hoppers);

                    // VERIFICA SE EXISTE MAIS DE UM ITEM OU GRUPO CONFIGURADO PARA O MESMO FUNIL E SE FOI SETADO PARA BLOQUEIA
                    if (smartHopper.getNameHopper().contains(";")) {
                        String[] lista = smartHopper.getNameHopper().split(";");
                        for (String values : lista) {

                            SmartHopper smart = new SmartHopper(hoppers.getBlock(), values);

                            if (smart.getNameHopper().contains("#")) {
                                isBloqueia = true;
                                if (smart.getType() instanceof Item items) {
                                    if (items.getCdItem() == item.getCdItem()) {
                                        isBloqueia = false;
                                        return;
                                    }
                                }
                                if (smart.getType() instanceof Grupo grupo) {
                                    if (grupo.contentsItem(item)) {
                                        isBloqueia = false;
                                        return;
                                    }
                                }
                                if (isBloqueia) {
                                    event.setCancelled(false);
                                }
                            }


                            if (smart.getType() instanceof Item items) {
                                if (items.getCdItem() == item.getCdItem()) {
                                    event.setCancelled(false);
                                    return;
                                }
                            }
                            if (smart.getType() instanceof Grupo grupo) {
                                if (grupo.contentsItem(item)) {
                                    event.setCancelled(false);
                                    return;
                                }
                            }
                        }
                    }

                    if (smartHopper.getType() instanceof Item itemSmart) {
                        if (smartHopper.getNameHopper().contains("#")) {
                            if (item.getCdItem() != itemSmart.getCdItem()) {
                                event.setCancelled(false);
                                return;
                            }
                        }
                        if (item.getCdItem() == itemSmart.getCdItem()) {
                            event.setCancelled(false);
                            return;
                        }
                    }

                    if (smartHopper.getType() instanceof Grupo grupoSmart) {
                        if (smartHopper.getNameHopper().contains("#")) {
                            if (!grupoSmart.contentsItem(item)) {
                                event.setCancelled(true);
                            } else {
                                event.setCancelled(false);
                            }
                            return;
                        }
                        if (grupoSmart.contentsItem(item)) {
                            event.setCancelled(false);
                            return;
                        }

                    }
                }
            }


            if (destination.getType() == InventoryType.HOPPER) {
                for (int i = 0; i < inventory.getSize(); i++) {
                    itemStack = inventory.getItem(i);
                    if (itemStack != null) {
                        item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getDsItem());
                        item.setSize(itemStack);// Pegando a quantidade do mesmo item
                        item.setMax(itemStack);// Setando a quantidade de impilhamento do item

                        // Se o funil estiver configurado para mais de um item ou grupo
                        if(smartHopperDestino.getNameHopper().contains(";")){
                            String[] names = smartHopperDestino.getNameHopper().split(";");
                            for(String name : names){
                                smartHopper = new SmartHopper(destination.getLocation().getBlock(), name);

                                // Se o hopper estiver configurado para o item
                                if(smartHopper.getType() instanceof Item itemSmart){
                                    if(itemSmart.getCdItem() == item.getCdItem() && smartHopper.isTransferer(item)){
                                        destination.addItem(itemStack);
                                        inventory.removeItem(itemStack);
                                    }
                                }

                                // Se o funil for configurado para o grupo
                                if(smartHopper.getType() instanceof Grupo grupoSmart){
                                    if(grupoSmart.contentsItem(item) && smartHopper.isTransferer(item)){
                                        destination.addItem(itemStack);
                                        inventory.removeItem(itemStack);
                                    }
                                }
                            }
                        }

                        // Se o funil for configurado para item
                        if (smartHopperDestino.getType() instanceof Item itemSmart) {
                            // Item bloqueado
                            if (smartHopperDestino.getNameHopper().contains("#")) {
                                if (itemSmart.getCdItem() != item.getCdItem()) {
                                    destination.addItem(itemStack);
                                    inventory.removeItem(itemStack);
                                }
                                continue;
                            }
                            // Se o item for igual a configuração do funil
                            if (item.getCdItem() == itemSmart.getCdItem() && smartHopperDestino.isTransferer(item)) {
                                destination.addItem(itemStack);
                                inventory.removeItem(itemStack);
                            }
                        }
                        // Se o funil estiver configurado para o grupo
                        if(smartHopperDestino.getType() instanceof Grupo grupoSmart){
                            if(smartHopperDestino.getNameHopper().contains("#")){
                                if(!grupoSmart.contentsItem(item)){
                                    destination.addItem(itemStack);
                                    inventory.removeItem(itemStack);
                                }
                                continue;
                            }
                            // Se o grupo conter o item
                            if(grupoSmart.contentsItem(item)){
                                destination.addItem(itemStack);
                                inventory.removeItem(itemStack);
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
            Msg.ServidorErro("Erro ao movimentar o item!!!", "", getClass(), e);
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