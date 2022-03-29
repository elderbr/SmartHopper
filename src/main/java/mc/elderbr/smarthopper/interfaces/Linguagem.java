package mc.elderbr.smarthopper.interfaces;

import org.bukkit.entity.Player;

public interface Linguagem {

    // LANG
    Object setCdLang(int codigo);
    int getCdLang();
    Object setDsLang(String lang);
    Object setDsLang(Player player);
    String getDsLang();

    // TRADUCAO
    Object setCdTraducao(int codigo);
    int getCdTraducao();
    Object setDsTraducao(String traducao);
    String getDsTraducao();

}
