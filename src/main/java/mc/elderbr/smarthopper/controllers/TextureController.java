package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.file.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.io.IOException;

public class TextureController {
    private final String url = "http://elderbr.com/minecraft/textures/SmartHopper.zip";
    private final boolean USE_TEXTURE = Config.IsUseTexture();

    public TextureController() {
    }

    public boolean isUseTexture() {
        if (!Config.ExistUseTexture()) {
            setUseTexture(true);
        }
        return Config.IsUseTexture();
    }

    public void setUseTexture(boolean use) {
        try {
            Config.SetUseTexture(use);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o uso da textura no servidor!\n" + e.getMessage());
        }
    }

    public String getResourcePack() {
        return url;
    }

    public void carrega(Player player) {
        if (Config.IsUseTexture()) {
            player.setResourcePack(url);
        }
    }

    public void aceitaTexture(PlayerResourcePackStatusEvent event) {
        if(USE_TEXTURE) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
                event.getPlayer().kickPlayer("§lPara entrar no nosso servidor precisa aceitar nossa textura!");
            }
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                event.getPlayer().kickPlayer("§lOcorreu um ao baixar a textura, tente novamente!");
            }
        }
    }
}
