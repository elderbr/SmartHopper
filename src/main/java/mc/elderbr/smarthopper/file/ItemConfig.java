package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Debug.Write("Carregando o arquivo item.yml");
        config = YamlConfiguration.loadConfiguration(ITEM_FILE);
    }

    private void update() {

        Msg.ServidorGreen("Verificando se precisa atualizar os items...");

        for (Map.Entry<String, Object> values : config.getValues(true).entrySet()) {

            key = values.getKey();
            value = values.getValue().toString();

            if (!key.contains("id") && !key.contains("name") && !value.contains("Memory") && !key.contains("lang")) {

                String item = key.substring(0, key.indexOf("."));
                String lang = key.substring(key.indexOf(".") + 1, key.length());

                // REMOVE O QUE NÃO FOR MATERIAL
                if (Material.getMaterial(item.replaceAll("\\s", "_").toUpperCase()) == null) {
                    config.set(item, null);
                    save();
                    continue;
                }

                Debug.Write("Atualizando o item: " + item);

                config.set(key, null);
                config.set(item.concat(".lang.").concat(lang), Utils.ToUTF(value));
                save();

            }
        }

        idItem = config.getValues(false).size() + 1;// PEGA O TAMANHO A LISTA
        Debug.WriteMsg("Adicionando novos itens...");
        itemList();// CARREGANDO TODOS OS ITENS
        List<String> itemList = new ArrayList<>(config.getValues(false).keySet());
        for (String items : list) {
            if (!itemList.contains(items)) {
                Debug.Write("Adicionando item >> " + items);
                config.set(items + ".item_id", idItem);
                config.set(items + ".item_name", items);
                config.set(items + ".lang", VGlobal.ITEM_LANG_MAP.get(items));
                save();
                idItem++;
            }
        }
        Debug.WriteMsg("Novos itens adicionados com sucesso!!!");
    }

    public void createDefault() {

        config = YamlConfiguration.loadConfiguration(ITEM_FILE);

        // SALVA O ITEM DO ARQUIVO ITEM.YML NO BANCO
        if (Config.VERSION() < VGlobal.VERSION_INT) {
            Debug.WriteMsg("Criando os item...");
            for (Map.Entry<String, Object> values : config.getValues(false).entrySet()) {
                Item item = new Item();
                item.setCdItem( config.getInt(values.getKey().concat(".item_id")));
                item.setDsItem( config.getString(values.getKey().concat(".item_name")));

                Msg.ServidorGreen("ITEM DO ARQUIVO >> "+ item.getDsItem(), getClass());

                if ((config.get(values.getKey().concat(".lang"))) instanceof MemorySection) {
                    MemorySection memorySection = ((MemorySection) config.get(values.getKey().concat(".lang")));
                    for(Map.Entry<String, Object> langs : memorySection.getValues(false).entrySet()){
                        item.addTraducao(langs.getKey(),langs.getValue().toString());
                    }
                }
                ItemDao.INSERT(item);
            }
        }
    }

    private void carregar() {

        Debug.WriteMsg("Carregando os items....");

        for (String items : config.getValues(false).keySet()) {

            item = new Item();
            item.setCdItem(config.getInt(items.concat(".item_id")));
            item.setDsItem(config.getString(items.concat(".item_name")));

            if ((config.get(items.concat(".lang"))) instanceof MemorySection) {
                //item.setLang(((MemorySection) config.get(items.concat(".lang"))));
            }

            // ADICIONANDO NO MAPs GLOBAIS
            VGlobal.ITEM_MAP_NAME.put(item.getDsItem(), item);// ADICIONANDO O ITEM PARA SER BUSCADO PELO O NOME
            VGlobal.ITEM_MAP_ID.put(item.getCdItem(), item);// ADICIONANDO O ITEM PARA SER BUSCADO PELO ID
            VGlobal.ITEM_MAP.put(item.getDsItem(), item.getDsItem());// ADICIONANDO O NOME DO ITEM TRADUZIDOS
            VGlobal.ITEM_NAME_LIST.add(item.getDsItem());// ADICIONA NA LISTA DE NOMES DE ITENS

            //Debug.WriteMsg("Item carregando >> " + item.getDsItem());
        }
        Debug.WriteMsg("Items carregados com sucesso!!!");

    }

    private void itemList() {
        list = new ArrayList<>();
        // Percorre todos os materias do jogo
        Debug.WriteMsg("Criando os itens...");
        for (Material materials : Material.values()) {
            if (materials.isItem() && !materials.isAir() && !list.contains(Utils.ToMaterial(materials))) {
                list.add(Utils.ToMaterial(materials));
            }
        }

        // PERCORRENDO OS ENCANTAMENTOS
        Debug.Write("PERCORRENDO OS ENCANTAMENTOS...");
        for (Enchantment enchantments : Enchantment.values()) {
            list.add(Utils.toEnchantment(enchantments));// Lista dos itens
        }
        Debug.Write("PERCORRIDO OS ENCANTAMENTOS!!!");
    }

    private void save() {
        try {
            config.save(ITEM_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
