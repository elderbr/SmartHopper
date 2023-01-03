package mc.elderbr.smarthopper.model;

import org.bukkit.entity.Player;

public class Traducao {

    private String lang;
    private String name;
    private String translation;

    public Traducao() {
    }

    public Traducao(String lang, String translation) {
        this.lang = lang;
        this.translation = translation;
    }

    public String getLang() {
        return lang;
    }

    public Traducao setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslation() {
        return translation;
    }

    public Traducao setTranslation(String translation) {
        this.translation = translation;
        return this;
    }
    @Override
    public String toString(){
        return (translation == null ? name : translation);
    }
}
