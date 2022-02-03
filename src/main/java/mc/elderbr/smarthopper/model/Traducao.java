package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Traducao {

    private int cdTraducao;
    private String dsTraducao;
    private Player player;

    public Traducao() {
        player.getLocale();
    }

    public int getCdTraducao() {
        return cdTraducao;
    }

    public void setCdTraducao(int cdTraducao) {
        this.cdTraducao = cdTraducao;
    }

    public String getDsTraducao() {
        return dsTraducao;
    }

    public void setDsTraducao(String dsTraducao) {
        this.dsTraducao = dsTraducao;
    }
}
