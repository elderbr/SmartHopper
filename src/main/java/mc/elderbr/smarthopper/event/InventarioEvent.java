package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.cmd.GrupoComando;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    private InventoryCustom inventoryCustom;

    private Grupo grupoNovo;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        inventory = player.getOpenInventory().getTopInventory();

        // VERIFICA SE O ITEM É DIFERENTE DE NULL, SE O INVENTARIO É CHEST E SE O PLAYER É ADM OU OPERADOR
        if (event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR
                || inventory.getType() != InventoryType.CHEST
        ) {
            return;
        }


        titulo = event.getView().getTitle();// NOME DO INVENTARIO
        inventoryCustom = GrupoComando.INVENTORY;// INVENTARIO VINDO DO GRUPO COMANDO

        itemClick = event.getCurrentItem();// ITEM CLICADO
        itemClick.setAmount(1);
        itemStack = new ItemStack(itemClick.getType());// PEGANDO O TIPO DE ITEM QUE FOI CLICADO
        itemBtnSalve = player.getOpenInventory().getItem(53);// BOTÃO DE SALVAR OU ATUALIZAR VAI SER A LÃ DA COR LIMÃO COM LORE

        // SE O INVENTARIO ABERTO É UM GRUPO
        if (inventoryCustom.getType() != null) {
            event.setCancelled(true);

            if (inventoryCustom.isAdm()) {
                if (event.isLeftClick()) {
                    if (itemBtnSalve != null
                            && itemBtnSalve.equals(itemClick)
                            && itemBtnSalve.getItemMeta().getDisplayName().equals("§2§lSalva")
                            ) {
                        Msg.ServidorBlue("btn salvar " + itemBtnSalve.getItemMeta().getDisplayName(), getClass());
                        return;
                    }
                    if(!inventory.contains(itemClick)) {
                        inventory.addItem(itemClick);
                    }
                }

                if (event.isRightClick() && inventory.contains(itemClick)) {
                    if (itemClick.getType() != Material.BARRIER) {
                        inventory.removeItem(itemClick);
                    }
                }
            }

        }

    }
}