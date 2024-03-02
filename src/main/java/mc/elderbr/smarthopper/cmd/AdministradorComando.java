package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.controllers.MessageController;
import mc.elderbr.smarthopper.enums.MessageType;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdministradorComando implements CommandExecutor {

    AdmController admCtrl = new AdmController();
    private MessageController msgController = new MessageController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            switch (command.getName().toLowerCase()) {
                case "addadm":
                    try {
                        admCtrl.addAdm(player, args);
                        Msg.PlayerTodos(msgController.select(MessageType.ADM_NEW, args[0]));
                        return true;
                    } catch (Exception e) {
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    break;
                case "removeradm":
                    try {
                        admCtrl.removeAdm(player, args);
                        Msg.PlayerTodos(msgController.select(MessageType.ADM_REMOVE, args[0]));
                        return true;
                    } catch (Exception e) {
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    break;
                case "reload":
                    if (!player.isOp() && !admCtrl.containsAdm(player)) {
                        Msg.PlayerGold(player, msgController.select(MessageType.NOT_PERMISSION));
                        return false;
                    }
                    Msg.PlayerTodos(msgController.select(MessageType.RESTARTER));
                    ItemController.findAll();
                    GrupoController.findAll();
                    new TraducaoConfig();
                    Msg.PlayerTodos(msgController.select(MessageType.RELOAD));
                    return true;
            }
        }
        return false;
    }
}
