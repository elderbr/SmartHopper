package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Grupo extends Traducao{

    private int cdGrupo;
    private String dsGrupo;
    private Map<String, Traducao> traducaoMap = new HashMap<>();
    private List<Item> listItem = new ArrayList<>();
    public final static int NEW = 0, UPGRADE = 1, DELETE = 2;

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

    public List<Item> getListItem() {
        return listItem;
    }

    public void setListItem(List<Item> listItem) {
        this.listItem = listItem;
    }

    public Map<String, Traducao> getTraducaoMap() {
        return traducaoMap;
    }

    public void setTraducaoMap(Map<String, Traducao> traducaoMap) {
        this.traducaoMap = traducaoMap;
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
        // Se o nome do grupo conter mais do que nome
        if (this.dsGrupo.split("\\s").length > 1) {
            if (item.contains(dsGrupo)) {
                return true;
            }
        } else {
            //Percorre o nome do item e ver se o nome do grupo existe
            for (String nameItem : item.split("\\s")) {
                if (nameItem.equals(this.dsGrupo)) {
                    return true;
                }
            }
        }
        return false;
    }

}
