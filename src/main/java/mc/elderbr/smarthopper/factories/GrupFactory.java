package mc.elderbr.smarthopper.factories;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GrupFactory {

    public GrupFactory() {
    }

    public static Grupo stoneTool() {
        Grupo grup = new Grupo();
        grup.setName("Stone Tool");
        grup.addTranslation("pt-br", "Ferramentas de pedra");
        grup.addItems(Material.STONE_AXE);
        grup.addItems(Material.STONE_SHOVEL);
        grup.addItems(Material.STONE_PICKAXE);
        grup.addItems(Material.STONE_SWORD);
        return grup;
    }

    public static Grupo ironTool() {
        Grupo grup = new Grupo();
        grup.setName("Iron Tool");
        grup.addTranslation("pt-br", "Ferramentas de ferro");
        grup.addItems(Material.IRON_AXE);
        grup.addItems(Material.IRON_SHOVEL);
        grup.addItems(Material.IRON_PICKAXE);
        grup.addItems(Material.IRON_SWORD);
        return grup;
    }

    public static Grupo ironArmor() {
        Grupo grup = new Grupo();
        grup.setName("Iron Armor");
        grup.addTranslation("pt-br", "Armadura de ferro");
        grup.addItems(Material.IRON_HELMET);
        grup.addItems(Material.IRON_CHESTPLATE);
        grup.addItems(Material.IRON_LEGGINGS);
        grup.addItems(Material.IRON_BOOTS);
        grup.addItems(Material.IRON_HORSE_ARMOR);
        return grup;
    }

    public static Grupo goldenTool() {
        Grupo grup = new Grupo();
        grup.setName("Golden Tool");
        grup.addTranslation("pt-br", "Ferramentas de ouro");
        grup.addItems(Material.GOLDEN_AXE);
        grup.addItems(Material.GOLDEN_SHOVEL);
        grup.addItems(Material.GOLDEN_PICKAXE);
        grup.addItems(Material.GOLDEN_SWORD);
        return grup;
    }

    public static Grupo goldenArmor() {
        Grupo grup = new Grupo();
        grup.setName("Golden Armor");
        grup.addTranslation("pt-br", "Armadura de ouro");
        grup.addItems(Material.GOLDEN_HELMET);
        grup.addItems(Material.GOLDEN_CHESTPLATE);
        grup.addItems(Material.GOLDEN_LEGGINGS);
        grup.addItems(Material.GOLDEN_BOOTS);
        grup.addItems(Material.GOLDEN_HORSE_ARMOR);
        return grup;
    }

    public static Grupo diamondTool() {
        Grupo grup = new Grupo();
        grup.setName("Diamond Tool");
        grup.addTranslation("pt-br", "Ferramentas de diamante");
        grup.addItems(Material.DIAMOND_AXE);
        grup.addItems(Material.DIAMOND_SHOVEL);
        grup.addItems(Material.DIAMOND_PICKAXE);
        grup.addItems(Material.DIAMOND_SWORD);
        return grup;
    }

    public static Grupo diamondArmor() {
        Grupo grup = new Grupo();
        grup.setName("Diamond Armor");
        grup.addTranslation("pt-br", "Armadura de diamante");
        grup.addItems(Material.DIAMOND_HELMET);
        grup.addItems(Material.DIAMOND_CHESTPLATE);
        grup.addItems(Material.DIAMOND_LEGGINGS);
        grup.addItems(Material.DIAMOND_BOOTS);
        grup.addItems(Material.DIAMOND_HORSE_ARMOR);
        return grup;
    }

    public static Grupo netheriteTool() {
        Grupo grup = new Grupo();
        grup.setName("Netherite Tool");
        grup.addTranslation("pt-br", "Ferramentas de netherita");
        grup.addItems(Material.NETHERITE_AXE);
        grup.addItems(Material.NETHERITE_SHOVEL);
        grup.addItems(Material.NETHERITE_PICKAXE);
        grup.addItems(Material.NETHERITE_SWORD);
        return grup;
    }

    public static Grupo netheriteArmor() {
        Grupo grup = new Grupo();
        grup.setName("Netherite Armor");
        grup.addTranslation("pt-br", "Armadura de netherita");
        grup.addItems(Material.NETHERITE_HELMET);
        grup.addItems(Material.NETHERITE_CHESTPLATE);
        grup.addItems(Material.NETHERITE_LEGGINGS);
        grup.addItems(Material.NETHERITE_BOOTS);
        return grup;
    }

    public static List<String> nameMethods() {
        List<String> names = new ArrayList<>();
        Method[] methods = GrupFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("names") || method.getName().equals("nameMethods")) continue;
            names.add(method.getName());
        }
        return names;
    }
    public static List<String> names() {
        List<String> names = new ArrayList<>();
        Method[] methods = GrupFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("names") || method.getName().equals("nameMethods")) continue;
            names.add(Utils.adicionarEspacoAntesMaiusculas(method.getName()));
        }
        return names;
    }
}
