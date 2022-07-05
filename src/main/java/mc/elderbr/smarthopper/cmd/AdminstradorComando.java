package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.AdmDao;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminstradorComando  implements CommandExecutor {


    private Jogador adm;
    private Jogador jogador;
    private Player player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player playerCommand){

            // VERIFICA SE EXISTE O NOME DO JOGADOR DIGITADO
            if(args.length == 0 || args[0].length() < 4){
                Msg.PlayerGold(playerCommand, "Verifique se digitou o nome do jogador correto!!!");
                return false;
            }

            // ADICIONANDO UM NOVO ADM
            if(command.getName().equalsIgnoreCase("addAdm")){

                if(!playerCommand.isOp()){
                    Msg.PlayerGold(playerCommand, "Você não tem permissão para usar esse comando!!!");
                    return false;
                }

                player = Bukkit.getPlayer(args[0]);
                if(player == null){
                    Msg.PlayerGold(playerCommand, "O jogador precisa está online para ser adicionando!!!");
                    return false;
                }
                jogador = new Adm(player);
                AdmDao.INSERT(jogador);
                // Estato do retorno do banco ao tentar adicionar
                switch (AdmDao.STATUS){
                    case 1:
                        Msg.PlayerTodos(jogador.getDsJogador()+" foi adicionado como Administrador do Smart Hopper com sucesso!!!");
                        VGlobal.JOGADOR_LIST.add(jogador);// Adicionando jogador na lista de administrador
                        Config.ADD_ADM();// Adicionando o jogador na lista no arquivo config
                        break;
                    case 2:
                        Msg.PlayerGold(playerCommand, jogador.getDsJogador() + " já foi adicionado!!!");
                        break;
                    default:
                        Msg.PlayerGold(playerCommand, "Ocorreu um erro ao adicionar Adm!!!");
                        break;
                }
            }

            // REMOVER ADMINISTRADOR
            if(command.getName().equalsIgnoreCase("removerAdm")){

                if(!playerCommand.isOp()){
                    Msg.PlayerGold(playerCommand, "Você não tem permissão para usar esse comando!!!");
                    return false;
                }

                // BUSCANDO JOGADOR NA LISTA
                jogador = AdmDao.SELECT(args[0]);

                if(jogador == null){
                    Msg.PlayerGold(playerCommand, "Jogador não está na lista de Administrador!!!");
                    return false;
                }

                AdmDao.DELETE(jogador);
                // Estato do retorno do banco ao tentar adicionar
                switch (AdmDao.STATUS){
                    case 1:
                        VGlobal.JOGADOR_LIST.remove(jogador);// Removendo jogador na lista de administrador
                        Msg.PlayerGold(playerCommand, jogador.getDsJogador()+" não mais um Administrador!!!");
                        break;
                    default:
                        Msg.PlayerGold(playerCommand, "Ocorreu um erro ao remover Adm!!!");
                        break;
                }
            }

        }
        return false;
    }
}
