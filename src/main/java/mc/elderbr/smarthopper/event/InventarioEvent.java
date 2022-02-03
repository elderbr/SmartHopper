package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventarioEvent implements Listener {

    private String titulo;
    private Player player;
    private ItemStack itemStack;
    private ItemStack itemBtnSalve;
    private ItemStack itemClick;

    // GRUPO
    private String nameGrupo;
    private Grupo grupo;
    private Inventory inventory;

    private Grupo grupoNovo;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        inventory = player.getOpenInventory().getTopInventory();



    }
}
