package mc.elderbr.smarthopper.cmd;

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


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            traducao = traducao(args);


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
