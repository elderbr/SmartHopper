package mc.elderbr.smarthopper.model;

public class Traducao extends Lang {

    private int cdTraducao;
    private String dsTraducao;


    public Traducao() {
    }

    public int getCdTraducao() {
        return cdTraducao;
    }

    public Traducao setCdTraducao(int cdTraducao) {
        this.cdTraducao = cdTraducao;
        return this;
    }

    public String getDsTraducao() {
        return dsTraducao;
    }

    public Traducao setDsTraducao(String dsTraducao) {
        this.dsTraducao = dsTraducao;
        return this;
    }
}