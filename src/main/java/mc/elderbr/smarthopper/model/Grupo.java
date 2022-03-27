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

public class Grupo extends Traducao {

    private int cdGrupo;
    private String dsGrupo;
    private Map<String, Traducao> traducaoMap = new HashMap<>();
    private List<Item> listItem = new ArrayList<>();

    // AULIXIAR
    public final static int NEW = 0, UPGRADE = 1, DELETE = 2;
    private List<String> grupoList = new ArrayList<>();

    public Grupo() {
        createGrupos();// CRIA GRUPO PADRÃO
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

    public Grupo addList(Item item){
        if(listItem == null){
            listItem = new ArrayList<>();
        }
        listItem.add(item);
        return this;
    }

    public Map<String, Traducao> getTraducaoMap() {
        return traducaoMap;
    }

    public void setTraducaoMap(Map<String, Traducao> traducaoMap) {
        this.traducaoMap = traducaoMap;
    }

    public List<String> createGrupos() {
        grupoList = new ArrayList<>();

        for (String item : VGlobal.ITEM_NAME_LIST) {
            if (item.split("\\s").length > 0) {
                String[] name = item.split("\\s");
                for (int i = 0; i < name.length; i++) {
                    String grupo = name[i];
                    if (!grupoList.contains(grupo)) {
                        grupoList.add(grupo);
                    }
                    if ((i + 1) <= name.length) {
                        grupo = name[i] + " " + name[i + 1];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 2) <= name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 3) <= name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 4) <= name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                    if ((i + 5) <= name.length) {
                        grupo = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4] + " " + name[i + 5];
                        if (!grupoList.contains(grupo)) {
                            grupoList.add(grupo);
                        }
                    }
                }
            }else{
                if (!grupoList.contains(item)) {
                    grupoList.add(item);
                }
            }
        }
        createGrupoItem();
        return grupoList;
    }

    /***
     * Adiciona item na lista do grupo
     */
    public void createGrupoItem(){
        Collections.sort(createGrupos());
        for(String nameGrupo : grupoList){
            Grupo grupo = new Grupo();
            grupo.setDsGrupo(nameGrupo);
            grupo.setDsLang("en_us");
            grupo.setDsTraducao(nameGrupo);
            for(String nameItem : VGlobal.ITEM_NAME_LIST){
                Item item = new Item(nameItem);
                if(grupo.contentItem(item)){
                    grupo.addList(item);
                }
            }
            // LISTA DE NOMES DE GRUPO GLOBAL
            if(!VGlobal.GRUPO_NAME_LIST.contains(grupo.dsGrupo)) {
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
                if ((i + 1) <= name.length) {
                    names = name[i] + " " + name[i + 1];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 2) <= name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 3) <= name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 4) <= name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
                if ((i + 5) <= name.length) {
                    names = name[i] + " " + name[i + 1] + " " + name[i + 2] + " " + name[i + 3] + " " + name[i + 4] + " " + name[i + 5];
                    if (names.equals(dsGrupo)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
