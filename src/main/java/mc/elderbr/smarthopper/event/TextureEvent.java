package mc.elderbr.smarthopper.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TextureEvent implements Listener {

    // http://elderbr.com/minecraft/textures/SmartHopper.zip

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().setResourcePack("http://elderbr.com/minecraft/textures/SmartHopper.zip");
    }

}
