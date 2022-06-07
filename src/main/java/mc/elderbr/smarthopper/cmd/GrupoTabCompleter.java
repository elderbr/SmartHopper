package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GrupoTabCompleter implements TabCompleter {

    private Player player;
    private String cmd;

    private Grupo grupo;
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

                    if (args.length == 1) {
                        grupoList = new ArrayList<>();
                        for (Map.Entry<String, Grupo> grups : VGlobal.TRADUCAO_GRUPO_LIST.entrySet()) {
                            if(grups.getKey().contains(cmd)) {
                                Grupo grupo = grups.getValue();
                                grupo.setDsLang(player.getLocale());
                                grupoList.add(grupo.toTraducao());
                            }
                        }
                        return grupoList;
                    }
            }
        }
        return null;
    }
}
