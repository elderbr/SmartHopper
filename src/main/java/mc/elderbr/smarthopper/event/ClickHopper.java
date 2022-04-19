package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.SmartHopper;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
        item = new Item(itemStack);

        // RETORN SE O BLOCO CLICADO FOR DIFERENTE DO HOPPER OU SE
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.HOPPER) {
            return;
        }

        block = event.getClickedBlock();
        smartHopper = new SmartHopper(block);

        // SE FOR CLICADO NO HOPPER COM GRAVETO NA MÃO
        if (itemStack.getType() == Material.STICK && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            // SE EXISTIR MAIS DE UM ITEM OU GRUPO CONFIGURADO PARA O MESMO FUNIL
            smartHopper.msgPlayerSmartHopper(player);
        }
    }
}
