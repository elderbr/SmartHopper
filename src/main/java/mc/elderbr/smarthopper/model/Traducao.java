package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;

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

    private String item = null;
    private Map<String, String> langMap = new HashMap<>();

    public Traducao() {
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void addItemTraducao(String langKey, String traducao) {
        if (VGlobal.ITEM_LANG_MAP.get(item) == null) {
            langMap.put(langKey, traducao);
            VGlobal.ITEM_LANG_MAP.put(item, langMap);
        } else {
            VGlobal.ITEM_LANG_MAP.get(item).put(langKey, traducao);
        }
    }

    public void addItemTraducao(String langKey, Object traducao) {
        if (VGlobal.ITEM_LANG_MAP.get(item) == null) {
            langMap.put(langKey, String.valueOf(traducao));
            VGlobal.ITEM_LANG_MAP.put(item, langMap);
        } else {
            VGlobal.ITEM_LANG_MAP.get(item).put(langKey, String.valueOf(traducao));
        }
    }


}
