package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VGlobal {

    // ITEM
    Map<Integer, Item> ITEM_ID_MAP = new HashMap<>();
    Map<String, Item> ITEM_NAME_MAP = new HashMap<>();
    List<Item> LIST_ITEM = new ArrayList<>();

    // GRUPO
    Map<Integer, Grupo> GRUPO_ID_MAP = new HashMap<>();
    Map<String, Grupo> GRUPO_NAME_MAP = new HashMap<>();
    List<Grupo> LIST_GRUPO = new ArrayList<>();
    String GRUPO_INVENTORY = "$8$lGrupo: $r";
    String GRUPO_NOVO_INVENTORY = "$5$lGrupo Novo: $r";

    // LISTA DE TODOS OS MATERIAIS DO JGO
    List<Material> LIST_MATERIAL = new ArrayList<>();

    // LANG
    List<Lang> LIST_LANG = new ArrayList<>();
    Map<String, Lang> LANG_NAME_MAP = new HashMap<>();

    // ADM
    List<Adm> ADM_LIST = new ArrayList<>();


    // CARGO
    Map<Integer, String> CARGO_NOME_MAP = new HashMap<>();

    Plugin SMARTHOPPER = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper");
    String VERSION = VGlobal.SMARTHOPPER.getDescription().getVersion();

    File ARQUIVO = Bukkit.getServer().getPluginManager().getPlugin("SmartHopper").getDataFolder().getAbsoluteFile();

}
