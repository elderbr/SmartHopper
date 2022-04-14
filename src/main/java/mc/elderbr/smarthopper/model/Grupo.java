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

    public Grupo addTraducao(Traducao traducao) {
        traducaoMap.put(traducao.getDsLang(), traducao.getDsTraducao());
        return this;
    }

    public Grupo addTraducao(String lang, String traducao) {
        traducaoMap.put(lang, traducao);
        return this;
    }

    public Map<String, String> getTraducaoMap() {
        return traducaoMap;
    }

    public String toTraducao() {
        if(!traducaoMap.isEmpty()) {
            dsTraducao = traducaoMap.get(dsLang);
        }
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

        // LIVROS ENCANTADOS
        grupoList.add("aqua affinity");
        grupoList.add("bane of arthropods");
        grupoList.add("binding curse");
        grupoList.add("blast protection");
        grupoList.add("channeling");
        grupoList.add("depth strider");
        grupoList.add("efficiency");
        grupoList.add("feather falling");
        grupoList.add("fire aspect");
        grupoList.add("fire protection");
        grupoList.add("flame");
        grupoList.add("fortune");
        grupoList.add("frost walker");
        grupoList.add("impaling");
        grupoList.add("infinity");
        grupoList.add("knockback");
        grupoList.add("looting");
        grupoList.add("loyalty");
        grupoList.add("luck of the sea");
        grupoList.add("lure");
        grupoList.add("mending");
        grupoList.add("multishot");
        grupoList.add("piercing");
        grupoList.add("power");
        grupoList.add("projectile protection");
        grupoList.add("protection");
        grupoList.add("punch");
        grupoList.add("quick charge");
        grupoList.add("respiration");
        grupoList.add("riptide");
        grupoList.add("sharpness");
        grupoList.add("silk touch");
        grupoList.add("smite");
        grupoList.add("soul speed");
        grupoList.add("sweeping");
        grupoList.add("thorns");
        grupoList.add("unbreaking");
        grupoList.add("vanishing curse");


        // POÇÕES
        grupoList.add("awkward");
        grupoList.add("fire resistance");
        grupoList.add("instant damage");
        grupoList.add("instant heal");
        grupoList.add("jump");
        grupoList.add("luck");
        grupoList.add("mundane");
        grupoList.add("night vision");
        grupoList.add("poison");
        grupoList.add("regen");
        grupoList.add("slow falling");
        grupoList.add("slowness");
        grupoList.add("speed");
        grupoList.add("strength");
        grupoList.add("thick");
        grupoList.add("turtle master");
        grupoList.add("uncraftable");
        grupoList.add("water");
        grupoList.add("water breathing");
        grupoList.add("weakness");





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
            if (NotGrupo().contains(nameGrupo)) continue;

            Grupo grupo = new Grupo();
            grupo.setDsGrupo(nameGrupo);
            for (Item item : VGlobal.ITEM_MAP_NAME.values()) {
                if (item == null) continue;
                if (pertence(grupo, item) && grupo.contentItem(item)) {
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

    public boolean contentsItem(Item item){
        Grupo grupo = VGlobal.GRUPO_MAP_NAME.get(dsGrupo);
        if(grupo == null) return false;
        for(Item items : grupo.getListItem()){
            if(items.getCdItem() == item.getCdItem()){
                return true;
            }
        }
        return false;
    }

    /***
     * Percorre o nome do item verifica se contém espaço se sim compara os nomes separados
     * @param item nome do item
     * @return falso ou verdadeiro
     */
    private boolean contentItem(Item item) {
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
    private static boolean pertence(Grupo grupo, Item item) {
        switch (grupo.getDsGrupo()) {
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
                    if (item.getDsItem().contains(items)) {
                        return false;
                    }
                }
            case "oak":
                if (item.getDsItem().contains("dark oak")) {
                    return false;
                }
            case "sandstone":
                if (item.getDsItem().contains("red sandstone")) {
                    return false;
                }
            default:
                return true;
        }
    }

}
