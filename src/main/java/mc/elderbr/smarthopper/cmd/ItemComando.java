package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class ItemComando implements CommandExecutor {

    private Item item = null;
    private String nameItem = null;
    private Player player;
    private String cmd;
    private ItemStack itemStack;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO

            item = null;

            if(!command.getName().equalsIgnoreCase("item")){
                return false;
            }

            if(cmd.length()>0){
                try{
                    item = ITEM_MAP_ID.get(Integer.parseInt(cmd));
                }catch (NumberFormatException e){
                    item = TRADUCAO_ITEM.get(cmd);
                }
            }else {
                if (itemStack.getType() == Material.AIR) {
                    Msg.PlayerGold(player, "Digite o nome ou ID do item ou segure um item na mão!!!");
                    return false;
                }
                item = ITEM_MAP_NAME.get(Item.ToName(itemStack));
            }
            if(item != null){
                Msg.ServidorBlue("item: "+ item.toInfor(), getClass());
                Msg.Item(player, item);
            }else{
                Msg.PlayerGold(player, String.format("O item %s não existe", cmd));
            }

        }
        return false;
    }
}