package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.SmartHopper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
            //smartHopper.msgPlayerSmartHopper(player);
        }
    }
}