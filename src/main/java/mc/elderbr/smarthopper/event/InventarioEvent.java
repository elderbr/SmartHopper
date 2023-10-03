package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventarioEvent implements Listener, Botao {

    private final Class CLAZZ = getClass();
    private Player player;
    private InventoryCustom inventoryCustom;


    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
        player = (Player) event.getWhoClicked();
        try {
            inventoryCustom = new InventoryCustom(event);
            inventoryCustom.btnNext();
        } catch (GrupoException e) {
            Msg.PlayerRed(player, e.getMessage());
        }
    }
}