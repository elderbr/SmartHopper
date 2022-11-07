package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TraducaoComando implements CommandExecutor {

    private Player player;
    private StringBuilder traducaoList = new StringBuilder();
    private String traducao = null;

    private String codigo;
    private Item item;
    private Grupo grupo;
    private ItemController itemController;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            traducao = traducao(args);

            switch (command.getName().toLowerCase()) {
                case "traducaoitem":
                    itemController = new ItemController();
                    try {
                        item = itemController.getItem(args[0]);
                        if (itemTraducao()) {
                            Msg.PlayerTodos("$6A tradução para o item " + item.getName() + " foi alterada pelo $e" + player.getName());
                            return true;
                        }
                    }catch (ItemException e){
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    return false;

            }


            // TRADUÇÃO PARA O GRUPO
            if (command.getName().equalsIgnoreCase("traducaogrupo")) {

                if (args.length == 0) {
                    Msg.PlayerRed(player, "Digite o código do grupo!!!");
                    Msg.PlayerGreen(player, "/traducaogrupo <código> <traducao>");
                    return false;
                }

                codigo = args[0];

                if (!Config.CONTAINS_ADD(player)) {
                    Msg.PlayerGold(player, "OPS, você não é adm do Smart Hopper!!!");
                    return false;
                }

                try {
                    grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(codigo));
                } catch (NumberFormatException e) {
                    Msg.PlayerRed(player, "Digite o código do grupo!!!");
                    Msg.PlayerGreen(player, "/traducaogrupo <código> <traducao>");
                    return false;
                }

                if (grupo == null) {
                    Msg.GrupoNaoExiste(player, codigo);
                    return false;
                }

                grupo.addTraducao(player.getLocale(), traducao(args));
                if (GrupoConfig.ADD_TRADUCAO(grupo)) {
                    Msg.PlayerTodos("$9O jogador $e" + player.getName() + "$9 adicionou tradução para o grupo $e" + grupo.getName() + "$9.");
                } else {
                    Msg.PlayerGold(player, "Não foi possivél adicionar a tradução!!!");
                }
            }
        }
        return false;
    }

    private boolean itemTraducao() {
        try {
            itemController.addTraducao(player, traducao);
            return true;
        } catch (ItemException e) {
            Msg.PlayerRed(player, e.getMessage());
        }
        return false;
    }

    private String traducao(String[] cmd) {
        StringBuilder tradStr = new StringBuilder();
        for (int i = 1; i < cmd.length; i++) {
            tradStr.append(cmd[i] + " ");
        }
        return tradStr.toString().trim();
    }
}
