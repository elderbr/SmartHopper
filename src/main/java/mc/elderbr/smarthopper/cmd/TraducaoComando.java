package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TraducaoComando implements CommandExecutor {

    private Player player;
    private Item item;
    private Adm jogador;
    private boolean isOP = false;

    private String[] traducaoArgs;
    private StringBuilder traducaoList = new StringBuilder();
    private Traducao traducao;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {

            player = (Player) sender;
            jogador = new Adm(player);

            traducaoArgs = args;

            if (command.getName().equalsIgnoreCase("addTraducao")) {

                if(player.getInventory().getItemInMainHand().getType() == Material.AIR){
                    Msg.PlayerGold(player, "Segure um item na mão para adicionar a tradução!!!");
                    return false;
                }

                item = VGlobal.ITEM_MAP_NAME.get(new Item(player.getInventory().getItemInMainHand()).getDsItem());
                item.setCdLang(jogador.getLang().getCdLang());

                // VERIFICA SE O JOGADOR ESTÁ NA LISTA DE ADMINISTRADORES
                isOP = false;
                for(Jogador jg : VGlobal.JOGADOR_LIST){
                    if (jg.getUUID().equals(jogador.getUUID())) {
                        isOP = true;
                        break;
                    }
                }

                if(!isOP){// VERIFICA SE O JOGADOR É ADM
                    Msg.PlayerGold(player, "Você não tem permissão para usar esse comando!!!");
                    return false;
                }

                if(args.length == 0){// VERIFICA SE EXISTE TEXTO PARA A TRADUÇÃO
                    Msg.PlayerGold(player, "Digite a tradução!!!");
                    return false;
                }

                item.setDsTraducao(toTraducao());

                traducao = VGlobal.TRADUCAO_MAP_ITEM_NAME.get(item.getCdItem());

                if(TraducaoDao.INSERT(item)>0){
                    VGlobal.ITEM_MAP_NAME.get(item.getDsItem()).addTraducao(jogador.getLang().getDsLang(), item.getDsTraducao());
                    Msg.PlayerGreen(player, "Tradução para o item "+ item.getDsItem()+" adicionado com sucesso!!!");
                }else{
                    item.setCdTraducao(traducao.getCdTraducao());
                    if(TraducaoDao.UPDATE(item)) {
                        traducao.setDsTraducao(item.getDsTraducao());
                        VGlobal.TRADUCAO_MAP_ITEM_NAME.put(item.getCdItem(), traducao);
                        VGlobal.ITEM_MAP_NAME.get(item.getDsItem()).addTraducao(item.getDsLang(), item.getDsTraducao());
                        Msg.PlayerGreen(player, "Tradução do "+ item.getDsItem() +" atualizado com sucesso!!!");
                    }
                }

            }
        }

        return false;
    }

    private String toTraducao(){
        traducaoList = new StringBuilder();
        for(String value : traducaoArgs){
            traducaoList.append(value.concat(" "));
        }
        return traducaoList.toString().trim();
    }
}
