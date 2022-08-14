package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.VGlobal;
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

public class ItemComando implements CommandExecutor {

    private Item item = null;
    private Player player;
    private String cmd;

    private ItemStack itemStack;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO

            if (command.getName().equalsIgnoreCase("item")) {

                if (cmd.length() == 0) {// VERIFICA SE NÃO FOI DIGITADO E SE O ITEM É DIFERENTE DE AR

                    if (itemStack.getType() == Material.AIR) {
                        player.sendMessage("§cSegure um item na mão ou escreva o nome ou ID!!!");
                        return true;
                    }
                    // CONVERTE ITEM STACK PARA ITEM
                    item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getName());

                } else {
                    try {
                        item = VGlobal.ITEM_MAP_ID.get(Integer.parseInt(cmd));//BUSCA O ITEM PELO O SEU ID
                    } catch (NumberFormatException e) {
                        item = VGlobal.TRADUCAO_ITEM_LIST.get(cmd);//BUSCA O ITEM PELO O NOME
                    }
                }
                if (item != null) {
                    // MOSTRA MENSAGEM PARA O JOGADOR COM TODAS AS INFORMAÇÕES DO ITEM
                    Msg.Item(player, item);// Mostra o nome do item e seu ID
                } else {
                    Msg.ItemNaoExiste(player, cmd);// O item não existe
                }
            }

        }
        return false;
    }
}