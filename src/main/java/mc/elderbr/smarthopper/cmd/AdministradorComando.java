package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdministradorComando implements CommandExecutor {

    private String player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player playerCommand) {

            player = args[0].trim();

            // VERIFICA SE EXISTE O NOME DO JOGADOR DIGITADO
            if (args.length == 0 || player.length() < 4) {
                Msg.PlayerGold(playerCommand, "Verifique se digitou o nome do jogador correto!!!");
                return false;
            }

            // ADICIONANDO UM NOVO ADM
            if (command.getName().equalsIgnoreCase("addAdm")) {

                if (!playerCommand.isOp()) {
                    Msg.PlayerGold(playerCommand, "Você não tem permissão para usar esse comando!!!");
                    return false;
                }

                if (VGlobal.ADM_LIST.contains(player)) {
                    Msg.PlayerGold(playerCommand, String.format("O jogador %s já é adm do Smart Hopper!!!", player));
                    return false;
                }

                if (Config.ADD_ADM(player)) {
                    Msg.PlayerTodos(String.format("$lO jogador $e%s$r$l é o novo administrador do $2Smart Hopper!!!", player));
                    return false;
                }else{
                    Msg.PlayerRed(playerCommand, "Ocorreu um erro ao adicionar novo adm do Smart Hopper!!!");
                }

            }

            // REMOVER ADMINISTRADOR
            if (command.getName().equalsIgnoreCase("removerAdm")) {

                if (!playerCommand.isOp()) {
                    Msg.PlayerGold(playerCommand, "Você não tem permissão para usar esse comando!!!");
                    return false;
                }
                // BUSCANDO JOGADOR NA LISTA
                if(Config.CONTAINS_ADD(player) && Config.REMOVE_ADM(player)){
                    Msg.PlayerTodos("$lO jogador $e"+ player +"$r$l foi $cremovido$r$l do Adm do Smart Hopper!!!");
                }

            }

        }
        return false;
    }
}
