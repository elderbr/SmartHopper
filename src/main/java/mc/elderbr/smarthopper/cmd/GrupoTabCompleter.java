package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrupoTabCompleter implements TabCompleter {

    private Player player;
    private String cmd;
    private List<String> grupoList;

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
                    if (args.length >= 1) {
                        grupoList = new ArrayList<>();
                        for(String traducao : VGlobal.TRADUCAO_GRUPO.keySet()){
                            if(traducao.contains(cmd)){
                                grupoList.add(traducao);
                            }
                        }
                        return grupoList;
                    }
            }
        }
        return null;
    }
}
