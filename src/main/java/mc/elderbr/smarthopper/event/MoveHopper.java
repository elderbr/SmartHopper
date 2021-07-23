package mc.elderbr.smarthopper.event;

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
            item = new Item(itemStack);

            // Pegando o inventorio onde está o item
            inventory = event.getSource();

            // Destino do item
            destination = event.getDestination();// Inventorio de destino

            // Pega o bloco de baixo de onde o item está
            blockDown = inventory.getLocation().getBlock().getRelative(BlockFace.DOWN);

            if (blockDown.getState().getType() == Material.HOPPER) {// Verifica se o bloco de baixo é um hopper

                // SE FOR LIVRO ENCANTADO
                if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                    item = Utils.isEnchantment(itemStack).get(0);
                }
                // SE FOR POÇÕES
                if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.LINGERING_POTION) {
                    item = Utils.isPotion(itemStack);
                }

                isBlockDownHopper();// Verifica se existe mais funis em baixo
                for (Hopper hoppers : hopperList) {

                    smartHopper = new SmartHopper(hoppers);

                    // BLOQUEIA A PASSAGEM DOS ITENS
                    if (smartHopper.getName().contains("#")) {

                        isBloqueio = new ArrayList<>();

                        // VERIFICA SE EXISTEM MAIS DE UM ITEM OU GRUPO PARA O MESMO FUNIL
                        if (smartHopper.getName().contains(";")) {
                            for (String items : smartHopper.getName().split(";")) {

                                smartHopperAux = smartHopper;
                                smartHopperAux.setName(items);

                                // SE O HOPPER FOR ITEM
                                if (smartHopperAux.getType() instanceof Item) {// VERIFICA SE O HOPPER ESTA CONFIGURADO PARA O ITEM
                                    isBloqueio.add(smartHopperAux.equalsItem(item));
                                }

                                // SE O HOPPER FOR GRUPO
                                if (smartHopperAux.getType() instanceof Grupo) {// VERIFICA SE O HOPPER ESTA CONFIGURADO PARA O GRUPO
                                    isBloqueio.add(smartHopperAux.equalsGrupo(item));
                                }
                                // SE O ITEM FOR IGUAL AO HOPPER TRAVA A PASSAGEM
                                if (isBloqueio.contains(true)) {
                                    event.setCancelled(true);
                                } else {// LIBERA A PASSAGEM DO ITEM
                                    event.setCancelled(false);
                                }
                            }
                        } else {
                            // VERIFICA SE O HOPPER ESTA CONFIGURADO PARA O ITEM
                            if (smartHopper.getType() instanceof Item) {
                                isBloqueio.add(smartHopper.equalsItem(item));
                            }
                            // VERIFICA SE O HOPPER ESTA CONFIGURADO PARA O GRUPO
                            if (smartHopper.getType() instanceof Grupo) {
                                isBloqueio.add(smartHopper.equalsGrupo(item));
                            }
                            if (isBloqueio.contains(true)) {
                                event.setCancelled(true);
                            } else {// LIBERA A PASSAGEM DO ITEM
                                event.setCancelled(false);
                            }
                        }
                    } else {

                        // VERIFICA SE EXISTEM MAIS DE UM ITEM OU GRUPO PARA O MESMO FUNIL
                        if (smartHopper.getName().contains(";")) {

                            for (String items : smartHopper.getName().split(";")) {

                                smartHopperAux = smartHopper;
                                smartHopperAux.setName(items);

                                // SMARTHOPPER É ITEM
                                if (smartHopperAux.getType() instanceof Item) {
                                    if (smartHopperAux.equalsItem(item)) {// VERIFICA SE O ID DO HOPPER É IGUAL DO ITEM
                                        event.setCancelled(false);
                                    }
                                }
                                // SMARTHOPPER É GRUPO
                                if (smartHopperAux.getType() instanceof Grupo) {
                                    if (smartHopperAux.equalsGrupo(item)) {// VERIFICA SE ITEM COMTÉM NO GRUPO
                                        event.setCancelled(false);
                                    }
                                }
                            }
                        }

                        // SMARTHOPPER É ITEM
                        if (smartHopper.getType() instanceof Item) {
                            if (smartHopper.equalsItem(item)) {// VERIFICA SE O ID DO HOPPER É IGUAL DO ITEM
                                event.setCancelled(false);
                            }
                        }

                        // SMARTHOPPER É GRUPO
                        if (smartHopper.getType() instanceof Grupo) {
                            if (smartHopper.equalsGrupo(item)) {// VERIFICA SE ITEM COMTÉM NO GRUPO
                                event.setCancelled(false);
                            }
                        }
                    }
                }
            }

            if (destination.getType() == InventoryType.HOPPER && SmartHopper.ParseHopper(destination).getName().equals("HOPPER")) {
                event.setCancelled(false);// Ativa o movimento do item
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
