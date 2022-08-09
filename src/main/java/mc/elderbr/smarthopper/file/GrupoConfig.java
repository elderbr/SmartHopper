package mc.elderbr.smarthopper.file;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GrupoConfig {

    private File fileConfig = new File(VGlobal.ARQUIVO, "grupo.yml");
    private YamlConfiguration config;
    private YamlConfiguration configLang;

    private InputStream inputStream;
    private BufferedReader reader;
    private String txtReader;
    private String key;
    private String value;
    private Map<String, String> langMap = new HashMap<>();

    private Grupo grupo;
    private int idGrupo = 1;

    private Item item;
    private List<String> itemStringList;
    private String nameMaterial;

    private ItemStack itemPotion;
    private PotionMeta potionMeta;
    private List<String> listPotion;

    public GrupoConfig() {
        if (!fileConfig.exists()) {
            try {
                fileConfig.createNewFile();
                create();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reload();
    }

    public void create() {
        config = YamlConfiguration.loadConfiguration(fileConfig);
        int cod = 1;
        for (Grupo grupos : VGlobal.GRUPO_LIST) {
            grupos.setCdGrupo(cod);
            add(grupos);
            Msg.ServidorGold("Criando o grupo >> "+ grupos.getDsGrupo()+" | codigo >> "+ cod);
            cod++;
        }
        VGlobal.GRUPO_LIST.clear();
    }

    private void save() {
        try {
            config.save(fileConfig);
        } catch (IOException e) {
            Msg.ServidorErro(e, "save()", getClass());
        }
    }

    private void add(@NotNull Grupo grupo) {

        String name = grupo.getDsGrupo();

        config.set(name.concat(".grupo_id"), grupo.getCdGrupo());
        config.set(name.concat(".grupo_name"), name);
        //Tradução
        if (VGlobal.TRADUCAO_GRUPO_NAME_MAP.get(name) != null) {
            grupo.addTraducao("pt_br", VGlobal.TRADUCAO_GRUPO_NAME_MAP.get(name));
            config.set(name.concat(".grupo_lang"), grupo.getTraducaoMap());
        }
        config.set(name.concat(".grupo_item"), grupo.getListItem());
        save();
    }

    public void reload() {
        VGlobal.GRUPO_LIST.clear();
        config = YamlConfiguration.loadConfiguration(fileConfig);
        String name = null;
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {

            name = obj.getKey().toString();

            grupo = new Grupo();
            grupo.setCdGrupo(config.getInt(name.concat(".grupo_id")));
            grupo.setDsGrupo(config.getString(name.concat(".grupo_name")));

            // TRADUÇÃO
            if(config.get(name.concat(".grupo_lang"))!=null) {
                MemorySection tradMemory = (MemorySection) config.get(name.concat(".grupo_lang"));
                for (Map.Entry<String, Object> trad : tradMemory.getValues(false).entrySet()) {
                    grupo.addTraducao(trad.getKey(), trad.getValue().toString());
                }
            }

            // ITEM DO GRUPO
            for(Object items : config.getList(name.concat(".grupo_item"))){
                grupo.addList(items.toString());
            }

            // ADICIONANDO NA VARIAVEL GLOBAL
            VGlobal.GRUPO_LIST.add(grupo);
            VGlobal.GRUPO_MAP_ID.put(grupo.getCdGrupo(), grupo);
            VGlobal.GRUPO_MAP_NAME.put(grupo.getDsGrupo(), grupo);
        }
    }

    private void add(@NotNull String key, @NotNull String value, List<String> comentario) {
        if (comentario != null) {
            config.setComments(key, comentario);
        }
        config.set(key, value);
        save();
    }
}