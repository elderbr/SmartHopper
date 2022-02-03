package mc.elderbr.smarthopper.event;

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

import java.util.List;

public class ClickHopper implements Listener {

    private Player player;
    private ItemStack itemStack;
    private Block block;
    private SmartHopper smartHopper;
    private InventoryCustom inventory;
    private Grupo grupo;
    private String nameGrupo;
    private ItemStack itemSalve;
    private ItemMeta meta;
    private List<String> lore;
    private InventoryCustom inventoryCustom;

    @EventHandler
    public void clickHopper(PlayerInteractEvent event) {

        player = event.getPlayer();
        itemStack = player.getInventory().getItemInMainHand();
    }
}
