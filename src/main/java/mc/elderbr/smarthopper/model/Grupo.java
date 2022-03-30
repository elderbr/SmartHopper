package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Linguagem;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.entity.Player;

import java.util.*;

public class Grupo implements Linguagem {

    private int cdGrupo;
    private String dsGrupo;

    // LANG
    private int cdLang;
    private String dsLang;

    // TRADUCAO
    private int cdTraducao;
    private String dsTraducao;

    // LISTA DO TRADUÇÃO
    private Map<String, String> traducaoMap = new HashMap<>();
    private List<Item> listItem = new ArrayList<>();

    // AULIXIAR
    private static List<String> grupoList = new ArrayList<>();

    public Grupo() {
    }

    public int getCdGrupo() {
        return cdGrupo;
    }

    public void setCdGrupo(int cdGrupo) {
        this.cdGrupo = cdGrupo;
    }

    public String getDsGrupo() {
        return dsGrupo;
    }

    public void setDsGrupo(String dsGrupo) {
        this.dsGrupo = dsGrupo;
    }

    @Override
    public Grupo setCdLang(int codigo) {
        cdLang = codigo;
        return this;
    }

    @Override
    public int getCdLang() {
        return cdLang;
    }

    @Override
    public Grupo setDsLang(String lang) {
        dsLang = lang;
        return this;
    }

    @Override
    public Grupo setDsLang(Player player) {
        dsLang = player.getLocale();
        return this;
    }

    @Override
    public String getDsLang() {
        return dsLang;
    }

    @Override
    public Grupo setCdTraducao(int codigo) {
        cdTraducao = codigo;
        return this;
    }

    @Override
    public int getCdTraducao() {
        return cdTraducao;
    }

    @Override
    public Grupo setDsTraducao(String traducao) {
        dsTraducao = traducao;
        return this;
    }

    @Override
    public String getDsTraducao() {
        return dsTraducao;
    }

    public Grupo addTraducao(String lang, String traducao) {
        traducaoMap.put(lang, traducao);
        return this;
    }

    public String toTraducao() {
        dsTraducao = traducaoMap.get(dsLang);
        return (dsTraducao == null ? dsGrupo : dsTraducao);
    }

    public List<Item> getListItem() {
        return listItem;
    }

    public Grupo addList(Item item) {
        if (listItem == null) {
            listItem = new ArrayList<>();
        }
        listItem.add(item);
        return this;
    }


    public static List<String> CreateGrupos() {
        grupoList = new ArrayList<>();

        for (String item : VGlobal.ITEM_NAME_LIST) {
            if (item.split("\\s").length > 0) {
                String[] name = item.split("\\s");
                for (int i = 0; i < name.length; i++) {
                    String grupo = name[i];
                    if (!grupoList.contains(grupo)) {
                        grupoList.add(grupo);
                    }
                    if ((i + 1) < name.length) {
                        grupo = name[i] + " " + name[i + 1];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 2) < name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 3) < name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 4) < name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 5) < name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4] + " " + name[i + 5];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                }
            } else {
                if (!grupoList.contains(item)) {
                    grupoList.add(item);
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
        Collections.sort(grupoList);
        for (String nameGrupo : grupoList) {

            // NÃO É NOME VALIDO DE GRUPOS
            if(NotGrupo().contains(nameGrupo)) continue;

            Grupo grupo = new Grupo();
            grupo.setDsGrupo(nameGrupo);
            for (Item item : VGlobal.ITEM_MAP_NAME.values()) {
                if(item == null ) continue;
                if (grupo.contentItem(item)) {
                    grupo.addList(item);
                }
            }

            // Ferramentas de Pedras
            if (grupo.getDsGrupo().equals("stone tools")) {
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("stone sword"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("stone shovel"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("stone pickaxe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("stone axe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("stone hoe"));
            }

            // Ferramentas de Ferro
            if (grupo.getDsGrupo().equals("iron tools")) {
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("iron sword"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("iron shovel"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("iron pickaxe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("iron axe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("iron hoe"));
            }

            // Ferramentas de Ouro
            if (grupo.getDsGrupo().equals("golden tools")) {
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("golden sword"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("golden shovel"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("golden pickaxe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("golden axe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("golden hoe"));
            }

            // Ferramentas de Diamante
            if (grupo.getDsGrupo().equals("diamond tools")) {
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("diamond sword"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("diamond shovel"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("diamond pickaxe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("diamond axe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("diamond hoe"));
            }

            // Ferramentas de netherite
            if (grupo.getDsGrupo().equals("netherite tools")) {
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("netherite sword"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("netherite shovel"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("netherite pickaxe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("netherite axe"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("netherite hoe"));
            }

            // ITENS PARA SEREM ASSADOS
            if (grupo.getDsGrupo().equals("carne crua")) {
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("potato"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("beef"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("porkchop"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("mutton"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("chicken"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("rabbit"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("cod"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("salmon"));
                grupo.addList(VGlobal.ITEM_MAP_NAME.get("kelp"));
            }

            // LISTA DE NOMES DE GRUPO GLOBAL
            if (grupo.getListItem().size() > 1 && !VGlobal.GRUPO_NAME_LIST.contains(grupo.dsGrupo)) {
                VGlobal.GRUPO_NAME_LIST.add(grupo.dsGrupo);
                VGlobal.GRUPO_LIST.add(grupo);
            }
        }
    }

    /***
     * Percorre o nome do item verifica se contém espaço se sim compara os nomes separados
     * @param item nome do item
     * @return falso ou verdadeiro
     */
    public boolean contentItem(Item item) {
        // Se o nome do grupo conter mais do que nome
        if (dsGrupo.equals(item.getDsItem())) return true;
        if (item.getDsItem().split("\\s").length > 0) {
            String[] name = item.getDsItem().split("\\s");
            for (int i = 0; i < name.length; i++) {
                String names = name[i];
                if (names.equals(dsGrupo)) {
                    return true;
                }
                if ((i + 1) < name.length) {
                    names = name[i] + " " + name[i + 1];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 2) < name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 3) < name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 4) < name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 5) < name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4] + " " + name[i + 5];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static List<String> NotGrupo(){
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

}
