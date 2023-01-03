package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ARQUIVO;
import static mc.elderbr.smarthopper.interfaces.VGlobal.TRADUCAO_ITEM;

public class ItemConfig {

    private static File ITEM_FILE = new File(ARQUIVO, "item.yml");
    private YamlConfiguration config;
    private Item item;

    private List<String> list = new ArrayList<>();

    public ItemConfig() {

        if (!ITEM_FILE.exists()) {
            try {
                Debug.Write("Criando o arquivo item.yml");
                ITEM_FILE.createNewFile();

                createYML();// Cria os itens se não existir

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        load();// Lendo os itens do arquivo item.yml
    }

    public void createYML() {
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        for (Item item : VGlobal.ITEM_LIST) {
            add(item);// Salvando o arquivo item.yml
        }
    }

    /********************************
     *
     * Lendo o arquivo item.yml
     *
     ********************************/
    public void load() {
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        for(String value : config.getValues(false).keySet()){
            item = new Item();
            item.setId(config.getInt(value.concat(".item_id")));
            item.setName(config.getString(value.concat(".item_name")));
            // ADICIONANDO TRADUÇÃO
            TRADUCAO_ITEM.put(item.getName(), item);// ADICIONANDO TRADUÇÃO GLOBAL
            if(config.get(value.concat(".item_lang"))!=null) {
                MemorySection memorySection = (MemorySection) config.get(value.concat(".item_lang"));
                for (Map.Entry<String, Object> langs : memorySection.getValues(false).entrySet()) {
                    item.addTranslation(langs.getKey(), langs.getValue().toString());
                    TRADUCAO_ITEM.put(item.getName(), item);// ADICIONANDO TRADUÇÃO GLOBAL
                    TRADUCAO_ITEM.put(langs.getValue().toString(), item);// ADICIONANDO TRADUÇÃO GLOBAL
                }
            }
            Item.SET(item);
        }
    }

    private void add(Item item) {
        config.set(item.getName().concat(".item_id"), item.getId());
        config.set(item.getName().concat(".item_name"), item.getName());
        if(item.getTranslation().size()>0) {
            config.set(item.getName().concat(".item_lang"), item.getTranslation());
        }
        save();
    }

    public static boolean ADD_TRADUCAO(Item item) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(ITEM_FILE);
            config.save(ITEM_FILE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void save() {
        try {
            config.save(ITEM_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}