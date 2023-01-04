package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.abstracts.Funil;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class Grupo extends Funil {

    private int id;
    private String name;

    private boolean blocked;

    private List<Item> listItem = new ArrayList<>();

    // Auxiliar
    private static List<String> grupoList;

    public Grupo() {
    }

    @Override
    public Funil setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Grupo setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public Funil setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    public Grupo addListItem(Item item) {
        listItem.add(item);
        return this;
    }

    public Grupo removeListItem(Item item) {
        listItem.remove(item);
        return this;
    }

    public List<Item> getListItem() {
        return listItem;
    }

    public List<String> getListNameItem(){
        List<String> lista = new ArrayList<>();
        for(Item item : getListItem()){
            lista.add(item.getName());
        }
        return lista;
    }

    public static List<String> CreateGrupos() {

        grupoList = new ArrayList<>();
        StringBuilder nameGrupoAux = new StringBuilder();

        // Pegando o nome do grupo
        for (String nameItem : ITEM_NAME_LIST) {
            if (nameItem.split("\\s").length > 0) {
                String[] nameSplit = nameItem.split("\\s");
                nameGrupoAux = new StringBuilder();
                for (String nameGrupo : nameSplit) {
                    nameGrupoAux.append(nameGrupo.concat(" "));
                    if (!grupoList.contains(nameGrupoAux.toString().trim())) {
                        grupoList.add(nameGrupoAux.toString().trim());
                    }
                }
            }
        }

        // Grupos extras
        grupoList.add("redstones");
        grupoList.add("flowers");
        grupoList.add("stone tools");
        grupoList.add("iron tools");
        grupoList.add("golden tools");
        grupoList.add("diamond tools");
        grupoList.add("netherite tools");
        grupoList.add("carne crua");

        // LIVROS ENCANTADOS
        grupoList.add("enchanted book aqua affinity");
        grupoList.add("enchanted book bane of arthropods");
        grupoList.add("enchanted book binding curse");
        grupoList.add("enchanted book blast protection");
        grupoList.add("enchanted book channeling");
        grupoList.add("enchanted book depth strider");
        grupoList.add("enchanted book efficiency");
        grupoList.add("enchanted book feather falling");
        grupoList.add("enchanted book fire aspect");
        grupoList.add("enchanted book fire protection");
        grupoList.add("enchanted book flame");
        grupoList.add("enchanted book fortune");
        grupoList.add("enchanted book frost walker");
        grupoList.add("enchanted book impaling");
        grupoList.add("enchanted book infinity");
        grupoList.add("enchanted book knockback");
        grupoList.add("enchanted book looting");
        grupoList.add("enchanted book loyalty");
        grupoList.add("enchanted book luck of the sea");
        grupoList.add("enchanted book lure");
        grupoList.add("enchanted book mending");
        grupoList.add("enchanted book multishot");
        grupoList.add("enchanted book piercing");
        grupoList.add("enchanted book power");
        grupoList.add("enchanted book projectile protection");
        grupoList.add("enchanted book protection");
        grupoList.add("enchanted book punch");
        grupoList.add("enchanted book quick charge");
        grupoList.add("enchanted book respiration");
        grupoList.add("enchanted book riptide");
        grupoList.add("enchanted book sharpness");
        grupoList.add("enchanted book silk touch");
        grupoList.add("enchanted book smite");
        grupoList.add("enchanted book oul speed");
        grupoList.add("enchanted book sweeping");
        grupoList.add("enchanted book horns");
        grupoList.add("enchanted book unbreaking");
        grupoList.add("enchanted book vanishing curse");


        // POÇÕES
        grupoList.add("potion awkward");
        grupoList.add("potion fire resistance");
        grupoList.add("potion instant damage");
        grupoList.add("potion instant heal");
        grupoList.add("potion jump");
        grupoList.add("potion luck");
        grupoList.add("potion mundane");
        grupoList.add("potion night vision");
        grupoList.add("potion poison");
        grupoList.add("potion regen");
        grupoList.add("potion slow falling");
        grupoList.add("potion slowness");
        grupoList.add("potion speed");
        grupoList.add("potion strength");
        grupoList.add("potion thick");
        grupoList.add("potion turtle master");
        grupoList.add("potion uncraftable");
        grupoList.add("potion water");
        grupoList.add("potion water breathing");
        grupoList.add("potion weakness");
        createGrupoItem();
        return grupoList;
    }

    /***
     * Adiciona item na lista do grupo
     */
    private static void createGrupoItem() {
        List<String> listGrupoName = new ArrayList<>();
        Map<String, Grupo> map = new HashMap<>();
        for (String grup : grupoList) {
            Grupo grupo = new Grupo();
            grupo.setName(grup);
            for (String itemName : ITEM_NAME_LIST) {
                if (IsContent(grup, itemName)) {
                    Item item = ITEM_MAP_NAME.get(itemName);
                    grupo.addListItem(item);
                }
            }
            if (grupo.getListItem().size() > 1) {
                map.put(grup, grupo);
                listGrupoName.add(grup);
            }
        }
        Collections.sort(listGrupoName);
        int id = 1;
        for (String grup : listGrupoName) {
            Grupo grupo = map.get(grup);
            grupo.setId(id);
            for(Item item : grupo.getListItem()){
                item.addListGrupo(grupo);
                Item.SET(item);
            }
            SET(grupo);
            id++;
        }

    }

    private static boolean IsContent(String grup, String item) {
        StringBuilder sb = new StringBuilder();
        String[] nameSplit = item.split("\\s");
        if (nameSplit.length > 0) {
            for (String nameItem : nameSplit) {
                sb.append(nameItem + " ");
                if (grup.equalsIgnoreCase(sb.toString().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<String> NotGrupo() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a stick");
        list.add("of");
        list.add("of the");
        list.add("on");
        list.add("on a");
        list.add("the");
        list.add("on a stick");
        return list;
    }

    /***
     * Verifica se o item pertence ao grupo como por exemplo o grupo stone não pode conter o item stone pickaxe
     * @param grupo da classe Grupo
     * @param item da classe item
     * @return boolean
     */
    private static boolean pertence(String grupo, String item) {
        switch (grupo) {
            case "stone":
                List<String> list = new ArrayList<>();
                list.add("stone shovel");
                list.add("stone pickaxe");
                list.add("stone axe");
                list.add("stone hoe");
                list.add("stone brick");
                list.add("stone bricks");
                list.add("end stone");
                for (String items : list) {
                    if (item.contains(items)) {
                        return false;
                    }
                }
            case "oak":
                if (item.contains("dark oak")) {
                    return false;
                }
            case "sandstone":
                if (item.contains("red sandstone")) {
                    return false;
                }
            default:
                return true;
        }
    }

    public static void SET(@NotNull Grupo grupo) {
        GRUPO_MAP_ID.put(grupo.getId(), grupo);
        GRUPO_MAP_NAME.put(grupo.getName(), grupo);
        if (!GRUPO_NAME_LIST.contains(grupo.getName())) {
            GRUPO_NAME_LIST.add(grupo.name);
        }
        if (!GRUPO_LIST.contains(grupo)) {
            GRUPO_LIST.add(grupo);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}