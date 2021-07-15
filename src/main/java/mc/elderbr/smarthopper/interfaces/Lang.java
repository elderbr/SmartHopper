package mc.elderbr.smarthopper.interfaces;

import org.bukkit.configuration.MemorySection;

import java.util.Map;

public interface Lang {

    void setLang(Map<String, String> lang);

    void setLang(MemorySection memorySection);

    Map<String, String> getLang();

    String getTraducao(String lang);

    void addTraducao(String lang, String traducao);
}
