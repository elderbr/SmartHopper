package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemComando implements CommandExecutor {

    private Item item = null;
    private Player player;
    private String cmd;

    private ItemStack itemStack;
    private ItemController itemController;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO

            if (command.getName().equalsIgnoreCase("item")) {
                itemController = new ItemController();
                try {
                    if (cmd.length() == 0) {// VERIFICA SE NÃO FOI DIGITADO E SE O ITEM É DIFERENTE DE AR
                        item = itemController.getItem(itemStack);
                    } else {
                        item = itemController.getItem(cmd);
                    }
                    Msg.Item(player, item);
                    return true;
                } catch (ItemException e) {
                    Msg.PlayerGold(player, e.getMessage());
                }
            }

        }
        return false;
    }
}