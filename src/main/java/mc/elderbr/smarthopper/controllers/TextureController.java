package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TextureController {

    private final String url;
    private ConfigController configCtrl;

    public TextureController() {
        configCtrl = new ConfigController();
        url = configCtrl.getTexture();
    }

    public String getResourcePack() {
        return url;
    }

    public void carrega(Player player) {
        if (configCtrl.getUseTexture()) {
            player.setResourcePack(url);
        }
    }

    public void aceitaTexture(PlayerResourcePackStatusEvent event) {
        if (configCtrl.getUseTexture()) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
                event.getPlayer().kickPlayer("§lPara entrar no nosso servidor precisa aceitar nossa textura!");
            }
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                event.getPlayer().kickPlayer("§lOcorreu um ao baixar a textura, tente novamente!");
            }
        }
    }

    public boolean setUseTexture(Player player, boolean useTexture) {

        if(player == null){
            return false;
        }

        if(player.isOp() || configCtrl.isAdm(player)){
            configCtrl.saveUseTexture(useTexture);
            Msg.PlayerTodos("Textura foi alterado para ser obrigatório como "+ useTexture);
            return true;
        }else {
            Msg.PlayerGold(player, "Você não tem permissão!");
        }
        return false;
    }
}
