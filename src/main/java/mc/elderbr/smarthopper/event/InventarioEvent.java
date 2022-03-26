package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
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
                || !Config.ADM_LIST.contains(player.getName())
                && !Config.OPERADOR_LIST.contains(player.getName())) {
            return;
        }


        titulo = event.getView().getTitle();// NOME DO INVENTARIO
        if (titulo.contains(Msg.Color("$5$lGrupo Novo")) || titulo.contains(Msg.Color("$8$lGrupo"))) {
            itemClick = event.getCurrentItem();// ITEM CLICADO
            itemStack = new ItemStack(itemClick.getType());// PEGANDO O TIPO DE ITEM QUE FOI CLICADO
            itemBtnSalve = player.getOpenInventory().getItem(53);// BOTÃO DE SALVAR OU ATUALIZAR VAI SER A LÃ DA COR LIMÃO COM LORE

            // ADICIONA NOVOS ITENS
            if (event.isLeftClick() && !itemClick.equals(itemBtnSalve) && !inventory.contains(itemStack)) {// CLICADO COM O BOTÃO ESQUERDO ADICIONA O NOVO ITEM
                inventory.addItem(new ItemStack(itemStack.getType()));
            }
            // REMOVE ITENS
            if (event.isRightClick()) {// CLICADO COM O BOTÃO DIREITO REMOVE O ITEM
                if (itemStack.getItemMeta().getLore() == null) {
                    inventory.remove(itemStack);// REMOVE O ITEM DO INVENTARIO
                }
            }

            // SALVAR NOVO GRUPO
            if (event.isLeftClick() && itemClick.getItemMeta().getLore() != null && itemClick.getItemMeta().getLore().contains(Msg.Color("$3Salva novo grupo"))) {
                grupoNovo = new Grupo();
                grupoNovo.setCdGrupo(Grupo.newID());
                grupoNovo.setDsGrupo(titulo.substring(Msg.Color("$5$lGrupo Novo: $r").length(), titulo.length()).trim());
                //grupoNovo.addTraducao(player.getLocale(), Utils.toUP(grupoNovo.getDsGrupo()));

                // ADICIONANDO OS ITEM AO GRUPO
                List<String> grupoNovoList = new ArrayList<>();
                for (ItemStack items : inventory.getContents()) {
                    if (items != null && !items.equals(itemBtnSalve)) {
                        //grupoNovo.addItemList(items);
                        //grupoNovoList.add(new Item(items).getDsGrupo());
                    }
                }

                // ADICIONANDO A VARIAL GLOBAL
                VGlobal.GRUPO_MAP_ID.put(grupoNovo.getCdGrupo(), grupoNovo);// ADICIONANDO A BUSCA PELO ID
                VGlobal.GRUPO_MAP_NAME.put(grupoNovo.getDsGrupo(), grupoNovo);// ADICIONANDO A BUSCA PELO NOME
                VGlobal.GRUPO_NAME_LIST.add(grupoNovo.getDsGrupo());// ADICIONANDO NA LISTA DE NOMES DO GRUPO
                //VGlobal.GRUPO_LANG_MAP.put(grupoNovo.getDsGrupo(), grupoNovo.getLang());// ADICIONANDO A BUSCA PELO LANG
                VGlobal.GRUPO_MAP.put(grupoNovo.getDsGrupo(), grupoNovo.getDsGrupo());// ADICIONANDO NA LISTA DE LANG TRADUZIDO
                VGlobal.GRUPO_ITEM_MAP_LIST.put(grupoNovo.getDsGrupo(), grupoNovoList);// LISTA GLOBAL DO GRUPO RETORNANDO A LISTA DE ITENS

                new GrupoConfig(grupoNovo, Grupo.NEW);// ADICIONANDO O GRUPO NO BANCO

                player.closeInventory();
                player.sendMessage(Msg.Color("$6Grupo $a$l" + grupoNovo.getDsGrupo() + "$r$6 criado com sucesso!"));
            }

            // ALTERANDO GRUPO
            if (event.getClick().isLeftClick() && itemClick.getItemMeta().getLore() != null && itemClick.getItemMeta().getLore().contains(Msg.Color("$3Salvar"))) {

                nameGrupo = titulo.substring(Msg.Color("$8$lGrupo: $r").length(), titulo.indexOf(Msg.Color(" $lID"))).trim().toLowerCase();
                grupo = VGlobal.GRUPO_MAP_NAME.get(VGlobal.GRUPO_MAP.get(nameGrupo));// BUSCA NO BANCO O GRUPO

                grupo.getListItem().clear();// APAGANDO A LISTA DO GRUPO DOS ITENS
                VGlobal.GRUPO_ITEM_MAP_LIST.get(grupo.getDsGrupo()).clear();// APAGANDO A LISTA GLOBAL DO GRUPO OS ITENS

                // ADICIONANDO OS ITEM AO GRUPO
                List<String> grupoNovoList = new ArrayList<>();
                for (ItemStack items : inventory.getContents()) {
                    if (items != null && !items.equals(itemBtnSalve)) {
                        //grupo.addItemList(items);
                        //VGlobal.GRUPO_ITEM_MAP_LIST.get(grupo.getDsGrupo()).add(new Item(items).getDsGrupo());// ADICIONANDO NA LISTA GLOBAL DO GRUPO OS ITENS
                    }
                }

                new GrupoConfig(grupo, Grupo.UPGRADE);// ATUALIZA A LISTA DO ITENS DO GRUPO
                player.closeInventory();
                grupo.setDsLang(player);
                player.sendMessage(Msg.Color("$6Grupo $a$l" + grupo.getDsTraducao() + "$r$6 atualizado com sucesso!"));

            }


            event.setCancelled(true);
        }

    }
}
