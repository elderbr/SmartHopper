package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.file.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.io.IOException;

public class TextureController {
    private final String url = "http://elderbr.com/minecraft/textures/SmartHopper.zip";

    public TextureController() {
    }

    public boolean isUseTexture(){
        if(!Config.ExistUseTexture()){
            setUseTexture(true);
        }
        return Config.IsUseTexture();
    }

    public void setUseTexture(boolean use){
        try {
            Config.SetUseTexture(use);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o uso da textura no servidor!\n"+ e.getMessage());
        }
    }

    public void carrega(Player player){
        if(isUseTexture()) {
            player.setResourcePack(url);
        }
    }

    public void aceitaTexture(PlayerResourcePackStatusEvent event){
        if(isUseTexture()) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
                event.getPlayer().kickPlayer("Para entrar no nosso servidor precisa aceitar nossa textura!");
            }
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                carrega(event.getPlayer());
            }
        }
    }
}
