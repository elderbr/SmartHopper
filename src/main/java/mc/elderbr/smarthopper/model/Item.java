package mc.elderbr.smarthopper.model;

public class Item extends Traducao{

    private int cdItem = 0;
    private String dsItem;

    public Item() {
    }

    public int getCdItem() {
        return cdItem;
    }

    public void setCdItem(int cdItem) {
        this.cdItem = cdItem;
    }

    public String getDsItem() {
        return dsItem;
    }

    public void setDsItem(String dsItem) {
        this.dsItem = dsItem;
    }
}
