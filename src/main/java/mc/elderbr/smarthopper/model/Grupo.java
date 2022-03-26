package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Traducao;
import mc.elderbr.smarthopper.interfaces.VGlobal;

import java.util.ArrayList;
import java.util.List;

public class Grupo implements Traducao {

    private int cdGrupo;
    private String dsGrupo;

    private int cdLang;
    private String dsLang;

    private int cdTraducao;
    private String dsTraducao;
    private List<Item> listItem;

    public Grupo() {
    }

    public int getCdGrupo() {
        return cdGrupo;
    }

    public void setCdGrupo(int cdGrupo) {
        this.cdGrupo = cdGrupo;
    }

    public void setCdGrupo(String cdGrupo) throws NumberFormatException {
        this.cdGrupo = Integer.parseInt(cdGrupo);
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

    @Override
    public int getCdLang() {
        return cdLang;
    }

    @Override
    public void setCdLang(int lang) {
        cdLang = lang;
    }

    @Override
    public String getDsLang() {
        return dsLang;
    }

    @Override
    public void setDsLang(String lang) {
        dsLang = lang;
    }

    @Override
    public int getCdTraducao() {
        return cdTraducao;
    }

    @Override
    public void setCdTraducao(int codigo) {
        cdTraducao = codigo;
    }

    @Override
    public String getDsTraducao() {
        return dsTraducao;
    }

    @Override
    public void setDsTraducao(String traducao) {
        dsTraducao = traducao;
    }

    public void addItem(Item item) {
        if (listItem == null) listItem = new ArrayList<>();
        listItem.add(item);
    }

    public void setLang(Lang lang) {
        cdLang = lang.getCdLang();
        dsLang = lang.getDsLang();
    }

    @Override
    public String toString() {
        return (dsTraducao != null ? dsTraducao : dsGrupo);
    }
}
