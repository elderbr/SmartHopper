package mc.elderbr.smarthopper.event;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilCreate implements Listener {

    private Player player;
    private ItemStack itemStack;

    @EventHandler
    public void createAnvil(PlayerInteractEvent e) {
        player = e.getPlayer();
        itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.HOPPER || itemStack.getItemMeta().getLore() == null) return;

/*
        // CRIA UMA BIGORNA SE O HOPPER ESTIVER COM A LORE
        if (itemStack.getItemMeta().getLore().get(0).equals("SmartHopper") && e.getAction() == Action.RIGHT_CLICK_AIR) {
            EntityPlayer entPlayer = ((CraftPlayer) player).getHandle();
            entPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(entPlayer.nextContainerCounter(), Containers.ANVIL, new ChatComponentText("§8§lSmart Hopper")));
        }

 */
    }
}
