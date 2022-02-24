package mc.elderbr.smarthopper.model;

import java.util.ArrayList;
import java.util.List;

public class Grupo extends Traducao {

    private int cdGrupo;
    private String dsGrupo;
    private List<Item> listItem;

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
        if (listItem == null) {
            listItem = new ArrayList<>();
        }
        return listItem;
    }

    public void setListItem(List<Item> listItem) {
        this.listItem = listItem;
    }

    public List<Item> addItem(Item item) {
        if (listItem == null) {
            listItem = new ArrayList<>();
        }
        listItem.add(item);
        return listItem;
    }

    public boolean contains(Item item) {
        String name = null;
        String[] names = item.getDsItem().split("\\s");

        if(dsGrupo.equalsIgnoreCase(item.getDsItem())) return true;

        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(dsGrupo)) {
                return true;
            }
            if ((i + 1) < names.length) {
                name = names[i] + " " + names[i + 1];
                if (name.equalsIgnoreCase(dsGrupo)) return true;
            }
            if ((i + 2) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2];
                if (name.equalsIgnoreCase(dsGrupo)) return true;
            }
            if ((i + 3) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2]+ " " + names[i + 3];
                if (name.equalsIgnoreCase(dsGrupo)) return true;
            }
            if ((i + 4) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2]+ " " + names[i + 3]+ " " + names[i + 4];
                if (name.equalsIgnoreCase(dsGrupo)) return true;
            }
            if ((i + 5) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2]+ " " + names[i + 3]+ " " + names[i + 4]+ " " + names[i + 5];
                if (name.equalsIgnoreCase(dsGrupo)) return true;
            }
        }
        return false;
    }

    public void setLang(Lang lang) {
        setCdLang(lang.getCdLang());
        setDsLang(lang.getDsLang());
    }

    @Override
    public String toString() {
        return (getDsTraducao() != null ? getDsTraducao() : dsGrupo);
    }
}
