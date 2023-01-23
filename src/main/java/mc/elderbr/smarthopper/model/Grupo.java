package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.abstracts.Funil;
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

    public List<String> getListNameItem() {
        List<String> lista = new ArrayList<>();
        for (Item item : getListItem()) {
            lista.add(item.getName());
        }
        return lista;
    }

    public boolean isContains(Item item){
        Grupo grupo = GRUPO_MAP_ID.get(id);
        return grupo.getListItem().contains(item);
    }

    public static List<String> CreateGrupos() {

        grupoList = new ArrayList<>();
        // Pegando o nome do grupo
        for (String nameItem : ITEM_NAME_LIST) {
            if (nameItem.split("\\s").length > 0) {
                //
                String[] nameSplit = nameItem.split("\\s");
                for (int i = 0; i < nameSplit.length; i++) {
                    String name = nameSplit[i];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                    // Passo 1
                    if (i + 1 == nameSplit.length) {
                        continue;
                    }
                    name = nameSplit[i] + " " + nameSplit[i + 1];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                    // Passo 2
                    if (i + 2 == nameSplit.length) {
                        continue;
                    }
                    name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                    // Passo 3
                    if (i + 3 == nameSplit.length) {
                        continue;
                    }
                    name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2] + " " + nameSplit[i + 3];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                    // Passo 4
                    if (i + 4 == nameSplit.length) {
                        continue;
                    }
                    name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2] + " " + nameSplit[i + 3] + " " + nameSplit[i + 4];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                    // Passo 5
                    if (i + 5 == nameSplit.length) {
                        continue;
                    }
                    name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2] + " " + nameSplit[i + 3] + " " + nameSplit[i + 4] + " " + nameSplit[i + 5];
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                }
            } else {
                if(!grupoList.contains(nameItem)) {
                    grupoList.add(nameItem);
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

        createGrupoItem();
        return grupoList;
    }

    /***
     * Adiciona item na lista do grupo
     */
    private static void createGrupoItem() {
        // Limpando a lista dos nomes do grupo
        GRUPO_NAME_LIST.clear();
        // Lista de nomes do grupo auxiliar
        List<String> listGrupoName = new ArrayList<>();
        // Mapa com os nome do grupo auxiliar
        Map<String, Grupo> map = new HashMap<>();
        for (String grup : grupoList) {

            // NÃO É NOME VALIDO DE GRUPOS
            if (NotGrupo().contains(grup)) continue;

            Grupo grupo = new Grupo();
            grupo.setName(grup);
            // Buscando item que o grupo possa ter
            for (String itemName : ITEM_NAME_LIST) {
                // Verifica se grupo pertence ao grupo
                if (pertence(grupo.getName(), itemName) && IsContent(grup, itemName)) {
                    grupo.addListItem(ITEM_MAP_NAME.get(itemName));
                }
            }
            // Verifica se o grupo contém mais do que 1 item se adiciona na lista
            if (grupo.getListItem().size() > 1) {
                // Lista de nomes do grupo
                if(!listGrupoName.contains(grup)) {
                    map.put(grup, grupo);// Adicionando na lista de mapa do grupo
                    listGrupoName.add(grup);
                }
            }
        }
        // Organizando a lista de nomes do grupo em ordem alfabetica
        Collections.sort(listGrupoName);
        // Id auxiliar para adicionar ao grupo
        int id = 1;
        // Percorrendo a lista de nomes do grupo
        for (String grup : listGrupoName) {
            // Buscando o grupo na lista do mapa
            Grupo grupo = map.get(grup);
            // Setando o ID do grupo
            grupo.setId(id);

            // Adicionando grupo no item
            for (String itemName : grupo.getListNameItem()) {
                // Buscando o item na mémoria
                Item item = ITEM_MAP_NAME.get(itemName);
                // Adicionando o grupo no item
                item.addListGrupo(grupo);
                // Salvando o item no atributo global
                Item.SET(item);
            }
            // Salvando o grupo no atributo global
            SET(grupo);
            id++;
        }

    }

    private static boolean IsContent(String grup, String item) {
        String name = null;
        String[] nameSplit = item.split("\\s");
        if (nameSplit.length > 0) {
            for (int i = 0; i < nameSplit.length; i++) {
                if (grup.equalsIgnoreCase(nameSplit[i])) {
                    return true;
                }

                // Passo 1
                if (i + 1 == nameSplit.length) {
                    continue;
                }
                name = nameSplit[i] + " " + nameSplit[i + 1];
                if (grup.equalsIgnoreCase(name)) {
                    return true;
                }

                // Passo 2
                if (i + 2 == nameSplit.length) {
                    continue;
                }
                name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2];
                if (grup.equalsIgnoreCase(name)) {
                    return true;
                }

                // Passo 3
                if (i + 3 == nameSplit.length) {
                    continue;
                }
                name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2] + " " + nameSplit[i + 3];
                if (grup.equalsIgnoreCase(name)) {
                    return true;
                }

                // Passo 4
                if (i + 4 == nameSplit.length) {
                    continue;
                }
                name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2] + " " + nameSplit[i + 3] + " " + nameSplit[i + 4];
                if (grup.equalsIgnoreCase(name)) {
                    return true;
                }

                // Passo 5
                if (i + 5 == nameSplit.length) {
                    continue;
                }
                name = nameSplit[i] + " " + nameSplit[i + 1] + " " + nameSplit[i + 2] + " " + nameSplit[i + 3] + " " + nameSplit[i + 4] + " " + nameSplit[i + 5];
                if (grup.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } else {
            return (grup.equalsIgnoreCase(item));
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
            GRUPO_NAME_LIST.add(grupo.getName());
        }
    }

    @Override
    public String toString() {
        return name;
    }
}