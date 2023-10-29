package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfigComando implements CommandExecutor {

    private Player myPlayer;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            myPlayer = player;

            switch (command.getName().toLowerCase()) {
                case "reload":
                    if(player.isOp() && !Config.CONTAINS_ADD(player)){
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
