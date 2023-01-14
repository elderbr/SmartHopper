package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TraducaoComando implements CommandExecutor {

    private Player player;
    private Grupo grupo;
    private GrupoController grupoController;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "traducaogrupo":
                    grupoController = new GrupoController();
                    try {
                        if (grupoController.addTraducao(player, args)) {
                            grupo = grupoController.getGrupo();
                            Msg.PlayerTodos("Tradução adicionada par ao grupo $9" + grupo.getName() + "$r em $8$l" + player.getLocale() + " $rcomo $8$l" + grupo.toTranslation(player) + "!");
                            return true;
                        } else {
                            Msg.PlayerGold(player, "Ocorreu um erro ao adicionar a tradução!");
                        }
                    } catch (GrupoException e) {
                        Msg.PlayerGold(player, e.getMessage());
                    }
                    break;
                case "traducaoitem":
            }
        }
        return false;
    }
}
