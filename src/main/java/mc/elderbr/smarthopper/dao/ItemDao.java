package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Map;

public class ItemDao implements VGlobal {

    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(ITEM_FILE);
    private String name;

    public ItemDao() {
        if (!ITEM_FILE.exists()) {
            try {
                ITEM_FILE.createNewFile();
            } catch (IOException e) {
                Msg.ServidorErro(e, "Erro ao criar o arquivo item.yml", getClass());
            }
        }
    }

    public boolean save(Item item) throws ItemException {
        try {
            name = item.getName().toLowerCase();
            config.set(name.concat(".id"), item.getId());
            config.set(name.concat(".name"), item.getName());
            if (item.getTranslation().size() > 0) {
                config.set(name.concat(".lang"), item.getTranslation());
            }
            config.save(ITEM_FILE);

            ITEM_MAP_ID.put(item.getId(), item);
            ITEM_MAP_NAME.put(item.getName().toLowerCase(), item);
            ITEM_NAME_LIST.add(item.getName());

        } catch (IOException e) {
            throw new ItemException("Erro ao salvar o item no arquivo item.yml!!!");
        }
        return true;
    }

    public Item findByName(String name) {
        ConfigurationSection section = config.getConfigurationSection(name);
        if (section == null) return null;

        Item item = new Item();
        item.setId(section.getInt("id"));
        item.setName(section.getString("name"));
        ConfigurationSection mapLang = section.getConfigurationSection("lang");
        if (mapLang != null) {
            for (Map.Entry<String, Object> lang : mapLang.getValues(false).entrySet()) {
                item.addTranslation(lang.getKey(), lang.getValue().toString());
            }
        }
        return item;
    }

    public void findAll() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {
            try {
                ConfigurationSection section = config.getConfigurationSection(obj.getKey());

                Item item = new Item();
                item.setId(section.getInt("id"));
                item.setName(section.getString("name"));
                ConfigurationSection mapLang = section.getConfigurationSection("lang");
                if (mapLang != null) {
                    for (Map.Entry<String, Object> lang : mapLang.getValues(false).entrySet()) {
                        item.addTranslation(lang.getKey(), lang.getValue().toString());
                    }
                }

                if (item.getId() < 1 || item.getName() == null) {
                    Msg.ServidorRed("Item desconhecido: " + obj.getKey());
                    Msg.PularLinha(getClass());
                    continue;
                }

                ITEM_MAP_ID.put(item.getId(), item);
                ITEM_MAP_NAME.put(item.getName().toLowerCase(), item);
                if (!ITEM_NAME_LIST.contains(item.getName())) {
                    ITEM_NAME_LIST.add(item.getName());
                }
            } catch (Exception e) {
                Msg.ServidorErro("Erro ao carregar todos os itens.\nO item com o nome: " + obj.getKey(), "", getClass(), e);
            }
        }
    }

    public boolean update(Item item) throws ItemException {
        try {
            name = item.getName().toLowerCase();
            if (item.getTranslation().size() > 0) {
                config.set(name.concat(".lang"), item.getTranslation());
            }
            config.save(ITEM_FILE);

            ITEM_MAP_ID.put(item.getId(), item);
            ITEM_MAP_NAME.put(item.getName().toLowerCase(), item);

        } catch (IOException e) {
            throw new ItemException("Erro ao apagar o item!!!");
        }
        return true;
    }

    public boolean delete(Item item) throws ItemException {
        try {
            config.set(item.getName().toLowerCase(), null);
            config.save(ITEM_FILE);

            ITEM_MAP_ID.remove(item.getId());
            ITEM_MAP_NAME.remove(item.getName().toLowerCase());
            ITEM_NAME_LIST.remove(item.getName());

        } catch (IOException e) {
            throw new ItemException("Erro ao apagar o item!!!");
        }
        return true;
    }


}
