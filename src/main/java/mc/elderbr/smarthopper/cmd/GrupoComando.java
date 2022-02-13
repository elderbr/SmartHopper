package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GrupoComando implements CommandExecutor {

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


    public GrupoComando() {
        grupoDao = new GrupoDao();
        traducaoDao = new TraducaoDao();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO
            langPlayer = player.getLocale();// LINGUAGEM DO JOGADOR

            if (command.getName().equalsIgnoreCase("grupo")) {
                grupo = new Grupo();
                grupo.setDsLang(langPlayer);
                if (cmd.length() > 0) {
                    // VERIFICA SE O COMANDO É UM NÚMERO
                    try {
                        grupo.setCdGrupo(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo.setDsGrupo(cmd);
                    }
                    grupo = grupoDao.select(grupo);
                } else {
                    item = VGlobal.ITEM_NAME_MAP.get(Utils.toItem(itemStack));
                    item.setDsLang(langPlayer);
                    listGrupo = grupoDao.selectListGrupo(item);
                    if (listGrupo.size() > 1) {
                        listGrupo.forEach(gp->{
                            Msg.GrupoPlayer(player, gp);
                        });
                    } else {
                        if (!listGrupo.isEmpty()) {
                            Msg.GrupoPlayer(player, listGrupo.get(0));
                        } else {
                            Msg.PlayerRed(player, "Grupo não encontrado!!!");
                        }
                    }
                    return false;
                }
                if (grupo != null) {
                    Msg.GrupoPlayer(player, grupo);
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

}
