package mc.elderbr.smarthopper.file;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GrupoConfig {

    private File fileConfig = new File(VGlobal.ARQUIVO, "grupo.yml");
    private YamlConfiguration config;

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

        Debug.Write("Verificando se existe o arquivo grupo.yml");
        if (!fileConfig.exists()) {
            try {
                Debug.Write("Criando o arquivo grupo.yml");
                fileConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Debug.Write("Carregando o arquivo grupo.yml");
        config = YamlConfiguration.loadConfiguration(fileConfig);
    }

    public void createYml() {
        Debug.WriteMsg("Criando grupos...");
        config = YamlConfiguration.loadConfiguration(fileConfig);
        for (Map.Entry<String, Grupo> grupos : VGlobal.GRUPO_MAP_NAME.entrySet()) {
            add(grupos.getValue());
        }
        Debug.WriteMsg("Grupos criados com sucesso!");
    }

    public void loadYML() {
        if (config == null) {
            config = YamlConfiguration.loadConfiguration(fileConfig);
        }
        for (Map.Entry<String, Object> grups : config.getValues(false).entrySet()) {
            grupo = new Grupo();
            grupo.setCdGrupo(config.getInt(grups.getKey().concat(".grupo_id")));
            grupo.setDsGrupo(config.getString(grups.getKey().concat(".grupo_name")));
        }
    }

    public void update() {

        if (config == null) {
            config = YamlConfiguration.loadConfiguration(fileConfig);
        }

        Msg.ServidorGold("Atualizando grupos...");

        for (Map.Entry<String, Grupo> grups : VGlobal.GRUPO_MAP_NAME.entrySet()) {
            config.set(grups.getValue().getDsGrupo(), null);
            add(grups.getValue());
        }
        Msg.ServidorGold("Grupos atualizados com sucesso!!!");
    }

    private void save() {
        try {
            config.save(fileConfig);
        } catch (IOException e) {
            Msg.ServidorErro(e, "save()", getClass());
        }
    }

    private void add(@NotNull Grupo grupo) {
        String key = grupo.getDsGrupo();
        List<String> listItem = new ArrayList<>();

        config.set(key.concat(".grupo_id"), grupo.getCdGrupo());
        config.set(key.concat(".grupo_name"), grupo.getDsGrupo());
        if(!grupo.getTraducaoMap().isEmpty()) {
            config.set(key.concat(".lang"), grupo.getTraducaoMap());
        }
        for (Item item : grupo.getListItem()) {
            listItem.add(item.getDsItem());
        }
        Collections.sort(listItem);
        config.set(key.concat(".grupo_item"), listItem);
        Msg.ServidorGold("Criando o grupo " + grupo.getDsGrupo() + " no arquivo grupo.yml");
        save();
    }

    private void add(@NotNull String key, @NotNull String value, List<String> comentario) {
        if (comentario != null) {
            config.setComments(key, comentario);
        }
        config.set(key, value);
        save();
    }
}
