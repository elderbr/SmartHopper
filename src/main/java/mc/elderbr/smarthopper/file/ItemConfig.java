package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemConfig {

    private final File ITEM_FILE = new File(VGlobal.ARQUIVO, "item.yml");
    private YamlConfiguration config;

    private int idItem = 1;
    private Item item;

    private List<String> list = new ArrayList<>();

    public ItemConfig() {

        Debug.Write("Verificando se existe o arquivo item.yml");
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

    public void createYML(){
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        for(Item item : VGlobal.ITEM_LIST){
            add(item);// Salvando o arquivo item.yml
        }
    }

    /********************************
     *
     * Lendo o arquivo item.yml
     *
     ********************************/
    public void load(){

       VGlobal.ITEM_LIST.clear();// Limpando a lista de item

        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        for(String names : VGlobal.ITEM_NAME_LIST){

            if(config.get(names)==null) continue;

            item = new Item();
            item.setCdItem(config.getInt(names.concat(".item_id")));
            item.setDsItem(config.getString(names.concat(".item_name")));

            // Tradução dos itens
            if(config.get(names.concat(".item_lang"))!=null) {
                MemorySection memorySection = ((MemorySection) config.get(names.concat(".item_lang")));
                for (Map.Entry<String, Object> langs : memorySection.getValues(false).entrySet()) {
                    item.addTraducao(langs.getKey(), langs.getValue().toString());
                    VGlobal.TRADUCAO_ITEM_LIST.put(langs.getValue().toString().toLowerCase(), item);// Adicionando a tradução para o item
                    VGlobal.TRADUCAO_ITEM_NAME_LIST.add(langs.getValue().toString().toLowerCase());
                }
            }
            // Adicionando item na variavel global
            VGlobal.ITEM_LIST.add(item);
            VGlobal.ITEM_MAP_ID.put(item.getCdItem(), item);
            VGlobal.ITEM_MAP_NAME.put(item.getDsItem(), item);
            VGlobal.TRADUCAO_ITEM_LIST.put(names.toLowerCase(), item);// Adicionando a tradução para o item
            VGlobal.TRADUCAO_ITEM_NAME_LIST.add(names.toLowerCase());
        }
    }

    private void add(Item item){
        config.set(item.getDsItem().concat(".item_id"), item.getCdItem());
        config.set(item.getDsItem().concat(".item_name"), item.getDsItem());
        if(!item.getTraducao().isEmpty()) {
            config.set(item.getDsItem().concat(".item_lang"), item.getTraducao());
        }
        save();
    }

    private void save() {
        try {
            config.save(ITEM_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}