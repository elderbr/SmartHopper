package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
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

    public Grupo findById(Integer id){
        return GRUPO_MAP_ID.get(id);
    }

    public Grupo findByName(String name) {
        return GRUPO_MAP_NAME.get(name);
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
