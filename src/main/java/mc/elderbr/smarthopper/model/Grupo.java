package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Utils;

import java.util.*;

public class Grupo implements Dados {

    private int codigo;
    private String name;
    private Map<String, String> traducao = new HashMap<>();
    private List<String> listItem = new ArrayList<>();

    // Auxiliar
    private static List<String> grupoList;

    public Grupo() {
    }

    @Override
    public int setCodigo(int codigo) {
        return this.codigo = codigo;
    }

    @Override
    public int getCodigo() {
        return codigo;
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
    public Map<String, String> getTraducao() {
        return traducao;
    }

    public List<String> getListItem() {
        return listItem;
    }

    public Grupo addList(String item) {
        if (!listItem.contains(item)) {
            listItem.add(item);
        }
        return this;
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

        // ALTERA PARA VERDADEIRO A ATUALIZAÇÃO DO GRUPO
        //Config.SetUpdateGrupo(true);
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

            for (String itemName : VGlobal.ITEM_NAME_LIST) {
                if (pertence(nameGrupo, itemName) && grupo.isContains(itemName)) {
                    grupo.addList(itemName);
                }
            }

            // Ferramentas de Pedras
            if (grupo.getName().equals("stone tools")) {
                grupo.addList("stone sword");
                grupo.addList("stone shovel");
                grupo.addList("stone pickaxe");
                grupo.addList("stone axe");
                grupo.addList("stone hoe");
            }

            // Ferramentas de Ferro
            if (grupo.getName().equals("iron tools")) {
                grupo.addList("iron sword");
                grupo.addList("iron shovel");
                grupo.addList("iron pickaxe");
                grupo.addList("iron axe");
                grupo.addList("iron hoe");
            }

            // Ferramentas de Ouro
            if (grupo.getName().equals("golden tools")) {
                grupo.addList("golden sword");
                grupo.addList("golden shovel");
                grupo.addList("golden pickaxe");
                grupo.addList("golden axe");
                grupo.addList("golden hoe");
            }

            // Ferramentas de Diamante
            if (grupo.getName().equals("diamond tools")) {
                grupo.addList("diamond sword");
                grupo.addList("diamond shovel");
                grupo.addList("diamond pickaxe");
                grupo.addList("diamond axe");
                grupo.addList("diamond hoe");
            }

            // Ferramentas de netherite
            if (grupo.getName().equals("netherite tools")) {
                grupo.addList("netherite sword");
                grupo.addList("netherite shovel");
                grupo.addList("netherite pickaxe");
                grupo.addList("netherite axe");
                grupo.addList("netherite hoe");
            }

            // ITENS PARA SEREM ASSADOS
            if (grupo.getName().equals("carne crua")) {
                grupo.addList("potato");
                grupo.addList("beef");
                grupo.addList("porkchop");
                grupo.addList("mutton");
                grupo.addList("chicken");
                grupo.addList("rabbit");
                grupo.addList("cod");
                grupo.addList("salmon");
                grupo.addList("kelp");
            }

            // GRUPO DE FLORES
            if (grupo.getName().equals("flowers")) {
                grupo.addList("grass");
                grupo.addList("fern");
                grupo.addList("dead bush");
                grupo.addList("seagrass");
                grupo.addList("sea pickle");
                grupo.addList("dandelion");
                grupo.addList("poppy");
                grupo.addList("blue orchid");
                grupo.addList("allium");
                grupo.addList("azure bluet");
                grupo.addList("red tulip");
                grupo.addList("orange tulip");
                grupo.addList("white tulip");
                grupo.addList("pink tulip");
                grupo.addList("oxeye daisy");
                grupo.addList("cornflower");
                grupo.addList("lily of the valley");
                grupo.addList("lily pad");
                grupo.addList("wither rose");
                grupo.addList("rose bush");
                grupo.addList("weeping vines");
                grupo.addList("twisting vines");
                grupo.addList("vine");
                grupo.addList("sunflower");
                grupo.addList("lilac");
                grupo.addList("peony");
                grupo.addList("tall grass");
                grupo.addList("large fern");
            }

            // LISTA DE NOMES DE GRUPO GLOBAL
            if (grupo.getListItem().size() > 1 && !VGlobal.GRUPO_NAME_LIST.contains(grupo.getName())) {
                grupo.setCodigo(codigo);
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