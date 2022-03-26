package mc.elderbr.smarthopper.model;

import org.bukkit.entity.Player;

public class Lang {
    private int cdLang;
    private String dsLang;

    public Lang() {
    }

    public int getCdLang() {
        return cdLang;
    }

    public void setCdLang(int cdLang) {
        this.cdLang = cdLang;
    }

    public String getDsLang() {
        return dsLang;
    }

    public void setDsLang(String dsLang) {
        this.dsLang = dsLang;
    }

    public void setDsLang(Player player){
        dsLang = player.getLocale();
    }

    @Override
    public String toString() {
        return dsLang;
    }
}
