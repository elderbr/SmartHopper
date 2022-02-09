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
    private Item itemConsulta = null;
    private ItemDao itemDao;
    private StringBuffer nameItem;

    private Player player;
    private String cmd;
    private String langPlayer;

    private ItemStack itemStack;

    private TraducaoDao traducaoDao;
    private Traducao traducao;

    private Lang lang;
    private LangDao langDao;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO
            langPlayer = player.getLocale();

            nameItem = new StringBuffer();
            if (args.length == 0) {
                nameItem.append(Utils.toItem(itemStack));
            }

            // Criar nome do item
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    nameItem.append(args[i]);
                }
                if (i < args.length) {
                    nameItem.append(" ");
                }
            }


            itemDao = new ItemDao();
            item = null;

            if (command.getName().equalsIgnoreCase("item")) {
                if (cmd.length() > 0) {

                    if (args[0].equalsIgnoreCase("traducao")) {

                        item = itemDao.select(new Item(itemStack));
                        item.setDsTraducao(nameItem.toString().trim());

                        Msg.ServidorGreen("item cd lang >> "+ item.getCdLang());
                        Msg.ServidorGreen("item DsTraducao >> "+ item.getDsTraducao());

                        // BUSCAR O ID DO LANG
                        if(item.getCdLang()<1){
                            langDao = new LangDao();
                            lang = langDao.select(langPlayer);
                            item.setCdLang(lang.getCdLang());
                        }

                        // SALVANDO NO BANCO A TRADUÇÃO
                        traducaoDao = new TraducaoDao();
                        if(item.getCdTraducao()<1) {
                            if (traducaoDao.insert(item) > 0) {
                                Msg.PlayerGold(player, "Tradução do item " + Utils.toItem(itemStack) + " salvo com sucesso!!!");
                            } else {
                                Msg.PlayerRed(player, "Erro ao salvar a tradução do item " + Utils.toItem(itemStack) + "!!!");
                            }
                        }else{
                            Msg.ServidorGreen("traducao id >> "+ item.getCdTraducao());
                            if(traducaoDao.update(item)>0){
                                Msg.PlayerGold(player, "Atualização do item " + item.getDsTraducao() + " bem sucessedida!!!");
                            }else{
                                Msg.PlayerRed(player, "Erro ao atualiza do item " + item.getDsTraducao() + "!!!");
                            }
                        }
                        return false;
                    }

                    itemConsulta = new Item();
                    itemConsulta.setDsItem(cmd);
                    itemConsulta.setDsLang(langPlayer);
                    item = itemDao.select(itemConsulta);
                } else {
                    if (itemStack.getType() != Material.AIR) {
                        itemConsulta = new Item();
                        itemConsulta.setDsItem(nameItem.toString().trim());
                        itemConsulta.setDsLang(langPlayer);
                        item = itemDao.select(itemConsulta);
                    }
                }
                if (item != null)
                    Msg.PlayerGreen(player, ChatColor.YELLOW + "Item ID: " + item.getCdItem() + ChatColor.GREEN + " - item: " + item.getDsTraducao());
            }
        }
        return false;
    }
}
