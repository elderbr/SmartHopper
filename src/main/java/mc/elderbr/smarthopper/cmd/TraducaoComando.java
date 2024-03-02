package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.controllers.MessageController;
import mc.elderbr.smarthopper.enums.MessageType;
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
    private GrupoController grupoController = new GrupoController();

    private MessageController msgController = new MessageController();

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
                            Msg.PlayerTodos(msgController.select(MessageType.TRANSLATION_ITEM_ADD, player, item, player.getLocale()));
                            return true;
                        } else {
                            Msg.PlayerGold(player, msgController.select(MessageType.TRANSLATION_ERRO));
                        }
                    } catch (ItemException e) {
                        Msg.PlayerGold(player, e.getMessage());
                    }
                    break;
                case "traducaogrupo":
                    try {
                        if (grupoController.addTraducao(player, args)) {
                            grupo = grupoController.getGrupo();
                            Msg.PlayerTodos(msgController.select(MessageType.TRANSLATION_GROUP_ADD, player, grupo, grupo.toTranslation(player)));
                            return true;
                        } else {
                            Msg.PlayerGold(player, msgController.select(MessageType.TRANSLATION_ERRO));
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
