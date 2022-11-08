package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.TextureController;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TextureEvent implements Listener {


    TextureController useTexture;

    @EventHandler
    public void onResourcepackStatusEvent(PlayerResourcePackStatusEvent event){
       useTexture = new TextureController();
       useTexture.aceitaTexture(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        useTexture = new TextureController();
        useTexture.carrega(event.getPlayer());
    }

}
