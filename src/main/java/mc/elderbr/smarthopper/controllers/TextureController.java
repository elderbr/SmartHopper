package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TextureController {
    private final String url = "http://elderbr.com/minecraft/textures/SmartHopper.zip";
    private ConfigDao configDao;

    public TextureController() {
    }

    public String getResourcePack() {
        return url;
    }

    public void carrega(Player player) {
        if (ConfigDao.useTexture()) {
            player.setResourcePack(url);
        }
    }

    public void aceitaTexture(PlayerResourcePackStatusEvent event) {
        if (ConfigDao.useTexture()) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
                event.getPlayer().kickPlayer("§lPara entrar no nosso servidor precisa aceitar nossa textura!");
            }
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                event.getPlayer().kickPlayer("§lOcorreu um ao baixar a textura, tente novamente!");
            }
        }
    }

    public boolean setUseTexture(Player player, boolean useTexture) {
        configDao = new ConfigDao();

        if(player == null){
            configDao.saveUseTexture(useTexture);
            Msg.ServidorRed("Textura foi alterado para ser obrigatorio como "+ useTexture);
            return true;
        }

        if(player.isOp() || configDao.containsAdm(player)){
            configDao.saveUseTexture(useTexture);
            Msg.PlayerGold(player, "Textura foi alterado para ser obrigatorio como "+ useTexture);
            return true;
        }else {
            Msg.PlayerGold(player, "Você não tem permissão!");
        }
        return false;
    }
}
