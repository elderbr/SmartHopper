package mc.elderbr.smarthopper.model;

import java.util.ArrayList;
import java.util.List;

public class ConfigModel {

    private String name;
    private double versao;
    private List<String> lang;
    private List<String> adm;
    private List<String> operador;

    public ConfigModel() {
        lang = new ArrayList<>();
        adm = new ArrayList<>();
        operador = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVersao() {
        return versao;
    }

    public void setVersao(double versao) {
        this.versao = versao;
    }

    public List<String> getLang() {
        return lang;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public void addLang(String lang){
        if(this.lang==null){
            this.lang=new ArrayList<>();
        }
        this.lang.add(lang);
    }

    public List<String> getAdm() {
        return adm;
    }

    public void setAdm(List<String> adm) {
        this.adm = adm;
    }

    public void addAdm(String adm){
        if(this.adm==null){
            this.adm=new ArrayList<>();
        }
        this.adm.add(adm);
    }

    public List<String> getOperador() {
        return operador;
    }

    public void setOperador(List<String> operador) {
        this.operador = operador;
    }

    public void addOperador(String operador){
        if(this.operador==null){
            this.operador=new ArrayList<>();
        }
        this.operador.add(operador);
    }
}
