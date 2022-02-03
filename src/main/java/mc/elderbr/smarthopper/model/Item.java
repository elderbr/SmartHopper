package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Item extends ItemStack implements Dados {

    private int id = 0;
    private String name;
    private String lang;
    private Traducao traducao;

    public Item() {
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void setID(int id) {

    }

    @Override
    public String getName() {
        return super.getType().name().toLowerCase().replaceAll("_"," ");
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String lang() {
        return null;
    }

    @Override
    public void setLang(String lang) {

    }

    @Override
    public String traducao() {
        return null;
    }

    @Override
    public void setTraducao(String traducao) {

    }
}
