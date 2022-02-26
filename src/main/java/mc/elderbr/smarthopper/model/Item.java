package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Item extends Traducao {

    private int cdItem;
    private String dsItem;
    private List<Item> listItem;

    public Item() {
    }

    public Item(ItemStack itemStack) {
        if (itemStack != null)
            dsItem = Utils.toItem(itemStack);
    }

    public Item(Material material) {
        dsItem = Utils.ToMaterial(material);
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

    public List<Item> getListMaterial() {
        listItem = new ArrayList<>();
        for (Material m : Material.values()) {
            Item i = new Item(m);
            if (i.getDsItem() != null && !m.isAir() && m.isItem()) {
                listItem.add(i);
            }
        }
        return listItem;
    }

    public boolean contains(@NotNull Grupo grupo) {
        String name = null;
        String nameGrupo = grupo.getDsGrupo();

        if (dsItem.equalsIgnoreCase(nameGrupo)) return true;

        String[] names = dsItem.split("\\s");

        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(nameGrupo)) {
                return true;
            }
            if ((i + 1) < names.length) {
                name = names[i] + " " + names[i + 1];
                if (name.equalsIgnoreCase(nameGrupo)) {
                    return true;
                }
            }
            if ((i + 2) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2];
                if (name.equalsIgnoreCase(nameGrupo)) {
                    return true;
                }
            }
            if ((i + 3) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3];
                if (name.equalsIgnoreCase(nameGrupo)) {
                    return true;
                }
            }
            if ((i + 4) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3] + " " + names[i + 4];
                if (name.equalsIgnoreCase(nameGrupo)) {
                    return true;
                }
            }
            if ((i + 5) < names.length) {
                name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3] + " " + names[i + 4]+ " " + names[i + 5];
                if (name.equalsIgnoreCase(nameGrupo)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return (getDsTraducao() != null ? getDsTraducao() : dsItem);
    }

    public void setLang(Lang lang) {
        setCdLang(lang.getCdLang());
        setDsLang(lang.getDsLang());
    }
}
