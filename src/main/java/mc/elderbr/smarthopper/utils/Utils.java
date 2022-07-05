package mc.elderbr.smarthopper.utils;
import mc.elderbr.smarthopper.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static String ToMaterial(Material material) {
        return material.getKey().getKey().replaceAll("_", " ").toLowerCase();
    }

    public static String toItemStack(ItemStack itemStack) {
        return itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();
    }

    public static Material ParseItemStack(String keys) {
        return Material.getMaterial(keys.toUpperCase().replaceAll("\\s", "_"));
    }

    public static String NAME_ARRAY(String[] args) {
        StringBuilder txt = new StringBuilder();
        for (String name : args) {
            txt.append(name + " ");
        }
        return txt.toString().trim();
    }

    public static String ToUTF(String obj) {
        return StringUtils.capitalize(obj);
    }

    public static Map<String, String> ToUTF(Map<String, String> obj) {
        if (obj == null) return null;
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> values : obj.entrySet()) {
            map.put(values.getKey(), StringUtils.capitalize(values.getValue()));
        }
        return map;
    }

    public static Material ParseMaterial(String name) {
        return Material.valueOf(name.toUpperCase().replaceAll("\\s", "_"));
    }

    public static String toUP(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }


    public static String toEnchantment(Enchantment enchantment) {
        return enchantment.getKey().getKey().toLowerCase().replaceAll("_", " ").trim();
    }

    public static Object getHopperType(String hopper) {
        hopper = hopper.toLowerCase().replace("#", "");
        // VERIFICA É ITEM
        try {
            if (hopper.startsWith("i")) {
                //return VGlobal.ITEM_MAP_ID.get(Integer.parseInt(hopper.replace("i", "")));
            }
        } catch (NumberFormatException e) {
            //return VGlobal.ITEM_MAP_NAME.get(hopper.replace("i", ""));
        }

        // VERIFICA SE É GRUPO
        if (hopper.startsWith("g") || hopper.contains("*")) {
            try {
                //return VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(hopper.replaceAll("[g*]", "")));
            } catch (NumberFormatException e) {
                //return VGlobal.GRUPO_MAP_NAME.get(hopper.replace("*", ""));
            }
        }
        return null;
    }

    public static String toData() {
        Calendar data = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(data.getTime());
    }

    public static String toDataHora() {
        Calendar data = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        return dateFormat.format(data.getTime());
    }

    public static List<Item> isEnchantment(ItemStack itemStack) {
        List<Item> itemList = new ArrayList<>();
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            for (Map.Entry<Enchantment, Integer> keys : meta.getStoredEnchants().entrySet()) {
                //itemList.add(VGlobal.ITEM_MAP_NAME.get(toEnchantment(keys.getKey())));
            }
        }
        return itemList;
    }

    public static String toItem(ItemStack item) {
        return item.getType().name().toLowerCase().replaceAll("_", " ");
    }
}
