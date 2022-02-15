package mc.elderbr.smarthopper.model;

import java.util.ArrayList;
import java.util.List;

public class ConfigModel {

    private String name;
    private String versao;
    private List<Adm> listAdm;

    public ConfigModel() {
        listAdm = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toVersao() {
        return versao;
    }

    public Double getVersao(){
        return Double.parseDouble(versao.replaceAll("[.]",""));
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public List<Adm> getAdm() {
        return listAdm;
    }

    public void setAdm(List<Adm> adm) {
        this.listAdm = adm;
    }
}
