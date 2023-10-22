package mc.elderbr.smarthopper.file;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GrupoConfig implements VGlobal {

    private static File fileConfig = new File(ARQUIVO, "grupo.yml");
    private YamlConfiguration config;

    private Grupo grupo;
    private String lang;
    private String traducao;
    private Item item;

    public GrupoConfig() {
        // Cria o arquivo grupo.yml se não existir
        if (!fileConfig.exists()) {
            try {
                // Criando o arquivo grupo.yml
                fileConfig.createNewFile();
                // Criando grupo no arquivo grupo.yml
                create();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reload();
    }

    public void create() {
        config = YamlConfiguration.loadConfiguration(fileConfig);
        Collections.sort(GRUPO_NAME_LIST);
        for (String name : GRUPO_NAME_LIST) {
            grupo = GRUPO_MAP_NAME.get(name);
            Msg.ServidorBlue("Criando o grupo " + grupo.getName() + " - ID: " + grupo.getId());
            add(grupo);
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

        String name = grupo.getName().toLowerCase();

        config.set(name.concat(".grupo_id"), grupo.getId());
        config.set(name.concat(".grupo_name"), grupo.getName());
        config.set(name.concat(".grupo_item"), grupo.getListNameItem());
        if (grupo.getTranslation().size() > 0) {
            config.set(name.concat(".grupo_lang"), grupo.getTranslation());
        }
        save();
    }

    public static boolean ADD(Grupo grupo) {
        String name = grupo.getName().toLowerCase();
        if (GRUPO_MAP_NAME.get(name) == null) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);

                config.set(name.concat(".grupo_id"), grupo.getId());
                config.set(name.concat(".grupo_name"), grupo.getName());
                config.set(name.concat(".grupo_lang"), grupo.getTranslation());
                config.set(name.concat(".grupo_item"), grupo.getListNameItem());
                config.save(fileConfig);

                GRUPO_MAP_ID.put(grupo.getId(), grupo);
                GRUPO_MAP_NAME.put(grupo.getName(), grupo);

                return true;
            } catch (IOException e) {
                Msg.ServidorErro("Erro ao salvar novo grupo!!!", "ADD(Grupo grupo)", GrupoConfig.class, e);
            }
        }
        return false;
    }

    public static boolean UPDATE(Grupo grupo) {
        String name = grupo.getName().toLowerCase();
        try {
            // Acessando o arquivo de configuração do grupo -> Grupo.yml
            YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);

            // Verifica se contém tradução para o grupo
            if(grupo.getTranslation().size()>0) {
                config.set(name.concat(".grupo_lang"), grupo.getTranslation());
            }

            // Atualizando a lista de itens do grupo
            config.set(name.concat(".grupo_item"), null);
            config.set(name.concat(".grupo_item"), grupo.getListNameItem());
            config.save(fileConfig);// Salvando o grupo no arquivo -> Grupo.yml

            //Atualiza a variavel global
            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(grupo.getName(), grupo);

            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean ADD_TRADUCAO(Grupo grupo) {
        try {

            YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);
            config.set(grupo.getName().toLowerCase().concat(".grupo_lang"), grupo.getTranslation());
            config.save(fileConfig);

            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(grupo.getName(), grupo);

            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean DELETE(Grupo grupo) {
        try {
            // REMOVENDO O GRUPO DO ARQUIVO grupo.yml
            YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);
            config.set(grupo.getName().toLowerCase(), null);
            config.save(fileConfig);
            // REMOVENDO O GRUPO DA VARIAVEL GLOBAL
            GRUPO_MAP_ID.remove(grupo.getId());
            GRUPO_MAP_NAME.remove(grupo.getName());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /***
     * Faz a leitura do arquivo grupo.yml
     */
    public void reload() {
    }

    private void add(@NotNull String key, @NotNull String value, List<String> comentario) {
        if (comentario != null) {
            config.setComments(key, comentario);
        }
        config.set(key, value);
        save();
    }
}