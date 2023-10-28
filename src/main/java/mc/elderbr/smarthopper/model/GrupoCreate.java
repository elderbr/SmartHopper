package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;

import java.util.*;

public class GrupoCreate implements VGlobal {

    private static List<String> listNameGrup = new ArrayList<>();

    private GrupoCreate() {
    }

    public static void NewNome() {

        StringBuilder nameGrup = new StringBuilder();
        String name;

        for (String itemName : ITEM_NAME_LIST_DEFAULT) {

            //if (itemName.contains("potion") || itemName.contains("enchantement")) continue;

            if (itemName.contains(" ")) {
                List<String> listName = Arrays.asList(itemName.split("\s"));
                for (String value : listName) {
                    name = value.trim();
                    if (name.length() < 2) continue;
                    if (NotGrupo().contains(name)) continue;
                    if (!listNameGrup.contains(name)) {
                        listNameGrup.add(name);
                    }
                }

                nameGrup = new StringBuilder();
                for (String value : listName) {
                    nameGrup.append(value).append(" ");
                    name = nameGrup.toString().trim();
                    if (name.length() < 2) continue;
                    if (NotGrupo().contains(name)) continue;
                    if (!listNameGrup.contains(name)) {
                        listNameGrup.add(name);
                    }
                }

                listName = new ArrayList<>(Arrays.asList(itemName.split("\s")));
                while (!listName.isEmpty()) {
                    listName.remove(0);
                    if (!listName.isEmpty()) {
                        nameGrup = new StringBuilder();
                        for (String value : listName) {
                            nameGrup.append(value).append(" ");
                            name = nameGrup.toString().trim();
                            if (name.length() < 2) continue;
                            if (NotGrupo().contains(name)) continue;
                            if (!listNameGrup.contains(name)) {
                                listNameGrup.add(name);
                            }
                        }
                    }
                }
            } else {
                if (!listNameGrup.contains(itemName)) {
                    listNameGrup.add(itemName);
                }
            }
        }
        Collections.sort(listNameGrup);
    }

    public static void NEW() {
        int id = 1;
        Iterator<String> iterator = listNameGrup.iterator();
        while (iterator.hasNext()) {
            String grupName = iterator.next();

            Grupo grupo = new Grupo();
            grupo.setName(grupName);

            for (String itemName : ITEM_NAME_LIST_DEFAULT) {
                if (pertence(grupName, itemName)) {
                    if (grupName.equals("dark oak") && itemName.contains("dark oak")) {
                        Msg.ServidorGreen("grupo name: " + grupName + " - item name: " + itemName + " - contains: " + contains(grupName, itemName), GrupoCreate.class);
                    }
                    if (contains(grupName, itemName)) {
                        if (!grupo.getListItem().contains(ITEM_MAP_NAME.get(itemName))) {
                            grupo.addListItem(ITEM_MAP_NAME.get(itemName));
                        }
                    }
                }
            }

            if (grupo.getListItem().size() > 1) {
                grupo.setId(id);
                try {
                    GrupoDao dao = new GrupoDao();
                    dao.save(grupo);
                    Msg.Grupo(grupo, GrupoCreate.class);
                    id++;
                } catch (GrupoException e) {
                    Msg.ServidorErro(e, "NEW()", GrupoCreate.class);
                }
            } else {
                iterator.remove();
            }
        }
    }

    private static boolean contains(String grupoName, String itemName) {
        String[] grups, itens;
        if (itemName.contains(" ")) {
            itens = itemName.split("\s");

            if (grupoName.contains(" ")) {
                grups = grupoName.split("\s");
                int size = grups.length;
                for (String grupValue : grups) {
                    for (String itemValue : itens) {
                        if (grupValue.equals(itemValue)) {
                            size--;
                        }
                    }
                }
                return (size == 0);
            } else {// Se o nome do grupo não conter mais do 2 palavras
                for (String itemValue : itens) {
                    if (itemValue.equals(grupoName)) {
                        return true;
                    }
                }
            }
        }
        return itemName.equals(grupoName);
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
        list.add("of the sea");
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
                list.add("stone sword");
                list.add("stone shovel");
                list.add("stone pickaxe");
                list.add("stone axe");
                list.add("stone hoe");
                list.add("stone brick");
                list.add("stone bricks");
                list.add("end stone");
                list.add("smooth stone");
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
}
