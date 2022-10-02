package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TextureEvent implements Listener {

    // http://elderbr.com/minecraft/textures/SmartHopper.zip

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().setResourcePack("http://elderbr.com/minecraft/textures/SmartHopper.zip");
    }
}
