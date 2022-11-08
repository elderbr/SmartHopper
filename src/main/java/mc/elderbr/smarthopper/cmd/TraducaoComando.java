package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
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
    private GrupoController grupoController;

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
                        if (itemController.addTraducao(player, traducao)) {
                            Msg.PlayerTodos("$6A tradução para o item " + item.getName() + " foi alterada pelo $e" + player.getName());
                            return true;
                        }
                    } catch (ItemException e) {
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    return false;
                case "traducaogrupo":
                    grupoController = new GrupoController();
                    try {
                        grupo = grupoController.getGrupo(args[0]).get(0);
                        if (grupoController.addTraducao(player, traducao)) {
                            Msg.PlayerTodos("$9O jogador $e" + player.getName() + "$9 adicionou tradução para o grupo $e" + grupo.getName() + "$9.");
                        }
                        return true;
                    } catch (GrupoException e) {
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    return false;
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
