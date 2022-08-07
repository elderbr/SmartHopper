package mc.elderbr.smarthopper.interfaces;

import org.bukkit.entity.Player;

public interface Linguagem {

    // LANG
    Object setDsLang(String lang);
    Object setDsLang(Player player);
    String getDsLang();

    // TRADUCAO
    Object setDsTraducao(String traducao);
    String getDsTraducao();

}