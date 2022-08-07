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
    private String key;
    private String value;

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

        Debug.Write("Carregando o arquivo item.yml");
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
    }

    public void createYML(){
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        int codigo = 1;
        for(String name : VGlobal.ITEM_NAME_LIST){
            Item item = new Item();
            item.setCdItem(codigo);
            item.setDsItem(name);
            add(item);// Salvando o arquivo item.yml
            codigo++;
        }
    }

    public void updateYML() {

        try {
            ITEM_FILE.delete();
            ITEM_FILE.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = YamlConfiguration.loadConfiguration(ITEM_FILE);

        Msg.ServidorGreen("Verificando se precisa atualizar os items...");
        Collections.sort(VGlobal.ITEM_NAME_LIST);
        for(String itemName : VGlobal.ITEM_NAME_LIST){
            item = VGlobal.ITEM_MAP_NAME.get(itemName);
            if(item == null) continue;

            config.set(itemName.concat(".item_id"), item.getCdItem());
            config.set(itemName.concat(".item_name"), item.getDsItem());
            config.set(itemName.concat(".lang"), item.getTraducao());
            save();

        }

        Debug.WriteMsg("Novos itens adicionados com sucesso!!!");
    }

    public void loadYmlAddBanco() {
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
        // SALVA O ITEM DO ARQUIVO ITEM.YML NO BANCO
        if (Config.VERSION() < VGlobal.VERSION_INT) {
            Debug.WriteMsg("Lendo o arquivo item.yml e salvando no bancao");
            for (Map.Entry<String, Object> values : config.getValues(false).entrySet()) {
                Item item = new Item();
                item.setCdItem(config.getInt(values.getKey().concat(".item_id")));
                item.setDsItem(config.getString(values.getKey().concat(".item_name")));

                if(item.getItemStack() == null ) continue;

                Msg.ServidorGreen("ITEM DO ARQUIVO >> " + item.getDsItem(), getClass());

                if ((config.get(values.getKey().concat(".lang"))) instanceof MemorySection) {
                    MemorySection memorySection = ((MemorySection) config.get(values.getKey().concat(".lang")));
                    for (Map.Entry<String, Object> langs : memorySection.getValues(false).entrySet()) {
                        item.addTraducao(langs.getKey(), langs.getValue().toString());
                    }
                }
            }
        }
    }

    private void add(Item item){
        config.set(item.getDsItem().concat(".item_id"), item.getCdItem());
        config.set(item.getDsItem().concat(".item_name"), item.getDsItem());
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