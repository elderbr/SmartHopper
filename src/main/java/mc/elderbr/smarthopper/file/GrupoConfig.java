package mc.elderbr.smarthopper.file;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GrupoConfig {

    private static File fileConfig = new File(VGlobal.ARQUIVO, "grupo.yml");
    private YamlConfiguration config;

    private Grupo grupo;
    private String lang;
    private String traducao;

    public GrupoConfig() {
        if (!fileConfig.exists()) {
            try {
                fileConfig.createNewFile();
                create();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        VGlobal.CD_MAX.add(1);// O ÚLTIMO CODIGO DO GRUPO
        reload();
    }

    public void create() {
        config = YamlConfiguration.loadConfiguration(fileConfig);
        for (Grupo grupos : VGlobal.GRUPO_LIST) {
            add(grupos);
            Msg.ServidorGold("Criando o grupo >> " + grupos.getName() + " | codigo >> " + grupos.getCodigo());
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

        String name = grupo.getName().toLowerCase();

        config.set(name.concat(".grupo_id"), grupo.getCodigo());
        config.set(name.concat(".grupo_name"), grupo.getName());
        if (!grupo.getTraducao().isEmpty()) {
            config.set(name.concat(".grupo_lang"), grupo.getTraducao());
        }
        config.set(name.concat(".grupo_item"), grupo.getListItem());
        save();
    }

    public static boolean ADD(Grupo grupo) {
        String name = grupo.getName().toLowerCase();
        if (VGlobal.GRUPO_MAP_NAME.get(name) == null) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);

                config.set(name.concat(".grupo_id"), grupo.getCodigo());
                config.set(name.concat(".grupo_name"), grupo.getName());
                config.set(name.concat(".grupo_lang"), grupo.getTraducao());
                config.set(name.concat(".grupo_item"), grupo.getListItem());
                config.save(fileConfig);

                VGlobal.GRUPO_LIST.add(grupo);
                VGlobal.GRUPO_MAP_ID.put(grupo.getCodigo(), grupo);
                VGlobal.GRUPO_MAP_NAME.put(grupo.getName(), grupo);

                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static boolean UPDATE(Grupo grupo) {
        String name = grupo.getName().toLowerCase();
        if (VGlobal.GRUPO_MAP_NAME.get(name) != null) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);

                config.set(name.concat(".grupo_id"), grupo.getCodigo());
                config.set(name.concat(".grupo_name"), grupo.getName());
                config.set(name.concat(".grupo_lang"), grupo.getTraducao());
                config.set(name.concat(".grupo_item"), grupo.getListItem());
                config.save(fileConfig);

                VGlobal.GRUPO_LIST.add(grupo);
                VGlobal.GRUPO_MAP_ID.put(grupo.getCodigo(), grupo);
                VGlobal.GRUPO_MAP_NAME.put(grupo.getName(), grupo);

                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static boolean ADD_TRADUCAO(Grupo grupo) {
        try {

            YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);
            config.set(grupo.getName().toLowerCase().concat(".grupo_lang"), grupo.getTraducao());
            config.save(fileConfig);

            VGlobal.GRUPO_LIST.add(grupo);
            VGlobal.GRUPO_MAP_ID.put(grupo.getCodigo(), grupo);
            VGlobal.GRUPO_MAP_NAME.put(grupo.getName(), grupo);
            VGlobal.TRADUCAO_GRUPO.put(grupo.getName(), grupo);

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
            VGlobal.GRUPO_LIST.remove(grupo);
            VGlobal.GRUPO_MAP_ID.remove(grupo.getCodigo());
            VGlobal.GRUPO_MAP_NAME.remove(grupo.getName());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public void reload() {
        VGlobal.GRUPO_LIST.clear();
        config = YamlConfiguration.loadConfiguration(fileConfig);
        String name = null;
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {

            name = obj.getKey().toString();

            grupo = new Grupo();
            grupo.setCodigo(config.getInt(name.concat(".grupo_id")));
            grupo.setName(config.getString(name.concat(".grupo_name")));
            VGlobal.TRADUCAO_GRUPO.put(grupo.getName().toLowerCase(), grupo);

            // PEGANDO O MAIOR CÓDIGO DO GRUPO
            if (grupo.getCodigo() > VGlobal.CD_MAX.get(0)) {
                VGlobal.CD_MAX.set(0, grupo.getCodigo());
            }

            // TRADUÇÃO
            if (config.get(name.concat(".grupo_lang")) != null) {
                MemorySection tradMemory = (MemorySection) config.get(name.concat(".grupo_lang"));
                for (Map.Entry<String, Object> langs : tradMemory.getValues(false).entrySet()) {
                    lang = langs.getKey();
                    traducao = langs.getValue().toString();
                    grupo.addTraducao(lang, traducao);
                    // Variavel Global
                    VGlobal.TRADUCAO_GRUPO.put(traducao.toLowerCase(), grupo);
                }
            }

            // ITEM DO GRUPO
            for (Object items : config.getList(name.concat(".grupo_item"))) {
                grupo.addList(items.toString());
            }

            // ADICIONANDO NA VARIAVEL GLOBAL
            VGlobal.GRUPO_LIST.add(grupo);
            VGlobal.GRUPO_MAP_ID.put(grupo.getCodigo(), grupo);
            VGlobal.GRUPO_MAP_NAME.put(grupo.getName().toLowerCase(), grupo);

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