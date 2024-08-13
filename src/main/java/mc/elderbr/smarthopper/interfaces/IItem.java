package mc.elderbr.smarthopper.interfaces;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface IItem {

    Integer getId();

    IItem setId(Integer code);

    String getName();

    IItem setName(String name);

    boolean isBlocked();
    IItem setBlocked(boolean blocked);

    Map<String, String> getTranslations();

    default IItem addTranslation(String lang, String translation) {
        getTranslations().put(lang, translation);
        return this;
    }

    default IItem addTranslation(Player player, String translation) {
        getTranslations().put(player.getLocale(), translation);
        return this;
    }

    default String toTranslation(Player player) {
        String lang = player.getLocale();
        if (getTranslations().get(lang) != null) {
            return getTranslations().get(lang);
        }
        return getName();
    }

    default String toItemStack(ItemStack itemStack) {
        String name = itemStack.getType().getKey().getKey().toLowerCase().replaceAll("_", " ");
        setName(name);
        return name;
    }

    default ItemStack getItemStack(){
        return new ItemStack(Material.getMaterial(getName().toLowerCase().replaceAll("\s","_")));
    }

}
