package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.utils.Msg;
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

        // VERIFICA SE O JOGADOR ESTÁ SEGURANDO GRAVETO E SE BATEU NO FUNIL
        if (event.getClickedBlock() == null
                || itemStack.getType() != Material.STICK
                || event.getAction() != Action.LEFT_CLICK_BLOCK
                || event.getClickedBlock().getType() != Material.HOPPER) {
            return;
        }

        block = event.getClickedBlock();
        try {
            smartHopper = new SmartHopper(block);
            Msg.getType(player, smartHopper.getType());
        } catch (Exception e) {
            Msg.PlayerGold(player, e.getMessage());
            Msg.ServidorRed(e.getMessage());
        }

    }
}