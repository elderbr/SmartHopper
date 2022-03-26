package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LivroComando implements CommandExecutor {

    private Player player;
    private ItemStack itemStack;
    private BookMeta bookMeta;

    private StringBuilder txt;
    private List<String> grupoList;
    private List<String> list;
    private int linha = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (sender instanceof Player) {

            player = (Player) sender;

            if (command.getDsGrupo().equalsIgnoreCase("livro")) {
                itemStack = new ItemStack(Material.WRITTEN_BOOK);
                bookMeta = (BookMeta) itemStack.getItemMeta();
                bookMeta.setTitle("Grupos");

                grupoList = new ArrayList<>();
                list = new ArrayList<>();
                String lang = null;
                for(Grupo grupos : VGlobal.GRUPO_MAP_NAME.values()){
                    lang = Utils.ToUTF(grupos.getTraducao(player.getLocale()));
                    if(lang!=null && !list.contains(lang)) {
                        list.add(lang);
                    }
                }

                Collections.sort(list);
                for(String grupos : list){
                    if(linha == 0){
                        txt = new StringBuilder();
                    }
                    txt.append("."+grupos+"\n");
                    linha++;
                    if(linha==15){
                        grupoList.add(txt.toString());
                        linha = 0;
                    }
                }
                bookMeta.setPages(grupoList);
                bookMeta.setAuthor("§eSmartHopper");
                itemStack.setItemMeta(bookMeta);
                player.getInventory().addItem(itemStack);
                player.sendMessage("Você recebeu o livro com a lista dos grupos");
                player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_YES, 20, 120);
                return true;
            }
        }else{
            Msg.ServidorRed("Comando somente para jogadores!!!");
        }
        return false;
    }
}
