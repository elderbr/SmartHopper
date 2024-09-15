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

    public static String toUP(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    public static String toData() {
        Calendar data = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(data.getTime());
    }

    public static String adicionarEspacoAntesMaiusculas(String name){
        StringBuilder novoNome = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char letra = name.charAt(i);
            // Verifica se o caractere é maiúsculo e não é o primeiro caractere
            if (Character.isUpperCase(letra) && i != 0) {
                novoNome.append(" ");  // Adiciona espaço antes da letra maiúscula
            }
            novoNome.append(letra);
        }
        return novoNome.toString().toLowerCase();
    }
}
