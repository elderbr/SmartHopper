package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.LangDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Lang;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.ChatColor;
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
    private String langPlayer;
    private ItemStack itemStack;

    // Grupo
    private int cdGrupo;
    private Grupo grupo;
    private GrupoDao grupoDao;
    private List<Grupo> listGrupo;

    private Item item;

    // Tradução
    private TraducaoDao traducaoDao;

    // LANG
    private Lang lang;
    private LangDao langDao;

    private InventoryCustom inventory;

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
            lang = VGlobal.LANG_NAME_MAP.get(langPlayer);

            if (command.getName().equalsIgnoreCase("grupo")) {
                grupo = new Grupo();
                // LANG DO PLAYER DO BANCO
                grupo.setCdLang(lang.getCdLang());
                grupo.setDsLang(lang.getDsLang());

                // SE O PLAYER DIGITAR O CÓDIGO DO GRUPO OU NOME
                if (cmd.length() > 0) {
                    // VERIFICA SE O COMANDO É UM NÚMERO
                    try {
                        grupo.setCdGrupo(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo.setDsGrupo(cmd);
                    }
                    grupo = grupoDao.select(grupo);
                    if (grupo != null) {
                        grupo = grupoDao.selectListItemGrupo(grupo);

                        // CRIAR UM INVENTARIO COM TODOS OS ITENS DO GRUPO
                        inventory = new InventoryCustom();
                        inventory.create(grupo);
                        grupo.getListItem().forEach(items -> {
                            inventory.addItem(items);
                        });

                        // SE O PLAYER FOR ADM OU OPERADOR ADICIONA LÃ COMO BOTÃO PARA ATUALIZAR O GRUPO
                        if(VGlobal.ADM_UUID.contains(player.getUniqueId().toString())){
                            // CRIANDO O BOTÃO PARA UPDATE
                            ItemStack itemSalve = new ItemStack(Material.LIME_WOOL);
                            ItemMeta meta = itemSalve.getItemMeta();
                            meta.setDisplayName("§eAtualizar");
                            List<String> lore = new ArrayList<>();
                            lore.add(VGlobal.GRUPO_UPDATE);
                            meta.setLore(lore);
                            itemSalve.setItemMeta(meta);
                            inventory.getInventory().setItem(53, itemSalve);
                        }
                        player.openInventory(inventory.getInventory());
                    }
                }
                // SE O PLAYER ESTIVER SEGURANDO UM ITEM NA MÃO
                else {
                    // SE O ITEM FOR IGUAL A AR RETORNA MENSAGEM
                    if (itemStack.getType() == Material.AIR) {
                        Msg.PlayerGold(player, "Segure um item na mão!!!");
                        return false;
                    }

                    // ITEM
                    item = VGlobal.ITEM_NAME_MAP.get(Utils.toItem(itemStack));
                    // LANG DO PLAYER
                    item.setCdLang(lang.getCdLang());
                    item.setDsLang(lang.getDsLang());
                    listGrupo = grupoDao.selectListGrupo(item);
                    // SE O ITEM CONTER MAIS DE UM GRUPO MOSTRA MENSAGEM
                    if (!listGrupo.isEmpty() && listGrupo.size() > 1) {
                        listGrupo.forEach(gp -> {
                            Msg.GrupoPlayer(player, gp);
                        });
                        Msg.PlayerGreenLine(player);// LINE PARA SEPARAR AS MENSAGENS
                        return false;
                    }
                    // BUSCAR O ITEM NO GRUPO COM A TRADUÇÃO
                    else {
                        if (!listGrupo.isEmpty())
                            grupo = grupoDao.selectListItemGrupo(listGrupo.get(0));
                        else
                            grupo = null;
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
                if (cmd.length() > 0) {
                    grupo = new Grupo();
                    getIdTraducao(args);
                    grupo.setDsLang(langPlayer);
                    grupo = grupoDao.select(grupo);
                    if (grupo.getDsTraducao() == null) {
                        getIdTraducao(args);
                        grupo.setCdLang(VGlobal.LANG_NAME_MAP.get(langPlayer).getCdLang());
                        if (traducaoDao.insert(grupo) > 0) {
                            Msg.PlayerGold(player, "Tradução para o grupo ".concat(grupo.getDsGrupo()).concat(" adicionado com sucesso!!!"));
                        } else {
                            Msg.PlayerGold(player, "Erro ao adicionar a tradução para o ".concat(grupo.getDsGrupo()).concat("!!!"));
                        }
                    } else {
                        getIdTraducao(args);
                        if (traducaoDao.update(grupo) > 0)
                            Msg.PlayerGold(player, "Tradução para o grupo ".concat(grupo.getDsGrupo()).concat(" atualizado com sucesso!!!"));
                        else
                            Msg.PlayerGold(player, "Erro ao atualizar a tradução para o ".concat(grupo.getDsGrupo()).concat("!!!"));
                    }
                }
                return false;
            }

            // ADICIONAR NOVO GRUPO
            return addGrupo();


        }

        return false;
    }

    private boolean getIdTraducao(String[] args) {
        cmd = Utils.NAME_ARRAY(args);
        if (args.length > 1) {// 23 Barra de ferro
            try {
                cdGrupo = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                Msg.PlayerRed(player, "Digite o código do grupo antes da sua tradução!!!");
                cdGrupo = 0;
            }

            StringBuilder listName = new StringBuilder();
            if (cdGrupo > 0) {
                for (int i = 1; i < args.length; i++) {
                    listName.append(args[i] + " ");
                }
            }
            grupo.setDsTraducao(listName.toString().trim());
            grupo.setCdGrupo(cdGrupo);
            return true;
        }
        return false;
    }

    private boolean addGrupo() {

        if (command.getName().equalsIgnoreCase("addgrupo")) {
            if (!VGlobal.ADM_UUID.contains(player.getUniqueId().toString())) {
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
        return false;
    }


}
