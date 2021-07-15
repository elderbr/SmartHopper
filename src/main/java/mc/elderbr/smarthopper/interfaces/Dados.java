package mc.elderbr.smarthopper.interfaces;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface Dados extends Lang {
    int getID();
    void setID(int id);
    String getName();
    void setName(String name);
    Location getLocation();
    void setLocation(Block block);
}
