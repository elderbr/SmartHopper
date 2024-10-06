package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.msg.ItemMsg;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.Map;
import java.util.Objects;

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
        if (Objects.nonNull(getTranslations().get(lang))) {
            return getTranslations().get(lang);
        }
        return getName();
    }

    default String toItem(ItemStack itemStack) {
        String name = itemStack.getType().getKey().getKey().toLowerCase().replaceAll("_", " ");
        setName(name);
        return name;
    }

    default ItemStack parseItemStack(String name) {
        return new ItemStack(Material.getMaterial(name.toUpperCase().replaceAll("\\s","_")));
    }

    default ItemStack getItemStack() {

        String name = getName();

        // Se o item for livro encantado
        String book = "enchanted book";
        if (name.contains(book)) {
            ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

            // Obtendo o nome do encantamento
            String nameEnchantment = name.replaceAll(book, "").trim();
            nameEnchantment = nameEnchantment.substring(0, nameEnchantment.length() - 1).trim().replaceAll(" ", "_");

            // Obtendo o nível do encantamento
            int nivel;
            try {
                nivel = Integer.parseInt(name.substring(name.length() - 1, name.length()));
            } catch (NumberFormatException e) {
                nivel = 0;
            }

            ItemMeta meta = itemStack.getItemMeta();
            for (Enchantment enchantment : Enchantment.values()) {
                String enchantementKey = enchantment.getKey().getKey();
                if (nameEnchantment.equals(enchantementKey)) {
                    meta.addEnchant(enchantment, nivel, true);
                    break;
                }
            }
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        // Se o item for uma poção
        String potion = "potion";
        String splash = "splash " + potion;
        String lingering = "lingering " + potion;
        ItemStack itemStack;

        // Se a poção for do tipo splash
        if (name.contains(splash)) {
            String namePotion = name.replaceAll(splash, "").trim().replaceAll(" ", "_");
            itemStack = new ItemStack(Material.SPLASH_POTION);
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            for (PotionType type : PotionType.values()) {
                String nameType = type.getKey().getKey();
                if (namePotion.equals(nameType)) {
                    meta.setBasePotionType(type);
                }
            }
            itemStack.setItemMeta(meta);
            return itemStack;
        } else if (name.contains(lingering)) {// Se a poção for do tipo lingering
            String namePotion = name.replaceAll(lingering, "").trim().replaceAll(" ", "_");
            itemStack = new ItemStack(Material.LINGERING_POTION);
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            for (PotionType type : PotionType.values()) {
                String nameType = type.getKey().getKey();
                if (namePotion.equals(nameType)) {
                    meta.setBasePotionType(type);
                }
            }
            itemStack.setItemMeta(meta);
            return itemStack;
        } else if (name.contains(potion)) {// Se a poção for do tipo comum
            String namePotion = name.replaceAll(potion, "").trim().replaceAll(" ", "_");
            itemStack = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            for (PotionType type : PotionType.values()) {
                String nameType = type.getKey().getKey();
                if (namePotion.equals(nameType)) {
                    meta.setBasePotionType(type);
                }
            }
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        try {
            itemStack = new ItemStack(Material.getMaterial(getName().toUpperCase().replaceAll("\s", "_")));
        } catch (Exception e) {
            throw new ItemException(ItemMsg.ITEM_INVALID);
        }
        return itemStack;
    }

    default void infor() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+--------------------------+\n");
        sb.append("ID: ").append(getId());
        sb.append("- Name: ").append(getName());
        sb.append("- translations: ").append(getTranslations()).append("\n");
        sb.append("+--------------------------+\n");
        Msg.ServidorBlue(sb.toString());
    }

}
