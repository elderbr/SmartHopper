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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Grupo implements Dados {

    private int id;
    private String name;
    private List<Item> itemList = new ArrayList<>();
    private Map<String, String> langMap = new HashMap<>();
    public final static int NEW = 0, UPGRADE = 1, DELETE = 2;
    private Location location;

    public Grupo() {
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLang(Map<String, String> lang) {
        this.langMap = lang;
    }

    @Override
    public void setLang(MemorySection memorySection) {
        if (memorySection == null) return;
        for (Map.Entry<String, Object> me : memorySection.getValues(false).entrySet()) {
            langMap.put(me.getKey(), me.getValue().toString());
        }
    }

    @Override
    public Map<String, String> getLang() {
        return langMap;
    }

    @Override
    public String getTraducao(String langKey) {
        langMap = VGlobal.GRUPO_LANG_MAP.get(getName());
        if (langMap == null || langMap.get(langKey) == null) {
            return name;
        }
        return langMap.get(langKey);
    }

    @Override
    public void addTraducao(String lang, String traducao) {
        langMap.put(lang, traducao);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public boolean isContentItem(Item item) {
        for (Item items : itemList) {
            if (items.getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void addItemList(Item item) {
        itemList.add(item);
    }

    public void addItemList(ItemStack itemStack) {
        itemList.add(new Item(itemStack));
    }

    public void removeItemList(ItemStack itemStack) {
        itemList.remove(new Item(itemStack));
    }

    public boolean isItemGrupo(String material) {

        List<String> list = new ArrayList<>();
        switch (getName()) {
            case "stone":
                list.add("cobble");
                list.add("sandstone");
                list.add("redstone");
                list.add("blackstone");
                list.add("glowstone");
                list.add("brick");
                list.add("bricks");
                list.add("grindstone");
                list.add("end stone");
                list.add("lodestone");
                list.add("stonecutter");
                break;
            case "oak":
                list.add("dark oak");
                break;
            case "blue":
                list.add("light blue");
                break;
            case "gray":
                list.add("light gray");
                break;
            case "sand":
                list.add("sandstone");
                break;
            case "sandstone":
                list.add("red sandstone");
                break;
            case "egg":
                list.add("leggings");
                break;
        }
        for (String items : list) {
            if (material.contains(items)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Block block) {
        location = block.getLocation();// ADICIONANDO A LOCATION
        VGlobal.LOCATION_LIST.add(location);// ADICIONANDO LOCATION NA LISTA GLOBAL
    }

    public void createGrupos() {
        List<String> grupoList = new ArrayList<>();
        File file = new File(VGlobal.ARQUIVO, "grupo_names.txt");

        List<String> grupoNot = new ArrayList<>();
        grupoNot.add("on");
        grupoNot.add("a");
        grupoNot.add("o");
        grupoNot.add("of");
        grupoNot.add("the");
        grupoNot.add("and");

        Map<String, String> grupoMap = new HashMap<>();

        for (Material materials : Material.values()) {
            if (materials.isItem() && !materials.isAir()) {
                for (String grupo : Utils.ToMaterial(materials).split("\\s")) {
                    if (grupoNot.contains(grupo)) continue;
                    if (!grupoList.contains(grupo)) {
                        grupoList.add(grupo);
                        grupoMap.put(grupo, VGlobal.ITEM_LANG_MAP.get(Utils.ToMaterial(materials)).get("pt_br"));
                    }
                }
            }
        }

        grupoList = new ArrayList<>(grupoMap.keySet());
        Collections.sort(grupoList);

        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(grupoMap);

        try (BufferedWriter escrever = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (Map.Entry<String, String> grupMap : map.entrySet()) {
                escrever.write(grupMap.getKey().concat(": ").concat(Utils.ToUTF(grupMap.getValue())));
                escrever.newLine();
                escrever.flush();
            }
        } catch (IOException e) {
            Msg.ServidorErro(e, "createGrupos()", getClass());
        }
    }

    public static int newID() {
        int newId = 0;
        for (int id : VGlobal.GRUPO_MAP_ID.keySet()) {
            if (id > newId) {
                newId = id;
            }
        }
        return (newId + 1);
    }

    /***
     * Percorre o nome do item verifica se contém espaço se sim compara os nomes separados
     * @param item nome do item
     * @return falso ou verdadeiro
     */
    public boolean isContentItem(String item) {



        if (this.name.split("\\s").length > 1) {
            if (item.contains(name)) {
                return true;
            }
        } else {
            if(name.equals("axe")){
                Msg.ServidorBlue("machado>>>");
            }
            for (String nameItem : item.split("\\s")) {
                Msg.ServidorBlue("item >> "+ nameItem +" <> grupo "+ name, getClass());
                if (nameItem.equals(this.name)) {
                    return true;
                }
            }
        }
        return false;
    }

}
