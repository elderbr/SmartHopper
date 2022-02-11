package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.dao.LangDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Lang;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemComando implements CommandExecutor {

    private Item item = null;
    private Item itemQuery = null;
    private Item itemConsulta = null;
    private ItemDao itemDao = new ItemDao();
    private StringBuffer nameItem;

    private Player player;
    private String cmd;
    private String langPlayer;

    private ItemStack itemStack;

    private TraducaoDao traducaoDao = new TraducaoDao();
    private Traducao traducao;

    private Lang lang;
    private LangDao langDao = new LangDao();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO
            langPlayer = player.getLocale();
            item = null;

            if (command.getName().equalsIgnoreCase("item")) {
                if (cmd.length() > 0) {
                    itemConsulta = new Item();
                    itemConsulta.setDsItem(cmd);
                    itemConsulta.setDsLang(langPlayer);
                    item = itemDao.select(itemConsulta);
                    if (item == null) {
                        item = traducaoDao.selectItem(cmd);
                        if (item != null)
                            Msg.ServidorGreen("item " + item.getDsTraducao());
                    }
                } else {
                    if (itemStack.getType() != Material.AIR) {
                        itemConsulta = new Item(itemStack);
                        itemConsulta.setDsLang(langPlayer);
                        item = itemDao.select(itemConsulta);
                    }
                }
                if (item != null)
                    Msg.PlayerGreen(player, ChatColor.YELLOW + "Item ID: " + item.getCdItem() + ChatColor.GREEN + " - item: " + item.getDsTraducao());
                else
                    Msg.PlayerRed(player, "Não existe registro do item " + ChatColor.GOLD + cmd + ChatColor.RED + "!!!");
            }

            // ADICIONANDO OU ATUALIZANDO A TRADUÇÃO DO ITEM
            if (command.getName().equalsIgnoreCase("traducaoItem")) {

                if (itemStack.getType() == Material.AIR) {
                    Msg.PlayerGold(player, "Segure um item na mão!!!");
                    return false;
                }

                item = new Item(itemStack);
                item.setDsLang(langPlayer);
                item = itemDao.select(item);
                item.setDsTraducao(Utils.NAME_ARRAY(args));
                // VERIFICA SE JÁ EXISTE TRADUÇÃO PARA O ITEM E LANG
                if (traducaoDao.selectItem(item) == null) {
                    if (traducaoDao.insert(item) > 0) {
                        Msg.PlayerGreen(player, "Tradução para o item " + item.getDsTraducao() + " adicionado com sucesso!");
                    } else {
                        Msg.PlayerRed(player, "Erro ao adicionar tradução do item " + item.getDsTraducao() + "!!!");
                    }
                } else {
                    if (traducaoDao.update(item) > 0) {
                        Msg.PlayerGreen(player, "Tradução para o item " + ChatColor.GOLD + item.getDsTraducao() + ChatColor.GREEN + " atualizada com sucesso!");
                    } else {
                        Msg.PlayerRed(player, "Erro para atualizar a tradução do item " + ChatColor.GOLD + item.getDsTraducao() + ChatColor.GREEN + "!!!");
                    }
                }
                Msg.ServidorGreen("cdItem >> " + item.getCdItem() + " - item getDsItem >> " + item.getDsItem() + " - lang >> " + item.getDsLang() + " - traducao >> " + item.getDsTraducao());
            }
        }
        return false;
    }
}
