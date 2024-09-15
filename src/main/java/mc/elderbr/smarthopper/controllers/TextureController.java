package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TextureController {

    private String url;
    private ConfigController configCtrl;

    public TextureController() {
        configCtrl = new ConfigController();
        url = configCtrl.getTexture();
    }

    public void carrega(Player player) {
        url = configCtrl.getTexture();
        if (configCtrl.getUseTexture()) {
            if(Utils.isValidURL(url)) {
                player.setResourcePack(url);
            }else{
                Msg.PlayerAdms("O link da textura está incorreto!");
                configCtrl.saveUseTexture(false);
            }
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
