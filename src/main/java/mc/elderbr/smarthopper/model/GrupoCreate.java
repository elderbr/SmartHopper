package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;

import java.util.*;

public class GrupoCreate implements VGlobal {

    private static Set<String> listNameGrup = new HashSet<>();

    private GrupoCreate() {
    }

    public static void NewNome() {

        StringBuilder nameGrup = new StringBuilder();
        listNameGrup = new HashSet<>();

        for (String itemName : ITEM_MAP_NAME.keySet()) {
            String name = itemName.toLowerCase().replaceAll("bricks", "brick");
            if (name.contains(" ")) {
                String[] names = name.split("\s");
                for (String value : names) {

                    if (value.replaceAll("[^0-9]", "").length() < 2) continue;

                    if (NotGrupo().contains(value)) continue;// Se conter o nome pula
                    listNameGrup.add(value.trim());
                }
                for (String value : names) {
                    nameGrup.append(value).append(" ");
                    String nameReplace = name.replaceAll(nameGrup.toString(), "").trim();

                    if (nameReplace.replaceAll("[^0-9]", "").length() < 2) continue;

                    if (NotGrupo().contains(nameReplace)) continue;// Se conter o nome pula
                    listNameGrup.add(nameReplace);
                }
            } else {
                listNameGrup.add(name);
            }
        }
    }

    public static List<String> NEW() {
        NewNome();
        Iterator<String> iterator = listNameGrup.iterator();
        while (iterator.hasNext()) {
            String nameGrupo = iterator.next();
            int count = 0;
            for (String nameItem : ITEM_MAP_NAME.keySet()) {
                if (containsItem(nameGrupo, nameItem)) {
                    count++;
                }
            }
            if (count < 2) {
                iterator.remove();
            }
        }
        List<String> list = new ArrayList<>(listNameGrup);
        Collections.sort(list);
        return list;
    }

    public static boolean containsItem(String grupoName, String itemName) {
        int count = 0;
        if (itemName.contains(" ")) {
            for (String name : itemName.split(" ")) {
                if (grupoName.equals(name)) {
                    return true;
                }
            }
            return false;
        } else {
            return grupoName.equals(itemName);
        }
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
