package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.cmd.GrupoComando;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Bukkit;
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

        // VERIFICA SE O ITEM É DIFERENTE DE NULL, SE O INVENTARIO É CHEST E SE O PLAYER É ADM OU OPERADOR
        if (event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR
                || inventory.getType() != InventoryType.CHEST
        ) {
            return;
        }


        titulo = event.getView().getTitle();// NOME DO INVENTARIO
        if (titulo.contains(Msg.Color("$5$lGrupo Novo")) || titulo.contains(Msg.Color("$8$lGrupo"))) {

            event.setCancelled(true);

            itemClick = event.getCurrentItem();// ITEM CLICADO
            itemStack = new ItemStack(itemClick.getType());// PEGANDO O TIPO DE ITEM QUE FOI CLICADO
            itemBtnSalve = player.getOpenInventory().getItem(53);// BOTÃO DE SALVAR OU ATUALIZAR VAI SER A LÃ DA COR LIMÃO COM LORE

            // ADICIONA NOVOS ITENS
            if (event.isLeftClick() && !itemClick.equals(itemBtnSalve) && !inventory.contains(itemStack)  && VGlobal.ADM_LIST.contains(player.getName())) {// CLICADO COM O BOTÃO ESQUERDO ADICIONA O NOVO ITEM
                inventory.addItem(new ItemStack(itemStack.getType()));
            }
            // REMOVE ITENS
            if (event.isRightClick() && VGlobal.ADM_LIST.contains(player.getName())) {// CLICADO COM O BOTÃO DIREITO REMOVE O ITEM
                if (itemStack.getItemMeta().getLore() == null) {
                    inventory.remove(itemStack);// REMOVE O ITEM DO INVENTARIO
                }
            }

            // SALVAR NOVO GRUPO
            if (event.isLeftClick() && itemClick.getItemMeta().getLore() != null && itemClick.getItemMeta().getLore().contains(Msg.Color("$3Salva novo grupo"))) {
                grupoNovo = GrupoComando.GRUPO;
                grupoNovo.addTraducao(player.getLocale(), Utils.toUP(grupoNovo.getDsGrupo()));

                // ADICIONANDO OS ITEM AO GRUPO
                List<String> grupoNovoList = new ArrayList<>();
                for (ItemStack itemStack : inventory.getContents()) {
                    if (itemStack != null && !itemStack.equals(itemBtnSalve)) {
                        grupoNovo.addList(VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getDsItem()));
                    }
                }
                player.closeInventory();

            }

            // ALTERANDO GRUPO
            if (event.getClick().isLeftClick() && itemClick.getItemMeta().getLore() != null && itemClick.getItemMeta().getLore().contains(Msg.Color("$3Salvar"))) {

                nameGrupo = titulo.substring(Msg.Color("$8$lGrupo: $r").length(), titulo.indexOf(Msg.Color(" $lID"))).trim().toLowerCase();

                grupo = GrupoComando.GRUPO;// BUSCA NO BANCO O GRUPO


                grupo.getListItem().clear();// APAGANDO A LISTA DO GRUPO DOS ITENS

                player.closeInventory();

                // ADICIONANDO OS ITEM AO GRUPO
                List<String> grupoNovoList = new ArrayList<>();
                for (ItemStack items : inventory.getContents()) {
                    if (items != null && !items.getType().isAir() && !items.equals(itemBtnSalve)) {
                        Item item = new Item(items);
                        grupo.addList(VGlobal.ITEM_MAP_NAME.get(item.getDsItem()));
                    }
                }


                grupo.setDsLang(player);
                Bukkit.getServer().broadcastMessage(Msg.Color("$6O jogador "+ player.getName() +" atualizou o grupo $a$l" + grupo.getDsGrupo() + "!"));
                player.sendMessage(Msg.Color("$6Grupo $a$l" + grupo.toTraducao() + "$r$6 atualizado com sucesso!"));

            }


            event.setCancelled(true);
        }

    }
}