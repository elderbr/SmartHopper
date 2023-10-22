package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
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

    private Item item;
    private ItemController itemController;
    private Grupo grupo;
    private GrupoController grupoController;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "traducaoitem":
                    itemController = new ItemController();
                    try {
                        if (itemController.addTranslation(player, args)) {
                            item = itemController.getItem();
                            Msg.PlayerTodos("Tradução adicionada para o item $e" + item.getName() + "$r em $e" + player.getLocale() + "$r como $e" + item.toTranslation(player)+"$r!");
                            return true;
                        } else {
                            Msg.PlayerGold(player, "Erro ao tentar adicionar a tradução para o item!!!");
                        }
                    } catch (ItemException e) {
                        Msg.PlayerGold(player, e.getMessage());
                    }
                    break;
                case "traducaogrupo":
                    grupoController = new GrupoController();
                    try {
                        if (grupoController.addTraducao(player, args)) {
                            //grupo = grupoController.findByGrupoItemStack();
                            Msg.PlayerTodos("Tradução adicionada para o grupo $9" + grupo.getName() + "$r em $8$l" + player.getLocale() + " $rcomo $8$l" + grupo.toTranslation(player) + "!");
                            return true;
                        } else {
                            Msg.PlayerGold(player, "Ocorreu um erro ao adicionar a tradução!");
                        }
                    } catch (GrupoException e) {
                        Msg.PlayerGold(player, e.getMessage());
                    }
                    break;
            }
        }
        return false;
    }
}
