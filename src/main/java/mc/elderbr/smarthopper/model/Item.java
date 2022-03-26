package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.Traducao;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Item implements Dados, Traducao {

    private int cdItem;
    private String dsItem;
    private int cdLang;
    private String dsLang;
    private int cdTraducao;
    private String dsTraducao;

    public Item() {
    }

    public Item(ItemStack itemStack) {
        dsItem = Utils.toItem(itemStack);
    }


    @Override
    public int getCodigo() {
        return cdItem;
    }

    @Override
    public void setCodigo(int codigo) {
        cdItem = codigo;
    }

    public void setCodigo(String codigo) throws NumberFormatException {
        cdItem = Integer.parseInt(codigo);
    }

    @Override
    public String getName() {
        return dsItem;
    }

    @Override
    public void setName(String name) {
        dsItem = name;
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

    public void setDsLang(Lang lang) {
        cdLang = lang.getCdLang();
        dsLang = lang.getDsLang();
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
                name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3] + " " + names[i + 4] + " " + names[i + 5];
                if (name.equalsIgnoreCase(nameGrupo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Item> getListMaterial() {
        List<Item> list = new ArrayList<>();
        for (Material m : Material.values()) {
            try {
                ItemStack itemStack = new ItemStack(m);
                if (itemStack != null) {
                    list.add(new Item(itemStack));
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return (dsTraducao != null ? dsTraducao : dsItem);
    }
}
