package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdministradorTabCompleter implements TabCompleter {

    private Player player;
    private String cmd;

    private List<String> list;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;
            switch (command.getName().toLowerCase()) {
                case "addadm":
                case "removeradm":
                    list = new ArrayList<>();
                    for (String jogador : VGlobal.ADM_LIST) {
                        if(jogador.equals(player.getName())) continue;
                        list.add(jogador);
                    }
                    return list;
            }
        }
        return null;
    }
}
