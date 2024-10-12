package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.factories.GrupFactory;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GrupoCreate implements VGlobal {

    private static Set<String> listNameGrup = new TreeSet<>();

    private GrupoCreate() {
    }

    public static void NewNome() {

        listNameGrup = new HashSet<>();

        for (String itemName : ITEM_MAP_NAME.keySet()) {
            String name = itemName.toLowerCase().replaceAll("bricks", "brick");
            if (name.contains(" ")) {
                String[] names = name.split(" ");
                for (int i = 0; i < names.length; i++) {
                    String value = String.join(" ", Arrays.copyOfRange(names, i, names.length));
                    if (value.replaceAll("\\d", "").isEmpty()) continue;
                    if (NotGrupo().contains(value)) continue;// Se conter o nome pula
                    listNameGrup.add(value);

                    value = names[i];
                    if (value.replaceAll("\\d", "").isEmpty()) continue;
                    if (NotGrupo().contains(value)) continue;// Se conter o nome pula
                    listNameGrup.add(value);
                }
                for (int i = names.length; i > 0; i--) {
                    String value = String.join(" ", Arrays.copyOfRange(names, 0, i));
                    if (value.replaceAll("\\d", "").isEmpty()) continue;
                    if (NotGrupo().contains(value)) continue;// Se conter o nome pula
                    listNameGrup.add(value);
                }
            } else {
                listNameGrup.add(name);
            }
        }
    }

    public static List<String> NEW() {
        List<Grupo> grupos = new ArrayList<>();
        NewNome();
        Iterator<String> iterator = listNameGrup.iterator();
        while (iterator.hasNext()) {
            String nameGrupo = iterator.next();

            Grupo grupo = new Grupo();
            grupo.setName(nameGrupo);

            int count = 0;
            for (String nameItem : ITEM_MAP_NAME.keySet()) {
                if (containsItem(nameGrupo, nameItem)) {
                    grupo.addItems(ItemController.FindByName(nameItem));
                    count++;
                }
            }
            grupos.add(grupo);
            if (count < 2) {
                iterator.remove();
                grupos.remove(grupo);
            }
        }
        // Grupos personalizados
        for (String name : GrupFactory.names()) {
            listNameGrup.add(name);
        }
        List<String> list = new ArrayList<>(listNameGrup);
        Collections.sort(list);
        return list;
    }

    public static boolean containsItem(String grupoName, String itemName) {
        if (itemName.contains(" ")) {
            String[] names = itemName.split("\\s");
            for (int i = 0; i < names.length;i++) {
                String name = String.join(" ", Arrays.copyOfRange(names, i, names.length));
                if (grupoName.equals(name) && pertence(grupoName, itemName)) {
                    return true;
                }
                if(grupoName.equals(names[i]) && pertence(grupoName, itemName)){
                    return true;
                }
            }
            for (int i = names.length; i > 0;i--) {
                String name = String.join(" ", Arrays.copyOfRange(names, 0, i));
                if (grupoName.equals(name) && pertence(grupoName, itemName)) {
                    return true;
                }
            }
            return false;
        } else {
            return (grupoName.equals(itemName) && pertence(grupoName, itemName));
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
                break;
            case "oak":
                if (item.contains("dark oak")) {
                    return false;
                }
                break;
            case "sandstone":
                if (item.contains("red sandstone")) {
                    return false;
                }
                break;
            default:
                return true;
        }
        return true;
    }
}
