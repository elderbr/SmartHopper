package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemFile {
    private final File FILE = new File(VGlobal.ARQUIVO, "item.yml");
    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(FILE);

    private Item item;
    private ItemDao itemDao = new ItemDao();

    public ItemFile() {
        if(VGlobal.VERSAO>Config.Version()){
            ler();
            itemDao.create();
        }
        escrever();
    }

    public void ler() {
        for (String itens : config.getValues(false).keySet()) {
            item = new Item();
            item.setCdItem(config.getInt(itens.concat(".item_id")));
            item.setDsItem(config.getString(itens.concat(".item_name")));
            Msg.ServidorGreen("Criando item " + item.getDsItem());
            itemDao.insertCodigo(item);
        }
    }

    public void escrever(){
        List<String> list = new ArrayList<>();
        list.add("Lista de Itens e seus códigos");

        config.setComments("absorption", list);
        for(Item itens : itemDao.selectList()){
            String name = itens.getDsItem();
            config.set(name.concat(".item_id"), itens.getCdItem());
            config.set(name.concat(".item_name"), name);
            save();

            VGlobal.ITEM_ID_MAP.put(itens.getCdItem(), itens);
            VGlobal.ITEM_NAME_MAP.put(itens.getDsItem(), itens);
        }
    }
    
    private void save(){
        try{
            config.save(FILE);
        }catch (IOException e){
            Msg.ServidorErro("Erro ao salvar o arquivo item.yml!!!", "save", getClass(), e);
        }
    }
}
