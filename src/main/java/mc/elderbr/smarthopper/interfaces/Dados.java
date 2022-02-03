package mc.elderbr.smarthopper.interfaces;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface Dados{
    int getID();
    void setID(int id);
    String getName();
    void setName(String name);
    String lang();
    void setLang(String lang);
    String traducao();
    void setTraducao(String traducao);
}
