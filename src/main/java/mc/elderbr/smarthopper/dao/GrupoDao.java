package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GrupoDao implements VGlobal {

    private YamlConfiguration config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
    private ItemController itemCtrl = new ItemController();

    public GrupoDao() {
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
            config.set(name.concat(".item"), grupo.getItems());
            config.save(GRUPO_FILE);

            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(name, grupo);
            if (!GRUPO_NAME_LIST.contains(grupo.getName())) {
                GRUPO_NAME_LIST.add(grupo.getName());
            }
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

    public Grupo findConfigByName(String name) {
        Msg.ServidorBlue("Nome do grupo: " + name, getClass());
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
        if (items.isEmpty()){
            throw new GrupoException("Item para o Grupo não existe");
        }

        for (String item : items) {
            grup.addItems(itemCtrl.findByName(item));
        }
        return grup;
    }

    public void findAll() {
        config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {
            Grupo grupo = findConfigByName(obj.getKey());
            if (grupo == null) continue;

            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(grupo.getName().toLowerCase(), grupo);
            if (!GRUPO_NAME_LIST.contains(grupo.getName())) {
                GRUPO_NAME_LIST.add(grupo.getName());
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
            config.set(name.concat(".item"), grupo.getItems());
            config.save(GRUPO_FILE);

            GRUPO_MAP_ID.put(grupo.getId(), grupo);
            GRUPO_MAP_NAME.put(grupo.getName().toLowerCase(), grupo);
            if (!GRUPO_NAME_LIST.contains(grupo.getName())) {
                GRUPO_NAME_LIST.add(grupo.getName());
            }
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
