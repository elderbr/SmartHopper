package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VGlobal {

    List<Jogador> JOGADOR_LIST = new ArrayList<>();
    List<String> ADM_LIST = new ArrayList<>();

    // Grupo
    // TODOS OS IDs DO GRUPO QUE RETORNA O NOME EM INGLÊS
    Map<Integer, Grupo> GRUPO_MAP_ID = new HashMap<>();
    // TODOS OS NOMES DO GRUPO QUE RETORNA O NOME EM INGLÊS
    Map<String, Grupo> GRUPO_MAP_NAME = new HashMap<>();
    // TODOS OS NOMES DO GRUPOS PEGANDO O LANG KEY E TRADUÇÃO
    Map<String, String> GRUPO_MAP = new HashMap<>();
    // TODOS OS NOMES DO GRUPO
    List<String> GRUPO_NAME_LIST = new ArrayList<>();
    // LISTA DE GRUPO
    List<Grupo> GRUPO_LIST = new ArrayList<>();
    List<Integer> CD_MAX = new ArrayList<>();

    // Items
    List<Item> ITEM_LIST = new ArrayList<>();
    Map<Integer, Item> ITEM_MAP_ID = new HashMap<>();
    Map<String, Item> ITEM_MAP_NAME = new HashMap<>();
    List<String> ITEM_NAME_LIST = new ArrayList<>();

    // LISTA DO LANGS DISPONIVEL
    List<String> LANG_NAME_LIST = new ArrayList<>();

    // LISTA DE TRADUCAO
    List<String> TRADUCAO_ITEM_NAME_LIST = new ArrayList<>();
    Map<String, Item> TRADUCAO_ITEM_LIST = new HashMap<>();
    Map<String, Grupo> TRADUCAO_GRUPO = new HashMap<>();
    Map<String, String> TRADUCAO_GRUPO_YML = new HashMap<>();

    // LIVROS ENCANTADOS
    List<String> BOOK_ENCHANTMENTE_LIST = new ArrayList<>();
    Map<String, Enchantment> BOOK_ENCHANTEMENT_MAP = new HashMap<>();

    Plugin SMARTHOPPER = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper");
    String VERSION = VGlobal.SMARTHOPPER.getDescription().getVersion();
    int VERSION_INT = Integer.parseInt(VERSION.replaceAll("[.]",""));

    File ARQUIVO = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper").getDataFolder().getAbsoluteFile();
    File FILE_LANG = new File(ARQUIVO+File.separator+"lang");


}