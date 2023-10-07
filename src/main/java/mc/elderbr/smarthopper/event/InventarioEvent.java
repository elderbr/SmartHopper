package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventarioEvent implements Listener, Botao {

    private final Class CLAZZ = getClass();
    private Player player;
    private InventoryCustom inventoryCustom;
    private Inventory inventory;

    private ItemStack itemClicked;
    private Grupo grupo;
    private List<ItemStack> listItemStack = new ArrayList<>();
    private InventoryClickEvent event;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
        player = (Player) event.getWhoClicked();
        this.event = event;
        try {
            inventoryCustom = new InventoryCustom(event);
            inventoryCustom.btnNavegation();
            itemClicked = event.getCurrentItem();

            if (itemClicked == null || itemClicked.getType().isAir()) return;
            if (inventoryCustom.getGrupo() == null) return;
            if (!player.isOp() && !Config.CONTAINS_ADD(player)) return;

            inventory = player.getOpenInventory().getTopInventory();
            if (grupo == null) {
                grupo = inventoryCustom.getGrupo();
                listItemStack = inventoryCustom.getGrupo().getListItemStack();
            }
            addItem();// Adicionando item ao grupo
            removeItem();// Removendo item do grupo

        } catch (Exception e) {
            Msg.PlayerRed(player, e.getMessage());
        }
    }

    private void addItem() {
        // Se o clique for com o botão direito do mouse
        if (event.isLeftClick()) {
            // Se o botão for igual ao botões personalizados
            if(equalButton(itemClicked)){
                return;
            }
            // Verifica se o item está na lista do inventário
            if (!listItemStack.contains(itemClicked)) {
                inventory.addItem(new ItemStack(itemClicked.getType()));
            }else{
                Msg.PlayerGold(player, "O item "+ Item.ToName(itemClicked)+" já está na lista do grupo!!!");
            }
        }
    }

    private void removeItem() {
        // Se for clicado com o botão direito do mouse no item
        if (event.isRightClick()) {
            if(equalButton(itemClicked)) return;
            // Verificando se o item está na lista do inventário
            if (inventory.contains(itemClicked)) {
                inventory.removeItem(itemClicked);
            }
            // Verificando se o item está lista
            if(listItemStack.contains(itemClicked)){
                listItemStack.remove(itemClicked);
            }
        }
    }
}