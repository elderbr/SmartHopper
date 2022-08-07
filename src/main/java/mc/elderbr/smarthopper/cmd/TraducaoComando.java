package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.*;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TraducaoComando implements CommandExecutor {

    private Player player;
    private Item item;
    private Adm jogador;
    private boolean isOP = false;

    private Lang lang;

    private String[] traducaoArgs;
    private StringBuilder traducaoList = new StringBuilder();
    private Traducao traducao;

    private Grupo grupo;
    private int cdGrupo;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            jogador = new Adm(player);

            traducaoArgs = args;

            if (command.getName().equalsIgnoreCase("addTraducao")) {

                lang = VGlobal.LANG_MAP.get(player.getLocale());

                if (args.length > 0 && args[0].equalsIgnoreCase("grupo")) {

                    if (args.length > 2) {

                        try {
                            cdGrupo = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            Msg.PlayerGold(player, "Código do grupo é invalido!!!");
                            return false;
                        }
                        grupo = VGlobal.GRUPO_MAP_ID.get(cdGrupo);
                        grupo.setCdLang(lang.getCdLang());
                        if (grupo != null) {
                            grupo.setDsTraducao(toGrupoTraducao());

                                VGlobal.GRUPO_MAP_ID.get(grupo.getCdGrupo()).addTraducao(lang.getDsLang(), grupo.getDsTraducao());
                                VGlobal.GRUPO_MAP_NAME.get(grupo.getDsGrupo()).addTraducao(lang.getDsLang(), grupo.getDsTraducao());
                                Msg.PlayerGreen(player, "Tradução do grupo " + grupo.getDsGrupo() + " adicionado com sucesso!!!");

                        } else {
                            Msg.PlayerGreen(player, "Grupo do código " + args[1] + " não foi encontrado!!!");
                        }
                    } else {
                        Msg.PlayerGold(player, "Digite o código do grupo e a seu tradução!!!");
                    }
                    return false;
                }

                if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    Msg.PlayerGold(player, "Segure um item na mão para adicionar a tradução!!!");
                    return false;
                }

                item = VGlobal.ITEM_MAP_NAME.get(new Item(player.getInventory().getItemInMainHand()).getDsItem());
                item.setCdLang(jogador.getLang().getCdLang());

                // VERIFICA SE O JOGADOR ESTA NA LISTA DE ADMINISTRADORES
                isOP = false;
                for (Jogador jg : VGlobal.JOGADOR_LIST) {
                    if (jg.getUUID().equals(jogador.getUUID())) {
                        isOP = true;
                        break;
                    }
                }

                if (!isOP) {// VERIFICA SE O JOGADOR É ADM
                    Msg.PlayerGold(player, "Você não tem permissão para usar esse comando!!!");
                    return false;
                }

                if (args.length == 0) {// VERIFICA SE EXISTE TEXTO PARA A TRADUÇÃO
                    Msg.PlayerGold(player, "Digite a tradução!!!");
                    return false;
                }

                item.setDsTraducao(toTraducao());

                traducao = VGlobal.TRADUCAO_MAP_ITEM_NAME.get(item.getCdItem());



            }
        }

        return false;
    }

    private String toTraducao() {
        traducaoList = new StringBuilder();
        for (String value : traducaoArgs) {
            traducaoList.append(value.concat(" "));
        }
        return traducaoList.toString().trim();
    }

    private String toGrupoTraducao() {
        traducaoList = new StringBuilder();
        for (int i = 2; i < traducaoArgs.length; i++) {
            String value = traducaoArgs[i];
            traducaoList.append(value.concat(" "));
        }
        return traducaoList.toString().trim();
    }
}
