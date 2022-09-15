package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.enums.AdmType;
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

    AdmType getType();
    String toType();

}
