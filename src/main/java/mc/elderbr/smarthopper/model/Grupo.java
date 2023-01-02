package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.abstracts.Funil;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<String> CreateGrupos() {

        grupoList = new ArrayList<>();
        String name = null;

        for (String item : VGlobal.ITEM_NAME_LIST) {

            grupoList.add(item);

            String[] split = item.split("\\s");
            int size = item.split("\\s").length;

            for (int i = 0; i < size; i++) {

                grupoList.add(split[i]);

                if ((i + 1) < size) {
                    name = split[i] + " " + split[i + 1];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                }
                if ((i + 2) < size) {
                    name = split[i] + " " + split[i + 1] + " " + split[i + 2];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                }
                if ((i + 3) < size) {
                    name = split[i] + " " + split[i + 1] + " " + split[i + 2] + " " + split[i + 3];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                }
                if ((i + 4) < size) {
                    name = split[i] + " " + split[i + 1] + " " + split[i + 2] + " " + split[i + 3] + " " + split[i + 4];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                }
                if ((i + 5) < size) {
                    name = split[i] + " " + split[i + 1] + " " + split[i + 2] + " " + split[i + 3] + " " + split[i + 4] + " " + split[i + 5];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
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
        Collections.sort(grupoList);
        int codigo = 1;
        for (String nameGrupo : grupoList) {

            // NÃO É NOME VALIDO DE GRUPOS
            if (NotGrupo().contains(nameGrupo)) continue;

            Grupo grupo = new Grupo();
            grupo.setName(nameGrupo);

            Msg.ServidorBlue("Grupo: " + nameGrupo);

            try {
                for (String itemName : VGlobal.ITEM_NAME_LIST) {
                    if (pertence(nameGrupo, itemName) && grupo.isContains(itemName)) {
                        grupo.addListItem(Item.GET(itemName));
                    }
                }

                // Ferramentas de Pedras
                if (grupo.getName().equals("stone tools")) {


                    grupo.addListItem(Item.GET("stone sword"));

                    grupo.addListItem(Item.GET("stone sword"));
                    grupo.addListItem(Item.GET("stone shovel"));
                    grupo.addListItem(Item.GET("stone pickaxe"));
                    grupo.addListItem(Item.GET("stone axe"));
                    grupo.addListItem(Item.GET("stone hoe"));
                }

                // Ferramentas de Ferro
                if (grupo.getName().equals("iron tools")) {
                    grupo.addListItem(Item.GET("iron sword"));
                    grupo.addListItem(Item.GET("iron shovel"));
                    grupo.addListItem(Item.GET("iron pickaxe"));
                    grupo.addListItem(Item.GET("iron axe"));
                    grupo.addListItem(Item.GET("iron hoe"));
                }

                // Ferramentas de Ouro
                if (grupo.getName().equals("golden tools")) {
                    grupo.addListItem(Item.GET("golden sword"));
                    grupo.addListItem(Item.GET("golden shovel"));
                    grupo.addListItem(Item.GET("golden pickaxe"));
                    grupo.addListItem(Item.GET("golden axe"));
                    grupo.addListItem(Item.GET("golden hoe"));
                }

                // Ferramentas de Diamante
                if (grupo.getName().equals("diamond tools")) {
                    grupo.addListItem(Item.GET("diamond sword"));
                    grupo.addListItem(Item.GET("diamond shovel"));
                    grupo.addListItem(Item.GET("diamond pickaxe"));
                    grupo.addListItem(Item.GET("diamond axe"));
                    grupo.addListItem(Item.GET("diamond hoe"));
                }

                // Ferramentas de netherite
                if (grupo.getName().equals("netherite tools")) {
                    grupo.addListItem(Item.GET("netherite sword"));
                    grupo.addListItem(Item.GET("netherite shovel"));
                    grupo.addListItem(Item.GET("netherite pickaxe"));
                    grupo.addListItem(Item.GET("netherite axe"));
                    grupo.addListItem(Item.GET("netherite hoe"));
                }

                // ITENS PARA SEREM ASSADOS
                if (grupo.getName().equals("carne crua")) {
                    grupo.addListItem(Item.GET("potato"));
                    grupo.addListItem(Item.GET("beef"));
                    grupo.addListItem(Item.GET("porkchop"));
                    grupo.addListItem(Item.GET("mutton"));
                    grupo.addListItem(Item.GET("chicken"));
                    grupo.addListItem(Item.GET("rabbit"));
                    grupo.addListItem(Item.GET("cod"));
                    grupo.addListItem(Item.GET("salmon"));
                    grupo.addListItem(Item.GET("kelp"));
                }

                // GRUPO DE FLORES
                if (grupo.getName().equals("flowers")) {
                    grupo.addListItem(Item.GET("grass"));
                    grupo.addListItem(Item.GET("fern"));
                    grupo.addListItem(Item.GET("dead bush"));
                    grupo.addListItem(Item.GET("seagrass"));
                    grupo.addListItem(Item.GET("sea pickle"));
                    grupo.addListItem(Item.GET("dandelion"));
                    grupo.addListItem(Item.GET("poppy"));
                    grupo.addListItem(Item.GET("blue orchid"));
                    grupo.addListItem(Item.GET("allium"));
                    grupo.addListItem(Item.GET("azure bluet"));
                    grupo.addListItem(Item.GET("red tulip"));
                    grupo.addListItem(Item.GET("orange tulip"));
                    grupo.addListItem(Item.GET("white tulip"));
                    grupo.addListItem(Item.GET("pink tulip"));
                    grupo.addListItem(Item.GET("oxeye daisy"));
                    grupo.addListItem(Item.GET("cornflower"));
                    grupo.addListItem(Item.GET("lily of the valley"));
                    grupo.addListItem(Item.GET("lily pad"));
                    grupo.addListItem(Item.GET("wither rose"));
                    grupo.addListItem(Item.GET("rose bush"));
                    grupo.addListItem(Item.GET("weeping vines"));
                    grupo.addListItem(Item.GET("twisting vines"));
                    grupo.addListItem(Item.GET("vine"));
                    grupo.addListItem(Item.GET("sunflower"));
                    grupo.addListItem(Item.GET("lilac"));
                    grupo.addListItem(Item.GET("peony"));
                    grupo.addListItem(Item.GET("tall grass"));
                    grupo.addListItem(Item.GET("large fern"));
                }
            } catch (ItemException e) {
                Msg.ServidorErro("Erro ao pegar o item do grupo!!!", "createGrupoItem", Grupo.class, e);
            }

            // LISTA DE NOMES DE GRUPO GLOBAL
            if (grupo.getListItem().size() > 1 && !VGlobal.GRUPO_NAME_LIST.contains(grupo.getName())) {
                grupo.setId(codigo);
                codigo++;
                VGlobal.GRUPO_NAME_LIST.add(grupo.name);
                VGlobal.GRUPO_LIST.add(grupo);
            }
        }
        Config.SET_VERSION();// Alterando para versão atual
    }

    public boolean isContains(Item item) {
        return this.getListItem().contains(item.getName());
    }

    public boolean isContains(String item) {

        String name = null;
        String[] split = item.split("\\s");
        int size = item.split("\\s").length;

        if (this.getName().equals(item)) return true;

        for (int i = 0; i < size; i++) {

            name = split[i];
            if (this.getName().equals(name)) return true;

            if ((i + 1) < size) {
                name = split[i] + " " + split[i + 1];
                if (this.getName().equals(name)) {
                    return true;
                }
            }
            if ((i + 2) < size) {
                name = split[i] + " " + split[i + 1] + " " + split[i + 2];
                if (this.getName().equals(name)) {
                    return true;
                }
            }
            if ((i + 3) < size) {
                name = split[i] + " " + split[i + 1] + " " + split[i + 2] + " " + split[i + 3];
                if (this.getName().equals(name)) {
                    return true;
                }
            }
            if ((i + 4) < size) {
                name = split[i] + " " + split[i + 1] + " " + split[i + 2] + " " + split[i + 3] + " " + split[i + 4];
                if (this.getName().equals(name)) {
                    return true;
                }
            }
            if ((i + 5) < size) {
                name = split[i] + " " + split[i + 1] + " " + split[i + 2] + " " + split[i + 3] + " " + split[i + 4] + " " + split[i + 4];
                if (this.getName().equals(name)) {
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

    @Override
    public String toString() {
        return name;
    }
}