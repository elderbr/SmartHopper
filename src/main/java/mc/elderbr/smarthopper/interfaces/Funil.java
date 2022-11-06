package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Map;

public interface Funil {

    Funil setCodigo(int codigo);

    int getCodigo();

    Funil setName(String name);

    String getName();

    default String toName() {
        return Utils.ToUTF(getName());
    }

    boolean isBloqueado();

    Funil setBloqueado(boolean bloqueado);

    Map<String, String> getTraducao();

    default Map<String, String> addTraducao(String lang, String traducao) {
        getTraducao().put(lang, traducao);
        return getTraducao();
    }

    default String toTraducao(String lang) {
        if (getTraducao().get(lang) != null) {
            return Utils.ToUTF(getTraducao().get(lang));
        } else {
            return Utils.ToUTF(getName());
        }
    }

    default String toTraducao(Player player) {
        if (getTraducao().get(player.getLocale()) != null) {
            return Utils.ToUTF(getTraducao().get(player.getLocale()));
        } else {
            return Utils.ToUTF(getName());
        }
    }

    default String toTraducao() {
        return Utils.ToUTF(getName());
    }
}
