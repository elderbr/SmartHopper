package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class BlockBreakHopperEvent implements Listener, VGlobal {

    private Player player;
    private Block block;

    private SmartHopper smartHopper;

    @EventHandler
    public void onBlockHopperBreak(BlockBreakEvent event) {
        player = event.getPlayer();
        block = event.getBlock();
        if (block.getType() == Material.HOPPER) {
            Collection<ItemStack> list = block.getDrops();
            if (list.isEmpty()) return;
            for (ItemStack itemStack : list) {
                if (itemStack.getType() == Material.HOPPER) {
                    if (Objects.nonNull(itemStack.getItemMeta()) && !itemStack.getItemMeta().getDisplayName().isEmpty()) {
                        ItemMeta meta = itemStack.getItemMeta();
                        smartHopper = new SmartHopper(meta.getDisplayName());
                        if (!smartHopper.getTypes().isEmpty()) {
                            event.setDropItems(false);
                            meta.setLore(List.of(NAME_RECIPE));
                            itemStack.setItemMeta(meta);
                            Location location = block.getLocation();
                            location.setY(location.getY()+0.5);
                            block.getWorld().dropItemNaturally(location, itemStack);
                        }
                    }
                }
            }
        }
    }
}
