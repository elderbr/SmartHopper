package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.TextureController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TextureEvent implements Listener {

    TextureController textureCtrl = new TextureController();

    @EventHandler
    public void playerResourcePack(PlayerResourcePackStatusEvent event) {
        textureCtrl.aceitaTexture(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        textureCtrl.carrega(event.getPlayer());
    }

}
