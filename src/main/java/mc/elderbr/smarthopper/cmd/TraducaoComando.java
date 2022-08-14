package mc.elderbr.smarthopper.cmd;

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

    private String codigo;
    private Item item;
    private Grupo grupo;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;

            if (command.getName().equalsIgnoreCase("traducaoitem")) {

                if (args.length == 0) {
                    Msg.PlayerRed(player, "Digite o código do item!!!");
                    Msg.PlayerGreen(player, "/traducaoitem <código> <traducao>");
                    return false;
                }

                codigo = args[0];

                if (!Config.CONTAINS_ADD(player)) {
                    Msg.PlayerGold(player, "OPS, você não é adm do Smart Hopper!!!");
                    return false;
                }

                try {
                    item = VGlobal.ITEM_MAP_ID.get(Integer.parseInt(codigo));
                } catch (NumberFormatException e) {
                    Msg.PlayerRed(player, "Digite o código do item!!!");
                    Msg.PlayerGreen(player, "/traducaoitem <código> <traducao>");
                    return false;
                }

                if (item == null) {
                    Msg.ItemNaoExiste(player, codigo);
                    return false;
                }
                item.addTraducao(player.getLocale(), traducao(args));
                if (ItemConfig.ADD_TRADUCAO(item)) {
                    Msg.PlayerTodos("§6A tradução para o item " + item.getName() + " foi alterada pelo §e" + player.getName());
                } else {
                    Msg.ItemNaoExiste(player, codigo);
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
                GrupoConfig.ADD_TRADUCAO(grupo);
            }
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
