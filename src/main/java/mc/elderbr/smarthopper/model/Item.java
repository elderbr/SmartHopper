package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.inventory.ItemStack;

public class Item extends Traducao {

    private int cdItem;
    private String dsItem;

    public Item() {
    }

    public Item(ItemStack itemStack) {
        if (itemStack != null)
            dsItem = Utils.toItem(itemStack);
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

    @Override
    public String toString() {
        return (getDsTraducao()!=null?getDsTraducao():dsItem);
    }
}
