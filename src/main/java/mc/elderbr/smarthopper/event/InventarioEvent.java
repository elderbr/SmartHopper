package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
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

    private String titulo;
    private Player player;
    private ItemStack itemStack;
    private ItemStack itemClick;
    private int pag = 1;

    // GRUPO
    private Grupo grupo;
    private Inventory inventory;

    private InventoryCustom inventoryCustom;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        inventory = player.getOpenInventory().getTopInventory();

        // Criando o inventario personalizado do grupo
        try {
            inventoryCustom = new InventoryCustom(player, event);
            inventoryCustom.create();
        } catch (GrupoException e) {
            Msg.PlayerRed(player, e.getMessage());
        }

        // SE O INVENTARIO ABERTO NÃO FOR UM GRUPO
        if (!inventoryCustom.isGrupo()) {
            return;
        }

        // Copiando o item que está sendo clicado
        if (event.getCurrentItem() == null) return;

        event.setCancelled(true);// CANCELANDO O MOVIMENTO DO ITEM

        itemClick = event.getCurrentItem().clone();
        itemClick.setAmount(1);

        // CLIQUE COM O BOTÃO ESQUERDO DO MOUSE
        if (event.isLeftClick()) {
            // CLICANDO NO BOTÃO PERSONALIZADO PRÓXIMO
            try {

                // BOTÃO QUE VAI PARA A PRÓXIMA PÁGINA
                if (itemClick.equals(BtnProximo()) && pag < inventoryCustom.getMaxPag()) {
                    pag++;
                    player.closeInventory();
                    inventoryCustom.show(pag);
                }

                // BOTÃO PARA RETORNA A PÁGINA ANTERIOR
                if (itemClick.equals(BtnAnterior()) && pag > 1) {
                    pag--;
                    player.closeInventory();
                    inventoryCustom.show(pag);
                }

                // BOTÃO SALVAR ITENS DO GRUPO
                if (itemClick.equals(BtnSalva()) && Config.CONTAINS_ADD(player)) {
                    player.closeInventory();
                    inventoryCustom.setInventory(inventory);
                    pag = 1;
                    if (inventoryCustom.save()) {
                        Msg.PlayerTodos("Grupo $e$l" + inventoryCustom.toNameGrupo() + "$r foi alterado pelo o jogador $e" + player.getName() + "!!!");
                    } else {
                        Msg.PlayerRed(player, "Não foi possivel alterar o grupo!!!");
                    }
                }

                // VERIFICAR SE O JOGAR É UM ADM DO SMARTHOPPER E SE O ITEM NÃO ESTÁ APRESENTE NO INVENTÁRIO
                if (Config.CONTAINS_ADD(player) && !inventory.contains(itemClick)) {
                    inventory.addItem(itemClick);
                }

            } catch (GrupoException e) {
                Msg.PlayerGold(player, e.getMessage());
            }

        }
        if (event.isRightClick()) {
            // VERIFICAR SE O JOGAR É UM ADM DO SMARTHOPPER E SE O ITEM ESTÁ APRESENTE NO INVENTÁRIO
            if (Config.CONTAINS_ADD(player)
                    && inventory.contains(itemClick)
                    && !itemClick.equals(BtnSalva())
                    && !itemClick.equals(BtnAnterior())
                    && !itemClick.equals(BtnProximo())
            ) {
                inventory.removeItem(itemClick);// REMOVER O ITEM DO INVENTÁRIO DE CIMA
            }
        }
    }
}