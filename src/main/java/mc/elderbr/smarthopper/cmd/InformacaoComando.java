package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InformacaoComando implements CommandExecutor {

    private Player player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "tutorial":
                    tutorial();
                    return true;
            }
        }
        return false;
    }

    private void tutorial() {
        Msg.PulaPlayer(player);
        player.sendMessage(Msg.Color("$eTutorial de como usar:"));
        player.sendMessage(Msg.Color("$2https://youtu.be/fBIeZ57ka1M?si=ZqE5TQRG2KjlaHdD"));
        player.sendMessage("https://www.spigotmc.org/resources/separador-inteligente.73646/");
        Msg.PulaPlayer(player);
    }
}
