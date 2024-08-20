package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdministradorComando implements CommandExecutor {

    AdmController admCtrl = new AdmController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            switch (command.getName().toLowerCase()) {
                case "addadm":
                    try {
                        admCtrl.addAdm(player, args);
                        Msg.PlayerTodos(String.format("$lO jogador $e%s $r$lé o novo administrador do $2Smart Hopper!!!", args[0]));
                        return true;
                    } catch (Exception e) {
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    break;
                case "removeradm":
                    try {
                        admCtrl.removeAdm(player, args);
                        Msg.PlayerTodos("$lO jogador $e"+ args[0] +"$r$l foi $cremovido$r$l do Adm do $2Smart Hopper!!!");
                        return true;
                    } catch (Exception e) {
                        Msg.PlayerRed(player, e.getMessage());
                    }
                    break;
                case "reload":
                    if(!player.isOp() && !admCtrl.containsAdm(player)){
                        Msg.PlayerGold(player, "Você não tem permissão!!!");
                        return false;
                    }
                    Msg.PlayerTodos("Smart Hopper foi reiniciado...");
                    ItemController.findAll();
                    GrupoController.findAll();
                    new TraducaoConfig();
                    Msg.PlayerTodos("Dados do Smart Hopper carregados...");
                    return true;
            }

        }
        return false;
    }
}
