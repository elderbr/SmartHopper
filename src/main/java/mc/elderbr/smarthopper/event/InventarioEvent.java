package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventarioEvent implements Listener, Botao {

    private final Class CLAZZ = getClass();
    private Player player;
    private InventoryCustom inventoryCustom;
    private Inventory inventory;
    private List<ItemStack> listItemStack = new ArrayList<>();

    private ItemStack itemClicked;
    private ItemController itemController = new ItemController();
    private Grupo grupo;
    private GrupoController grupoCtrl = new GrupoController();
    private InventoryClickEvent event;

    @EventHandler
    public void openInventory(InventoryOpenEvent event) {
        try {
            grupo = null;
            player = (Player) event.getPlayer();
            inventoryCustom = new InventoryCustom(event);
            inventory = event.getInventory();
        } catch (Exception e) {
            Msg.PlayerGold(player, e.getMessage());
        }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
        player = (Player) event.getWhoClicked();
        this.event = event;
        try {

            itemClicked = event.getCurrentItem();

            if (inventoryCustom == null || itemClicked == null || itemClicked.getType().isAir()) return;

            grupo = inventoryCustom.getGrupo();

            if (grupo == null) return;
            event.setCancelled(true);// Cancela o movimento do item pelo o player
            // Evento que navega entre os itens do grupo
            inventoryCustom.btnNavegation(event);

            // Verifica se o player é Adm do servidor ou do Smart Hopper
            if (!player.isOp() && !AdmController.ContainsAdm(player)) return;
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
                grupo.addItems(itemController.findByItemStack(newItem));
            }
        }
    }

    private void remove() {
        // Se for clicado com o botão direito do mouse no item
        if (event.isRightClick()) {

            if (equalButton(itemClicked)) return;

            // Verifica se o jogador é ADM
            if (!player.isOp() && !AdmController.ContainsAdm(player)) return;

            // Verificando se o item está na lista do inventário
            ItemStack newItem = new ItemStack(itemClicked.getType());
            if (inventory.contains(newItem)) {
                inventory.removeItem(newItem);
            }

            // Verificando se o item está lista
            grupo.removeItems(newItem);

        }
    }

    private void save() {
        try {
            // Verificar se foi clicado com o botão esquerdo do mouse no botão salvar(save)
            if (event.isLeftClick() && itemClicked.equals(BtnSalva())) {
                String msg = "Ops, algo deu errado!!!";
                // Verifica se o jogador é ADM
                if (!player.isOp() && !AdmController.ContainsAdm(player)) return;
                if (grupo.getId() < 1) {
                    grupo.toTranslation(player);
                    if (grupoCtrl.save(grupo)) {
                        msg = String.format("$eO jogador $c%s $eadicionou um novo grupo $c%s$e!!!", player.getName(), grupo.getName());
                    }
                } else {
                    grupoCtrl.update(grupo);// Atualizando o grupo
                    msg = String.format("$eO jogador $c%s $ealterou o grupo $c%s$e!!!", player.getName(), grupo.getName());
                }
                player.closeInventory();// Fechando o inventário do jogador

                // Envia mensagem para todos os jogadores online
                Msg.PlayerTodos(msg);
                grupo = null;
            }
        } catch (Exception e) {
            Msg.PlayerRed(player, e.getMessage());
        }
    }
}