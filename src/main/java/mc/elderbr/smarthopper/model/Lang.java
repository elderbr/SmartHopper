package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Linguagem;

public class Lang implements Linguagem {

    private int cdLang;
    private String dsLang;

    public Lang() {
    }

    @Override
    public int getCdLang() {
        return cdLang;
    }

    @Override
    public void setCdLang(int cdLang) {
        this.cdLang = cdLang;
    }

    @Override
    public String getDsLang() {
        return dsLang;
    }

    @Override
    public void setDsLang(String dsLang) {
        this.dsLang = dsLang;
    }
}
