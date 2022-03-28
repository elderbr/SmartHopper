package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VGlobal {

    // Grupo
    // TODOS OS IDs DO GRUPO QUE RETORNA O NOME EM INGLÊS
    Map<Integer, Grupo> GRUPO_MAP_ID = new HashMap<>();
    // TODOS OS NOMES DO GRUPO QUE RETORNA O NOME EM INGLÊS
    Map<String, Grupo> GRUPO_MAP_NAME = new HashMap<>();
    // TODOS OS NOMES DO GRUPOS PEGANDO O LANG KEY E TRADUÇÃO
    Map<String, Map<String, String>> GRUPO_LANG_MAP = new HashMap<>();
    // TODOS OS NOMES DO GRUPOS E SUA TRADUÇÃO
    Map<String, String> GRUPO_MAP = new HashMap<>();
    // TODOS OS NOMES DO GRUPO
    List<String> GRUPO_NAME_LIST = new ArrayList<>();
    // LISTA DE GRUPO
    List<Grupo> GRUPO_LIST = new ArrayList<>();
    // ADICIONA LISTA DE ITEM AO NOME DO GRUPO
    Map<String, List<String>> GRUPO_ITEM_MAP_LIST = new HashMap<>();
    // CRIANDO LISTA DE ITEM QUE RETORNA O NOME DO GRUPO
    Map<String, List<String>> GRUPO_LIST_ITEM = new HashMap<>();

    // Items
    Map<Integer, Item> ITEM_MAP_ID = new HashMap<>();
    Map<String, Item> ITEM_MAP_NAME = new HashMap<>();
    // RETORNA A TRADUÇÃO DO ITEM PEGANDO O NOME DO ITEM E O LANG
    Map<String, Map<String, String>> ITEM_LANG_MAP = new HashMap<>();
    Map<String, String> ITEM_MAP = new HashMap<>();
    List<String> ITEM_NAME_LIST = new ArrayList<>();

    // LISTA DO LANGS DISPONIVEL
    List<Lang> LANG_LIST = new ArrayList<>();
    Map<String, Lang> LANG_MAP = new HashMap<>();

    Plugin SMARTHOPPER = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper");
    String VERSION = VGlobal.SMARTHOPPER.getDescription().getVersion();

    File ARQUIVO = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper").getDataFolder().getAbsoluteFile();
    File FILE_LANG = new File(ARQUIVO+File.separator+"langs");

}
