package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Map;

public class GrupoDao implements VGlobal {

    private YamlConfiguration config = YamlConfiguration.loadConfiguration(GRUPO_FILE);

    public GrupoDao() {
    }

    public boolean save(Grupo grupo) throws GrupoException {
        String name = grupo.getName().toLowerCase();
        try {
            config.set(name.concat(".id"), grupo.getId());
            config.set(name.concat(".name"), grupo.getName());
            if (!grupo.getTranslation().isEmpty()) {
                config.set(name.concat(".lang"), grupo.getTranslation());
            }
            config.set(name.concat(".item"), grupo.getListNameItem());
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

    public Grupo findByName(String name) {
        ConfigurationSection section = config.getConfigurationSection(name);

        Msg.ServidorBlue("Grup: " + name);

        if (section == null) return null;

        Grupo grupo = new Grupo();
        grupo.setId(section.getInt("id"));
        grupo.setName(section.getString("name"));

        for (Object itemName : section.getList("item")) {
            Item item = ITEM_MAP_NAME.get(itemName.toString());
            if (item == null) continue;
            grupo.addListItem(item);
        }
        ConfigurationSection mapLang = section.getConfigurationSection("lang");
        if (mapLang != null) {
            for (Map.Entry<String, Object> lang : mapLang.getValues(false).entrySet()) {
                grupo.addTranslation(lang.getKey(), lang.getValue().toString());
            }
        }
        return grupo;
    }

    public void findAll() {
        config = YamlConfiguration.loadConfiguration(GRUPO_FILE);
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {
            Grupo grupo = findByName(obj.getKey());
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
            if (!grupo.getTranslation().isEmpty()) {
                config.set(name.concat(".lang"), grupo.getTranslation());
            }
            config.set(name.concat(".item"), grupo.getListNameItem());
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
