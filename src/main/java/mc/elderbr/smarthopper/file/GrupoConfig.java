package mc.elderbr.smarthopper.file;


import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
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

    /***
     * Verificar se a versão do plugin atual é maior que a versão anterior, se sim vai ler o arquivo grupo.yml
     * e salvar no banco de dados
     */
    public void loadYmlAddBanco() {
        if (VGlobal.VERSION_INT > Config.VERSION()) {
            Debug.WriteMsg("Criando grupos...");
            if (config == null) {
                config = YamlConfiguration.loadConfiguration(fileConfig);
            }
            for (Map.Entry<String, Object> grups : config.getValues(false).entrySet()) {
                String key = grups.getKey().concat(".");
                grupo = new Grupo();
                grupo.setCdGrupo(config.getInt(key.concat("grupo_id")));
                grupo.setDsGrupo(config.getString(key.concat("grupo_name")));

                if ((config.get(key.concat("lang.lang")) instanceof MemorySection && config.get(key.concat("lang.lang")) != null)) {
                    MemorySection memorySection = (MemorySection) config.get(key.concat("lang.lang"));
                    for (Map.Entry<String, Object> langs : memorySection.getValues(false).entrySet()) {
                        grupo.addTraducao(langs.getKey(), langs.getValue().toString());
                        grupo.setCdLang(VGlobal.LANG_MAP.get(langs.getKey()).getCdLang());
                        grupo.setDsTraducao(langs.getValue().toString());
                        TraducaoDao.INSERT(grupo);
                    }
                }
                // Percorrendo a lista de item do grupo
                for (String itens : (List<String>) config.getList(key.concat(".grupo_item"))) {
                    item = VGlobal.ITEM_MAP_NAME.get(itens);
                    if (item == null) continue;
                    grupo.addList(item);
                }

                GrupoDao.INSERT_ID(grupo);
                Msg.Grupo(grupo, getClass());
                Msg.PularLinha(getClass());
            }
            Debug.WriteMsg("Grupos criados com sucesso!");
        }
    }

    public void loadYML() {
        if (config == null) {
            config = YamlConfiguration.loadConfiguration(fileConfig);
        }
        for (String name : VGlobal.GRUPO_NAME_LIST) {
            grupo = new Grupo();
            grupo.setCdGrupo(config.getInt(name.concat(".grupo_id")));
            grupo.setDsGrupo(config.getString(name.concat(".grupo_name")));
        }
    }

    public void updateYML() {
        if(!Config.IsGrupoUpdate()) {
            try {
                fileConfig.delete();
                fileConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (config == null) {
                config = YamlConfiguration.loadConfiguration(fileConfig);
            }

            Msg.ServidorGold("Atualizando o arquivo grupo.yml");

            Collections.sort(VGlobal.GRUPO_NAME_LIST);
            for (String name : VGlobal.GRUPO_NAME_LIST) {
                grupo = VGlobal.GRUPO_MAP_NAME.get(name);
                if (grupo == null) continue;
                add(grupo);
            }
            Msg.ServidorGold("Finalizado atualização do arquivo grupo.yml!!!");
        }
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
        if (!grupo.getTraducaoMap().isEmpty()) {
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