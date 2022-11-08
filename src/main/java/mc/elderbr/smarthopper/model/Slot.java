package mc.elderbr.smarthopper.model;

import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class Slot{

    private int position;
    private Item item;

    public Slot() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
