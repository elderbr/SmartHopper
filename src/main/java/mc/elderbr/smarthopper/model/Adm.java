package mc.elderbr.smarthopper.model;

public class Adm {
    private int cdAdm;
    private String dsAdm;
    private String dsUuid;

    public Adm() {
    }

    public Adm(String dsAdm) {
        this.dsAdm = dsAdm;
    }

    public int getCdAdm() {
        return cdAdm;
    }

    public void setCdAdm(int cdAdm) {
        this.cdAdm = cdAdm;
    }

    public String getDsAdm() {
        return dsAdm;
    }

    public void setDsAdm(String dsAdm) {
        this.dsAdm = dsAdm;
    }

    public String getDsUuid() {
        return dsUuid;
    }

    public void setDsUuid(String dsUuid) {
        this.dsUuid = dsUuid;
    }
}
