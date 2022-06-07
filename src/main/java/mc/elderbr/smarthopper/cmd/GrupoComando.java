package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.GrupoDao;
<<<<<<< HEAD
import mc.elderbr.smarthopper.dao.LangDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.interfaces.Traducao;
=======
>>>>>>> v4.0.0
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.*;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GrupoComando implements CommandExecutor {

    private Command command;

    private Player player;
    private String cmd;
<<<<<<< HEAD
    private String langPlayer;
=======

    public static Grupo GRUPO;
    private List<Grupo> listGrupo;
>>>>>>> v4.0.0
    private ItemStack itemStack;

    // Grupo
    private int cdGrupo;
    private Grupo grupo;
    private GrupoDao grupoDao;
    private List<Grupo> listGrupo;

    private Item item;
    private List<Item> listItem;

    // Tradução
    private TraducaoDao traducaoDao;

    // LANG
    private Lang lang;
    private LangDao langDao;

    private InventoryCustom inventory;
    private Traducao traducao;


    public GrupoComando() {
        grupoDao = new GrupoDao();
        traducaoDao = new TraducaoDao();
        langDao = new LangDao();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        this.command = command;

        if (sender instanceof Player) {
            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args);// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO

            // PEGANDO O LANG DO PLAYER
            langPlayer = player.getLocale();// LINGUAGEM DO JOGADOR
            // SE O LANG NÃO EXISTIR CRIAR NO BANCO
            if (langDao.select(langPlayer) == null) {
                langDao.insert(langPlayer);
                langDao.selectAll();
            }
            lang = VGlobal.LANG_NAME_MAP.get(langPlayer);

            if (command.getName().equalsIgnoreCase("grupo")) {
                grupo = new Grupo();
                // LANG DO PLAYER DO BANCO
                grupo.setLang(lang);

<<<<<<< HEAD
                // SE O PLAYER DIGITAR O CÓDIGO DO GRUPO OU NOME
=======
                GRUPO = null;
>>>>>>> v4.0.0
                if (cmd.length() > 0) {
                    // VERIFICA SE O COMANDO É UM NÚMERO
                    try {
<<<<<<< HEAD
                        grupo.setCdGrupo(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo.setDsGrupo(cmd);
                    }
                    grupo = grupoDao.select(grupo);
                    if (grupo != null) {
                        listItem = grupoDao.selectListItemGrupo(grupo);

                        // CRIAR UM INVENTARIO COM TODOS OS ITENS DO GRUPO
                        inventory = new InventoryCustom();
                        inventory.create(grupo);
                        if (listItem.size()>0) {
                            listItem.forEach(items -> {
                                inventory.addItem(items);
                            });
                        }

                        // SE O PLAYER FOR ADM OU OPERADOR ADICIONA LÃ COMO BOTÃO PARA ATUALIZAR O GRUPO
                        if (VGlobal.ADM_UUID.contains(player.getUniqueId().toString())) {
                            // CRIANDO O BOTÃO PARA UPDATE
                            ItemStack itemSalve = new ItemStack(Material.LIME_WOOL);
                            ItemMeta meta = itemSalve.getItemMeta();
                            meta.setDisplayName("§eAtualizar");
                            List<String> lore = new ArrayList<>();
                            lore.add(VGlobal.GRUPO_UPDATE);
=======
                        GRUPO = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        GRUPO = VGlobal.TRADUCAO_GRUPO_LIST.get(cmd);
                    }
                    if (GRUPO != null) {
                        GRUPO.setDsLang(player);

                        if (!GRUPO.getListItem().isEmpty()) {
                            inventoryCustom = new InventoryCustom();
                            inventoryCustom.create(GRUPO.toTraducao().concat(Msg.Color(" $lID:$r" + GRUPO.getCdGrupo())));
                            for (Item items : GRUPO.getListItem()) {
                                inventoryCustom.addItem(items.getItemStack());
                            }
                            // SE FOR ADM OU OPERADOR ADICIONA O BOTÃO PARA SALVAR OU ALTERAR
                            if (VGlobal.ADM_LIST.contains(player.getName())) {
                                // CRIANDO O BOTÃO PARA SALVAR
                                itemSalve = new ItemStack(Material.LIME_WOOL);
                                meta = itemSalve.getItemMeta();
                                meta.setDisplayName(Msg.Color("$aAtualizar"));
                                lore = new ArrayList<>();
                                lore.add(Msg.Color("$3Salvar"));
                                meta.setLore(lore);
                                itemSalve.setItemMeta(meta);
                                inventoryCustom.getInventory().setItem(53, itemSalve);
                            }
                            player.openInventory(inventoryCustom.getInventory());
                        }
                        player.sendMessage(Msg.Color("$2Grupo: $6" + GRUPO.toTraducao() + " $e$lID: " + GRUPO.getCdGrupo()));
                    } else {
                        player.sendMessage(Msg.Color("$2O grupo $e" + cmd + " $6não existe!"));
                    }
                } else {

                    if (itemStack.getType() == Material.AIR) {
                        Msg.PlayerRed(player, "Segure um item na mão ou escreva o nome do grupo ou seu ID!!!");
                        return false;
                    }

                    Item item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getDsItem());
                    item.setDsLang(player);

                    listGrupo = GrupoDao.SELECT_GRUPO_ITEM(item);

                    if (listGrupo.isEmpty()) {
                        player.sendMessage(Msg.Color("$2Não existe grupo para o item $e" + item.toTraducao() + "!"));
                        return false;
                    }

                    if (listGrupo.size() == 1) {
                        GRUPO = VGlobal.GRUPO_MAP_NAME.get(listGrupo.get(0).getDsGrupo());
                        GRUPO.setDsLang(player);
                        inventoryCustom = new InventoryCustom();
                        inventoryCustom.create(GRUPO.toTraducao().concat(Msg.Color(" $lID:$r" + GRUPO.getCdGrupo())));
                        for (Item items : GRUPO.getListItem()) {
                            inventoryCustom.addItem(items.getItemStack());
                        }

                        // SE FOR ADM OU OPERADOR ADICIONA O BOTÃO PARA SALVAR OU ALTERAR
                        if (VGlobal.ADM_LIST.contains(player.getName())) {
                            // CRIANDO O BOTÃO PARA SALVAR
                            itemSalve = new ItemStack(Material.LIME_WOOL);
                            meta = itemSalve.getItemMeta();
                            meta.setDisplayName(Msg.Color("$aAtualizar"));
                            lore = new ArrayList<>();
                            lore.add(Msg.Color("$3Salvar"));
>>>>>>> v4.0.0
                            meta.setLore(lore);
                            itemSalve.setItemMeta(meta);
                            inventory.getInventory().setItem(53, itemSalve);
                        }
<<<<<<< HEAD
=======
                        player.openInventory(inventoryCustom.getInventory());
                        player.sendMessage(Msg.Color("$2Grupo: $6" + GRUPO.toTraducao() + " $e$lID: " + GRUPO.getCdGrupo()));
                        return false;
                    }

                    player.sendMessage("=====================================================");
                    for (Grupo grupo : listGrupo) {
                        grupo.setDsLang(player);
                        player.sendMessage(Msg.Color("$2Grupo: $6" + grupo.toTraducao() + " $e$lID: " + grupo.getCdGrupo()));
                    }
                    player.sendMessage("=====================================================");
                }

            }

            /************  ADICIONA NOVO GRUPO     ************/
            if (command.getName().contentEquals("addgrupo")) {

                if (VGlobal.ADM_LIST.contains(player.getName())) {
                    if (cmd.length() > 4) {

                        GRUPO = new Grupo();
                        GRUPO.setDsGrupo(cmd);
                        GRUPO.setDsTraducao(cmd);

                        inventory = new InventoryCustom();
                        inventory.createNewGrupo(cmd);

                        // CRIANDO O BOTÃO PARA SALVAR
                        itemSalve = new ItemStack(Material.LIME_WOOL);
                        meta = itemSalve.getItemMeta();
                        meta.setDisplayName(Msg.Color("$aSalva"));
                        lore = new ArrayList<>();
                        lore.add(Msg.Color("$3Salva novo grupo"));
                        meta.setLore(lore);
                        itemSalve.setItemMeta(meta);

                        inventory.getInventory().setItem(53, itemSalve);

>>>>>>> v4.0.0
                        player.openInventory(inventory.getInventory());
                    }
                }
<<<<<<< HEAD
                // SE O PLAYER ESTIVER SEGURANDO UM ITEM NA MÃO
                else {
                    // SE O ITEM FOR IGUAL A AR RETORNA MENSAGEM
                    if (itemStack.getType() == Material.AIR) {
                        Msg.PlayerGold(player, "Segure um item na mão!!!");
=======
            }

            /************  REMOVER GRUPO     ************/
            if (command.getName().contentEquals("removegrupo")) {
                if (VGlobal.ADM_LIST.contains(player.getName())) {
                    if (cmd.length() > 0) {
                        try {
                            GRUPO = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                        } catch (NumberFormatException e) {
                            GRUPO = VGlobal.GRUPO_MAP_NAME.get(cmd);
                        }
                        if (GRUPO != null) {
                            if (GrupoDao.DELETE(GRUPO)) {

                                // REMOVENDO O GRUPO DAS VARIAVEIS GLOBAL
                                VGlobal.GRUPO_MAP_NAME.get(GRUPO.getDsGrupo());
                                VGlobal.GRUPO_MAP_ID.get(GRUPO.getCdGrupo());
                                VGlobal.GRUPO_MAP.get(GRUPO.getDsGrupo());
                                VGlobal.GRUPO_NAME_LIST.remove(GRUPO.getDsGrupo());

                                // TRADUÇÃO
                                VGlobal.TRADUCAO_GRUPO_LIST.remove(GRUPO.getDsGrupo());
                                VGlobal.TRADUCAO_MAP_GRUPO_NAME.remove(GRUPO.getCdGrupo());

                                Msg.PlayerTodos(Msg.Color("$6O jogador " + player.getName() + " deletou o grupo $a$l" + GRUPO.getDsGrupo() + "!"));
                            }else{
                                Msg.PlayerRed(player, "Ocorreu um erro ao deletar o grupo "+ GRUPO.getDsGrupo()+"!!!");
                            }
                        }else{
                            Msg.PlayerRed(player, "O grupo "+ cmd +" não existe!!!");
                        }
                        return false;
                    } else {
                        player.sendMessage("Escreva o nome do grupo que deseja apagar!!!\n/removegrupo <grupo>");
>>>>>>> v4.0.0
                        return false;
                    }

                    // ITEM
                    item = VGlobal.ITEM_NAME_MAP.get(Utils.toItem(itemStack));
                    item.setDsLang(langPlayer);
                    listGrupo = grupoDao.selectListGrupo(item);
                    if(listGrupo.isEmpty()){
                        Msg.PlayerGold(player,"Não foi encontrado grupo do item!!!");
                        return false;
                    }
                    // SE O ITEM CONTER MAIS DE UM GRUPO MOSTRA MENSAGEM
                    if (listGrupo.size() > 1) {
                        listGrupo.forEach(gp -> {
                            Msg.GrupoPlayer(player, gp);
                        });
                        Msg.PlayerGreenLine(player);// LINE PARA SEPARAR AS MENSAGENS
                        return false;
                    }
                    // BUSCAR O ITEM NO GRUPO COM A TRADUÇÃO
                    else {
                        grupo = listGrupo.get(0);
                        listItem = grupoDao.selectListItemGrupo(grupo);
                        // CRIAR UM INVENTARIO COM TODOS OS ITENS DO GRUPO
                        inventory = new InventoryCustom();
                        inventory.create(grupo);
                        if (!listItem.isEmpty()) {
                            listItem.forEach(items -> {
                                inventory.addItem(items);
                            });
                        }
                        player.openInventory(inventory.getInventory());
                    }
                }
                if (grupo != null) {
                    Msg.GrupoPlayer(player, grupo);
                    Msg.PlayerGreenLine(player);
                } else {
                    Msg.PlayerRed(player, "Grupo não encontrado!!!");
                }
                return false;
            }

            if (command.getName().equalsIgnoreCase("traducaoGrupo")) {
                if (!VGlobal.ADM_UUID.contains(player.getUniqueId().toString())) {
                    Msg.PlayerRed(player, "Apenas os Adm ou operador podem adicionar tradução para o grupo!!!");
                    return false;
                }
                if (cmd.length() > 0) {
                    grupo = new Grupo();
                    isCodeName(args);
                    traducao = traducaoDao.selectGrupo(grupo);
                    if (traducao == null) {
                        if (traducaoDao.insert(grupo) > 0)
                            Msg.PlayerGreen(player, "Tradução para o grupo adicionada com sucesso!!!");
                        else
                            Msg.PlayerGreen(player, "Erro ao adicionar tradução para o grupo!!!");
                    } else {
                        grupo.setCdTraducao(traducao.getCdTraducao());
                        if (traducaoDao.update(grupo) > 0)
                            Msg.PlayerGreen(player, "Tradução do grupo atualizada com sucesso!!!");
                        else
                            Msg.PlayerGreen(player, "Erro ao atualizar a tradução do grupo!!!");
                    }
                }
            }
            return false;
        }

        // ADICIONAR NOVO GRUPO
        if (command.getName().equalsIgnoreCase("addgrupo")) {
            if (!VGlobal.ADM_UUID.contains(player.getUniqueId().toString())) {
                Msg.PlayerRed(player, "Apenas os Adm ou operador podem adicionar grupo!!!");
                return false;
            }

            if (cmd.length() > 2) {

                grupo = new Grupo();
                grupo.setDsGrupo(cmd);
                grupo.setCdLang(lang.getCdLang());
                grupo.setDsLang(lang.getDsLang());

                grupo = grupoDao.select(grupo);

                if (grupo == null) {
                    inventory = new InventoryCustom();
                    inventory.createNewGrupo(cmd);

                    // CRIANDO O BOTÃO PARA SALVAR
                    ItemStack itemSalve = new ItemStack(Material.LIME_WOOL);
                    ItemMeta meta = itemSalve.getItemMeta();
                    meta.setDisplayName("§eSalva");
                    List<String> lore = new ArrayList<>();
                    lore.add(VGlobal.GRUPO_SALVA);
                    meta.setLore(lore);
                    itemSalve.setItemMeta(meta);
                    inventory.getInventory().setItem(53, itemSalve);

                    player.openInventory(inventory.getInventory());
                } else {
                    Msg.PlayerGold(player, String.format("O grupo %s já existe!!!", grupo.toString()));
                }
            } else {
                Msg.PlayerGold(player, "Digite o nome do grupo com mais de 2 caracteres!!!");
            }
        }

        // REMOVER GRUPO
        if (command.getName().equalsIgnoreCase("removegrupo")) {
            if (!VGlobal.ADM_UUID.contains(player.getUniqueId().toString())) {
                Msg.PlayerRed(player, "Apenas os Adm ou operador podem remover grupo!!!");
                return false;
            }
            if (cmd.length() > 0) {
                grupo = VGlobal.GRUPO_NAME_MAP.get(cmd);
                if (grupo != null) {
                    if (grupoDao.delete(grupo)) {
                        grupoDao.deleteItem(grupo);// APAGA OS ITEM DO GRUPO
                        traducaoDao.delete(grupo);// APAGA A TRADUÇÃO DO GRUPO
                        Msg.PlayerGold(player, String.format("Grupo %s apagado com sucesso!!", grupo.toString()));
                    }
                } else {
                    Msg.PlayerRed(player, "Esse grupo não foi existe!!!");
                }
            } else {
                Msg.PlayerGold(player, "Digite o código ou nome do grupo!!!");
            }
        }
        return false;
    }

    private void isCodeName(String[] args) {
        cmd = Utils.NAME_ARRAY(args);
        if (args.length > 1) {// 23 Barra de ferro
            try {
                grupo.setCdGrupo(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                grupo.setDsGrupo(args[0]);
            }
            StringBuilder listName = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                listName.append(args[i] + " ");
            }
            grupo.setDsTraducao(listName.toString().trim());
            grupo.setLang(lang);
        }
    }
}
