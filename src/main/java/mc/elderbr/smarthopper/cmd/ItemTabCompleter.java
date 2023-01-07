package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.model.Item;
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

import static mc.elderbr.smarthopper.interfaces.VGlobal.TRADUCAO_ITEM;

public class ItemTabCompleter implements TabCompleter {

    private Item item = null;

    private Player player;
    private String cmd;
    private ItemStack itemStack;
    private List<String> itemList;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;
            if (command.getName().equalsIgnoreCase("item")) {
                cmd = Utils.NAME_ARRAY(args);// PEGA O NOME DO ITEM DIGITADO
                if (cmd.length() > 0) {
                    itemList = new ArrayList<>();
                    for(Item item : TRADUCAO_ITEM.values()){
                        if(item.getName().toLowerCase().contains(cmd.toLowerCase())
                                || item.toTranslation(player).toLowerCase().contains(cmd.toLowerCase())){
                            itemList.add(item.getName());
                            itemList.add(item.toTranslation(player));
                        }
                    }
                    return itemList;
                }
            }
        }
        return Arrays.asList();
    }
}
