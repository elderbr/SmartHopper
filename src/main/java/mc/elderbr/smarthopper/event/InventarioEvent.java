package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventarioEvent implements Listener, Botao {

    private final Class CLAZZ = getClass();
    private Player player;
    private InventoryCustom inventoryCustom;
    private Inventory inventory;

    private ItemStack itemClicked;
    private Grupo grupo;
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
            }

            add();// Adicionando item ao grupo
            remove();// Removendo item do grupo
            save();// Salvando o grupo

        } catch (Exception e) {
            Msg.PlayerRed(player, e.getMessage());
        }
    }

    private void add() {
        // Se o clique for com o botão direito do mouse
        if (event.isLeftClick()) {
            // Se o botão for igual ao botões personalizados
            if (equalButton(itemClicked)) {
                return;
            }
            // Verifica se o item está na lista do inventário
            if (!grupo.containsItem(itemClicked)) {
                // Adicionando o item no inventário aberto
                inventory.addItem(new ItemStack(itemClicked.getType()));
                // Adicionando o item na lista de item do grupo
                grupo.addListItem(itemClicked);
            }
        }
    }

    private void remove() {
        // Se for clicado com o botão direito do mouse no item
        if (event.isRightClick()) {

            if (equalButton(itemClicked)) return;

            // Verifica se o jogador é ADM
            if (!player.isOp() && !Config.CONTAINS_ADD(player)) return;

            // Verificando se o item está na lista do inventário
            if (inventory.contains(itemClicked)) {
                inventory.removeItem(itemClicked);
            }

            // Verificando se o item está lista
            grupo.removeListItem(itemClicked);

        }
    }

    private void save() {
        // Verificar se foi clicado com o botão esquerdo do mouse no botão salvar(save)
        if (event.isLeftClick() && itemClicked.equals(BtnSalva())) {

            // Verifica se o jogador é ADM
            if (!player.isOp() && !Config.CONTAINS_ADD(player)) return;
            GrupoConfig.UPDATE(grupo);// Atualizando o grupo
            player.closeInventory();// Fechando o inventário do jogador

            // Envia mensagem para todos os jogadores online
            Msg.PlayerTodos("$e$lO jogador " + player.getName() + " alterou o grupo " + grupo.getName() + "!!!");
        }
    }
}