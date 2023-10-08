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

import static mc.elderbr.smarthopper.interfaces.VGlobal.CD_MAX;

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
            ItemStack newItem = new ItemStack(itemClicked.getType());
            newItem.setAmount(1);
            // Verifica se o item está na lista do inventário
            if (!grupo.containsItem(newItem)) {
                // Adicionando o item no inventário aberto
                inventory.addItem(newItem);
                // Adicionando o item na lista de item do grupo
                grupo.addListItem(newItem);
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
            String msg = "Ops, algo deu errado!!!";
            // Verifica se o jogador é ADM
            if (!player.isOp() && !Config.CONTAINS_ADD(player)) return;
            if (grupo.getId() < 1) {
                grupo.setId(CD_MAX.get(0) + 1);
                grupo.addTranslation(player);
                if (GrupoConfig.ADD(grupo)) {
                    CD_MAX.add(0, grupo.getId());
                    msg = String.format("$eO jogador %s adicionou um novo grupo %s!!!", player.getName(), grupo.getName());
                }
            } else {
                GrupoConfig.UPDATE(grupo);// Atualizando o grupo
                msg = "$e$lO jogador " + player.getName() + " alterou o grupo " + grupo.getName() + "!!!";
            }
            player.closeInventory();// Fechando o inventário do jogador

            // Envia mensagem para todos os jogadores online
            Msg.PlayerTodos(msg);
            grupo = null;
        }
    }
}