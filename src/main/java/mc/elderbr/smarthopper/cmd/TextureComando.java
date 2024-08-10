package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.TextureController;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TextureComando implements CommandExecutor {

    private Player player;
    private TextureController textureController = new TextureController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player p){
            player = p;
        }
        if(command.getName().equalsIgnoreCase("useTexture")){
            if(args.length < 1){
                return false;
            }
            String cmd = args[0];
            if(cmd.equalsIgnoreCase("true") || cmd.equalsIgnoreCase("false")){
                try {
                    return textureController.setUseTexture(player, isBoolean(cmd));
                }catch (Exception e){
                    Msg.PlayerGold(player, e.getMessage());
                }
            }else{
                Msg.PlayerGold(player, "Valor invalido, escolha entre \"true\" ou \"false\"");
            }
        }
        return false;
    }

    private boolean isBoolean(String text) {
        if("true".equalsIgnoreCase(text) || "false".equalsIgnoreCase(text)){
            if(text.equalsIgnoreCase("false")){
                return false;
            }
            if(text.equalsIgnoreCase("true")){
                return true;
            }
        }
        throw new RuntimeException("Não é um boolean");
    }
}
