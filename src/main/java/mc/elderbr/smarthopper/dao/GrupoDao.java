package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GrupoDao implements VGlobal {

    private static GrupoDao instance;
    private YamlConfiguration config;
    private ItemController itemCtrl = new ItemController();

    private GrupoDao() {
        if (!GRUPO_FILE.exists()) {
            try {
                GRUPO_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
    }

    public static GrupoDao getInstance(){
        synchronized (GrupoDao.class){
            if(Objects.isNull(instance)){
                instance = new GrupoDao();
            }
            return instance;
        }
    }

    public boolean save(Grupo grupo) throws GrupoException {
        String name = grupo.getName().toLowerCase();
        try {
            config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
            config.set(name.concat(".id"), grupo.getId());
            config.set(name.concat(".name"), grupo.getName());
            if (!grupo.getTranslations().isEmpty()) {
                config.set(name.concat(".lang"), grupo.getTranslations());
            }
            List<String> nameItem = grupo.getItemsNames();
            Collections.sort(nameItem);
            config.set(name.concat(".item"), nameItem);
            config.save(GRUPO_FILE);

            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(name, grupo);
            if (!GRUPO_NAME_LIST.contains(grupo.getName())) {
                GRUPO_NAME_LIST.add(grupo.getName());
            }
            Msg.ServidorBlue("Criando o Grupo " + name);
            return true;
        } catch (IOException e) {
            throw new GrupoException(e.getMessage());
        }
    }

    public Grupo findById(Integer id) {
        return GRUPO_MAP_ID.get(id);
    }

    public Grupo findByName(String name) {
        return GRUPO_MAP_NAME.get(name);
    }

    public static Grupo FindByName(String name) {
        return GRUPO_MAP_NAME.get(name);
    }

    public Grupo findConfigByName(String name) {
        Grupo grup = new Grupo();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
        ConfigurationSection grupSection = config.getConfigurationSection(name);

        if (grupSection == null) return null;

        grup.setId(grupSection.getInt("id"));
        grup.setName(grupSection.getString("name"));

        ConfigurationSection langSection = grupSection.getConfigurationSection("lang");
        if (langSection != null) {
            for (Map.Entry<String, Object> lang : langSection.getValues(false).entrySet()) {
                grup.addTranslation(lang.getKey(), lang.getValue().toString());
            }
        }

        // Lista de itens
        List<String> items = (List<String>) grupSection.getList("item");
        if (items.isEmpty()) {
            throw new GrupoException("Item para o Grupo não existe");
        }

        for (String item : items) {
            grup.addItems(itemCtrl.findByName(item));
        }
        return grup;
    }

    public void findAll() {
        config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
        // Percorrendo o arquivo grupo.yml
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {
            try {
                ConfigurationSection section = config.getConfigurationSection(obj.getKey());
                if (section == null) continue;

                // Criando grupo
                Grupo grupo = new Grupo();
                if (section.get("id") == null) {
                    Msg.ServidorRed("Não existe ID associado para o grupo " + grupo.getName());
                    continue;
                }
                grupo.setId(section.getInt("id"));

                if (section.get("name") == null) {
                    Msg.ServidorRed("Não existe nome associado para o grupo " + grupo.getId());
                    continue;
                }
                grupo.setName(section.getString("name"));

                if (section.get("item") == null || section.getList("item").isEmpty()) {
                    Msg.ServidorRed("Não existe item para o grupo " + grupo.getName());
                    continue;
                }

                for (Object itemName : section.getList("item")) {
                    Item item = itemCtrl.findByName(itemName.toString());
                    if (item == null) {
                        continue;
                    }
                    grupo.addItems(item);
                    ItemController.addGrup(item, grupo);// Adicionando o grupo na lista de item
                }
                // Verificando se existe tradução
                ConfigurationSection mapLang = section.getConfigurationSection("lang");
                if (mapLang != null) {
                    for (Map.Entry<String, Object> lang : mapLang.getValues(false).entrySet()) {
                        grupo.addTranslation(lang.getKey(), lang.getValue().toString());
                    }
                }

                GRUPO_MAP_ID.put(grupo.getId(), grupo);
                GRUPO_MAP_NAME.put(grupo.getName().toLowerCase(), grupo);
            } catch (Exception e) {
                throw new GrupoException("Erro ao buscar grupo no arquivo grupo.yml: " + e.getMessage());
            }
        }
    }

    public boolean update(Grupo grupo) throws GrupoException {
        String name = grupo.getName().toLowerCase();
        try {
            config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
            if (!grupo.getTranslations().isEmpty()) {
                config.set(name.concat(".lang"), grupo.getTranslations());
            }
            Collections.sort(grupo.getItemsNames());
            config.set(name.concat(".item"), grupo.getItemsNames());
            config.save(GRUPO_FILE);

            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(grupo.getName().toLowerCase(), grupo);
            GRUPO_NAME_LIST.add(grupo.getName());
            return true;
        } catch (IOException e) {
            throw new GrupoException(e.getMessage());
        }
    }

    public boolean delete(Grupo grupo) throws GrupoException {
        try {
            config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
            config.set(grupo.getName().toLowerCase(), null);
            config.save(GRUPO_FILE);

            GRUPO_MAP_ID.remove(grupo.getId());
            GRUPO_MAP_NAME.remove(grupo.getName().toLowerCase());
            GRUPO_NAME_LIST.remove(grupo.getName());
            return true;

        } catch (IOException e) {
            throw new GrupoException(e.getMessage());
        }
    }


}
