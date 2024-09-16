package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrupoTabCompleter implements TabCompleter {

    private Player player;
    private String cmd;
    private GrupoController grupoCtrl = new GrupoController();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "grupo":
                case "addgrupo":
                case "removegrupo":
                    cmd = Utils.NAME_ARRAY(args);
                    return grupoCtrl.findNameContains(player, cmd);
            }
        }
        return null;
    }
}
