package mc.elderbr.smarthopper.abstracts;

import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Funil {

    public abstract Funil setId(int id);
    public abstract int getId();

    public abstract Funil setName(String name);
    public abstract String getName();

    public abstract Funil setBlocked(boolean blocked);
    public abstract boolean isBlocked();

    private Map<String, String> translation = new HashMap<>();


    public Map<String, String> getTranslation(){
        return translation;
    }

    public String toTranslation(String lang){
        return Utils.toUP(translation.get(lang) == null ? getName() : translation.get(lang));
    }

    public String toTranslation(Player player){
        return Utils.toUP(translation.get(player.getLocale()) == null ? getName() : translation.get(player.getLocale()));
    }

    public Object addTranslation(String lang, String translation){
        this.translation.put(lang, translation);
        return this;
    }

    public Object removeTranslation(String lang){
        this.translation.remove(lang);
        return this;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }

}
