package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Item extends ItemStack implements Dados {

    private int id = 0;
    private String name;
    private Map<String, String> langMap = new HashMap<>();
    private Location location;
    private String key;
    private String lang;

    public Item() {
    }

    public Item(ItemStack itemStack) {
        super(itemStack);
        name = Utils.ToItemStack(itemStack);
    }

    public Item(Material material) {
        super.setType(material);
        name = Utils.ToMaterial(material);
    }

    public Item(String material) {
        if(material != null
                && !material.equals("grass path")
                && new ItemStack(Utils.ParseMaterial(material)).getType() != Material.AIR) {
            super.setType(Utils.ParseMaterial(material));
            name = Utils.ToItemStack(super.clone());
        }else{
            name = material;
        }
    }

    @Override
    public int getID() {
        return id;
    }

    public int getIDMap() {
        return VGlobal.ITEM_MAP_NAME.get(name).getID();
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getNameMap() {
        return VGlobal.ITEM_MAP_NAME.get(getName()).getName();
    }

    @Override
    public void setName(String name) {
        try {
            super.setType(Utils.ParseMaterial(name));
            this.name = Utils.ToItemStack(super.clone());
        } catch (IllegalArgumentException e) {
            this.name = name;
        }
    }

    @Override
    public String getTraducao(String langKey) {
        if (langMap.get(langKey) == null) {
            return name;
        }
        return langMap.get(langKey).toString();
    }


    @Override
    public void setLang(Map<String, String> lang) {
        this.langMap = lang;
    }

    @Override
    public void setLang(MemorySection memorySection) {
        if (memorySection == null) return;
        for (Map.Entry<String, Object> langs : memorySection.getValues(false).entrySet()) {

            key = langs.getKey();
            lang = Utils.ToUTF(langs.getValue().toString());
            langMap.put(key, lang);// ADICIONANDO AO ITEM ATUAL

            VGlobal.ITEM_MAP.put(lang.toLowerCase(), name);// ADICIONANDO O NOME DO ITEM TRADUZIDOS
            VGlobal.ITEM_NAME_LIST.add(lang.toLowerCase());// ADICIONA NA LISTA DE NOMES DE ITENS
        }
    }

    @Override
    public Map<String, String> getLang() {
        return langMap;
    }

    public ItemStack getItemStack() {
        return this;
    }

    @Override
    public void addTraducao(String lang, String traducao) {
        langMap.put(lang, traducao);
    }

    public boolean isItem() {
        return getItemStack().getType().isItem();
    }

    @Override
    public boolean equals(Object item) {
        if (item instanceof Item) {
            return this.getID() == ((Item) item).getID();
        }
        return false;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Block block) {
        location = block.getLocation();
    }
}
