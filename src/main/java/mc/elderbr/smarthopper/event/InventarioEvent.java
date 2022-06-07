package mc.elderbr.smarthopper.event;

<<<<<<< HEAD
import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.LangDao;
import mc.elderbr.smarthopper.file.Config;
=======
import mc.elderbr.smarthopper.cmd.GrupoComando;
import mc.elderbr.smarthopper.dao.GrupoDao;
>>>>>>> v4.0.0
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Lang;
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
    private ItemStack itemNovo;

    // ITEM
    private Item item;

    // GRUPO
    private String nameGrupo;
    private Grupo grupo;
    private GrupoDao grupoDao = new GrupoDao();
    private List<String> lore;

    private Inventory inventory;

    private Adm adm;

    private Lang lang;
    private LangDao langDao = new LangDao();

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        lang = VGlobal.LANG_NAME_MAP.get(player.getLocale());
        adm = new Adm(player);

        inventory = player.getOpenInventory().getTopInventory();
        itemStack = event.getCurrentItem();

        titulo = event.getView().getTitle();// NOME DO INVENTARIO

        // VERIFICA SE O ITEM É DIFERENTE DE NULL, SE O INVENTARIO É CHEST E SE O PLAYER É ADM OU OPERADOR
<<<<<<< HEAD
        if (itemStack == null || itemStack.getType() == Material.AIR || inventory.getType() != InventoryType.CHEST) {
            return;
        }

        // SE O INVENTARIO RECEBER O NOME DE NOVO GRUPO OU GRUPO CANCELA A MOVIMENTAÇÃO DO ITEM
        if (titulo.contains(Msg.Color(VGlobal.GRUPO_NOVO_INVENTORY)) || titulo.contains(Msg.Color(VGlobal.GRUPO_INVENTORY))) {
=======
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
                if (GrupoDao.INSERT(grupoNovo)) {
                    Msg.PlayerTodos(Msg.Color("$6O jogador "+ player.getName() +" criou o grupo $a$l" + grupoNovo.getDsGrupo() + "!"));
                }else{
                    Msg.PlayerTodos(Msg.Color("$4Ocorreu um erro ao criar o grupo $e&l "+ grupoNovo.getDsGrupo()+"$r!!!"));
                }
            }

            // ALTERANDO GRUPO
            if (event.getClick().isLeftClick() && itemClick.getItemMeta().getLore() != null && itemClick.getItemMeta().getLore().contains(Msg.Color("$3Salvar"))) {

                nameGrupo = titulo.substring(Msg.Color("$8$lGrupo: $r").length(), titulo.indexOf(Msg.Color(" $lID"))).trim().toLowerCase();

                grupo = GrupoComando.GRUPO;// BUSCA NO BANCO O GRUPO

                GrupoDao.DELETE_GRUPO_ITEM(grupo);// APAGANGO A LISTA DE ITEM DO GRUPO NO BANCO DE DADOS
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
                GrupoDao.INSERT_GRUPO_ITEM(grupo);// ADICIONANDO OS ITEM DO GRUPO

                grupo.setDsLang(player);
                Bukkit.getServer().broadcastMessage(Msg.Color("$6O jogador "+ player.getName() +" atualizou o grupo $a$l" + grupo.getDsGrupo() + "!"));
                player.sendMessage(Msg.Color("$6Grupo $a$l" + grupo.toTraducao() + "$r$6 atualizado com sucesso!"));

            }


>>>>>>> v4.0.0
            event.setCancelled(true);

            // SE O PLAYER É O ADM OU OPERADOR
            if (VGlobal.ADM_UUID.contains(adm.getDsUuid())) {
                // SE O ITEM CONTEM LORE
                lore = new ArrayList<>();
                if (itemStack != null && itemStack.getItemMeta().getLore() != null) {
                    lore = itemStack.getItemMeta().getLore();
                }

                // SE CLICADO COM O BOTÃO ESQUERDO ADICIONA ITEM
                if (event.isLeftClick() && !inventory.contains(itemStack)) {
                    event.getInventory().addItem(itemStack);
                }
                // SE CLICADO COM O BOTÃO DIREITO REMOVE ITEM
                if (event.isRightClick() && inventory.contains(itemStack) && !lore.contains(VGlobal.GRUPO_SALVA) && !lore.contains(VGlobal.GRUPO_UPDATE)) {
                    event.getInventory().removeItem(itemStack);
                }
                // SE CLICADO NA LÃ SALVAR ADICIONA NO BANCO O NOVO GRUPO
                if (event.isLeftClick() && lore.contains(VGlobal.GRUPO_SALVA)) {
                    grupo = new Grupo();
                    grupo.setDsGrupo(titulo.replace(VGlobal.GRUPO_NOVO_INVENTORY, ""));
                    // LANG
                    grupo.setCdLang(lang.getCdLang());
                    grupo.setDsLang(lang.getDsLang());

                    player.closeInventory();// FECHA INVENTARIO

                    int cdGrupo = grupoDao.insert(grupo);
                    if (cdGrupo > 0) {
                        // REMOVENDO O BOTÃO LÃ
                        grupo.setCdGrupo(cdGrupo);
                        inventory.remove(inventory.getItem(53));
                        for (ItemStack items : inventory.getStorageContents()) {
                            if (items == null) continue;
                            // ADICIONANDO O ITEM AO GRUPO
                            itemNovo = new ItemStack(items.getType(), 1);
                            item = VGlobal.ITEM_NAME_MAP.get(new Item(itemNovo).toString());
                            grupo.addItem(item);
                            // ADICIONANDO O ITEM DO GRUPO NO BANCO
                            grupoDao.insertItem(grupo, item);
                        }
                        Msg.PlayerGold(player, "Grupo salvo com sucesso!!!");
                    } else {
                        Msg.PlayerGold(player, "Erro ao criar novo grupo!!!");
                    }
                }
                if (event.isLeftClick() && lore.contains(VGlobal.GRUPO_UPDATE)) {

                    nameGrupo = titulo.replaceAll(VGlobal.GRUPO_INVENTORY, "");
                    nameGrupo = nameGrupo.substring(0, nameGrupo.indexOf("§8§lID")).trim();
                    Msg.ServidorGold("nome do grupo >> "+ nameGrupo, getClass());
                    lang  = VGlobal.LANG_NAME_MAP.get(player.getLocale());

                    grupo = new Grupo();
                    grupo.setDsGrupo(nameGrupo);
                    grupo.setLang(lang);

                    grupo = grupoDao.select(grupo);
                    player.closeInventory();

                    int retorno = 0;
                    if (grupo != null) {

                        inventory.removeItem(inventory.getItem(53));

                        grupoDao.deleteItem(grupo);
                        for (ItemStack items : inventory.getStorageContents()) {
                            if (items == null) continue;
                            itemNovo = new ItemStack(items.getType(), 1);
                            retorno = grupoDao.insertItem(grupo, VGlobal.ITEM_NAME_MAP.get(Utils.toItem(itemNovo)));
                        }
                    }else{
                        Msg.PlayerRed(player, "Esse grupo não existe!!!");
                    }

                    if(retorno>0) {
                        Msg.PlayerGold(player, "Grupo atualizado com sucesso!!!");
                    }else{
                        Msg.PlayerRed(player, "Erro ao atualizar o grupo!!!");
                    }

                }

            }

        }


    }
}
