package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventarioEvent implements Listener {

    private String titulo;
    private Player player;
    private ItemStack itemStack;
    private ItemStack itemBtnSalve;

    // GRUPO
    private String nameGrupo;
    private Grupo grupo;
    private Inventory inventory;

    private Grupo grupoNovo;
    private List<String> lore;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        inventory = player.getOpenInventory().getTopInventory();
        itemStack = event.getCurrentItem();

        titulo = event.getView().getTitle();// NOME DO INVENTARIO

        // VERIFICA SE O ITEM É DIFERENTE DE NULL, SE O INVENTARIO É CHEST E SE O PLAYER É ADM OU OPERADOR
        if (itemStack == null || itemStack.getType() == Material.AIR || inventory.getType() != InventoryType.CHEST) {
            return;
        }

        if (titulo.contains(Msg.Color(VGlobal.GRUPO_NOVO_INVENTORY)) || titulo.contains(Msg.Color(VGlobal.GRUPO_INVENTORY))) {
            event.setCancelled(true);

            if (titulo.contains(Msg.Color(VGlobal.GRUPO_NOVO_INVENTORY))
                    && Config.ADM_LIST.contains(player.getName())
                    || Config.OPERADOR_LIST.contains(player.getName())) {


                itemBtnSalve = inventory.getItem(53);
                lore = new ArrayList<>();
                if (itemBtnSalve != null && !itemBtnSalve.getItemMeta().getLore().isEmpty()) {
                    lore = itemBtnSalve.getItemMeta().getLore();
                }

                if (event.isLeftClick() && !inventory.contains(itemStack)) {
                    event.getInventory().addItem(itemStack);
                }
                if (event.isRightClick() && inventory.contains(itemStack) && itemBtnSalve != null && !lore.contains("$3Salva novo grupo")) {
                    event.getInventory().removeItem(itemStack);
                }
            }
        }
        Msg.ServidorGreen("nome do inventario aberto >> " + titulo);


    }
}
