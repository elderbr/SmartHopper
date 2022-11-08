package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemTabCompleter implements TabCompleter {

    private Item item = null;

    private Player player;
    private String cmd;
    private ItemStack itemStack;
    private List<String> itemList;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Msg.ServidorGold("Comando apenas para jogadores!!!");
            return null;
        }

        if (sender instanceof Player) {
            player = (Player) sender;
            if (command.getName().equalsIgnoreCase("item")) {
                cmd = Utils.NAME_ARRAY(args);// PEGA O NOME DO ITEM DIGITADO
                if (cmd.length() > 0) {
                    itemList = new ArrayList<>();
                    for (String items : VGlobal.TRADUCAO_ITEM_NAME_LIST) {
                        if (items.contains(cmd)) {
                            item = VGlobal.TRADUCAO_ITEM_LIST.get(items);
                            itemList.add(item.toTraducao(player));
                        }
                    }
                    return itemList;
                }
            }
        }
        return Arrays.asList();
    }
}
