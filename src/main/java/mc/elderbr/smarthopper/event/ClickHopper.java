package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.SmartHopper;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ClickHopper implements Listener {

    private Player player;
    private ItemStack itemStack;
    private Block block;
    private SmartHopper smartHopper;

    private Item item;

    @EventHandler
    public void clickHopper(PlayerInteractEvent event) {

        player = event.getPlayer();
        itemStack = player.getInventory().getItemInMainHand();
<<<<<<< HEAD
        Hopper hopper = null;
=======
        item = new Item(itemStack);
>>>>>>> v4.0.0

        if(itemStack.getType() == Material.STICK && event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(event.getClickedBlock().getState().getType()==Material.HOPPER) {
                hopper = (Hopper) event.getClickedBlock().getState();
                SmartHopper smartHopper = new SmartHopper(hopper);

<<<<<<< HEAD
                if(smartHopper.getType()!=null) {
                    Dados nameHopper = (Dados) smartHopper.getType();
                    Msg.ServidorGreen("clicado com o graveto no bloco "+ nameHopper.getName(), getClass());
                }else{
                    Msg.ServidorGreen("não encontrado ", getClass());
                }
            }
=======
        block = event.getClickedBlock();
        smartHopper = new SmartHopper(block);

        // SE FOR CLICADO NO HOPPER COM GRAVETO NA MÃO
        if (itemStack.getType() == Material.STICK && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            // SE EXISTIR MAIS DE UM ITEM OU GRUPO CONFIGURADO PARA O MESMO FUNIL
            smartHopper.msgPlayerSmartHopper(player);
>>>>>>> v4.0.0
        }
    }
}
