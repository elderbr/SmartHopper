package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.enums.AdmType;
import mc.elderbr.smarthopper.model.Lang;
import org.bukkit.entity.Player;

public interface Jogador {

    Jogador setCdJogador(int codigo);
    int getCdJogador();

    Jogador setDsJogador(Player player);
    Jogador setDsJogador(String player);
    String getDsJogador();

    Jogador setUUID(Player player);
    Jogador setUUID(String player);
    String getUUID();

    Jogador setLang(Player player);
    Lang getLang();

    AdmType getType();
    String toType();

}
