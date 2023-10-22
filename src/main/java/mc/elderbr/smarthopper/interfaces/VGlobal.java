package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VGlobal {
    List<String> ADM_LIST = new ArrayList<>();

    /*********************************************************
     *
     *                      GRUPO
     *
     *********************************************************/
    // TODOS OS IDs DO GRUPO QUE RETORNA O NOME EM INGLÊS
    Map<Integer, Grupo> GRUPO_MAP_ID = new HashMap<>();
    // TODOS OS NOMES DO GRUPO QUE RETORNA O NOME EM INGLÊS
    Map<String, Grupo> GRUPO_MAP_NAME = new HashMap<>();
    // TODOS OS NOMES DO GRUPOS PEGANDO O LANG KEY E TRADUÇÃO

    // TODOS OS NOMES DO GRUPO
    List<String> GRUPO_NAME_LIST = new ArrayList<>();

    /*********************************************************
     *
     *                      ITEM
     *
     *********************************************************/
    // Items

    Map<Integer, Integer> ITEM_ID = new HashMap<>();
    List<Item> ITEM_LIST = new ArrayList<>();
    Map<Integer, Item> ITEM_MAP_ID = new HashMap<>();
    Map<String, Item> ITEM_MAP_NAME = new HashMap<>();
    List<String> ITEM_NAME_LIST = new ArrayList<>();
    default void ADD_ITEM_NAME_LIST(String name){
        if(name.isBlank()) return;
        if(!ITEM_NAME_LIST.contains(name)){
            ITEM_NAME_LIST.add(name);
        }
    }

    List<String> ITEM_NAME_LIST_DEFAULT = new ArrayList<>();
    default void ADD_ITEM_NAME_LIST_DEFAULT(String name) {
        if (name.isBlank()) return;
        if (!ITEM_NAME_LIST_DEFAULT.contains(name)) {
            ITEM_NAME_LIST_DEFAULT.add(name);
        }
    }

    List<String> ITEM_NAME_LIST_UPDATE = new ArrayList<>();

    /*********************************************************
     *
     *                      TRADUÇÃO
     *
     *********************************************************/

    // LISTA DO LANGS DISPONIVEL
    List<String> LANG_NAME_LIST = new ArrayList<>();

    // LISTA DE TRADUCAO
    Map<String, Item> TRADUCAO_ITEM = new HashMap<>();

    /*********************************************************
     *
     *                    Livros Encantados
     *
     *********************************************************/
    Map<String, ItemStack> ENCHANTEMENT_BOOK_MAP = new HashMap<>();

    /*********************************************************
     *
     *                    Poções
     *
     *********************************************************/
    Map<String, ItemStack> POTION_MAP = new HashMap<>();


    Plugin SMARTHOPPER = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper");
    String VERSION = VGlobal.SMARTHOPPER.getDescription().getVersion();
    int VERSION_INT = Integer.parseInt(VERSION.replaceAll("[.]", ""));

    File ARQUIVO = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper").getDataFolder().getAbsoluteFile();
    File ITEM_FILE = new File(ARQUIVO, "item.yml");
    File GRUPO_FILE = new File(ARQUIVO, "grupo.yml");
    File FILE_LANG = new File(ARQUIVO + File.separator + "lang");


}