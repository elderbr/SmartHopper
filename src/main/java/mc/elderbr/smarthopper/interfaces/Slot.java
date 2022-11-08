package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.Item;

import java.util.List;

public interface Slot {

    boolean empty();

    Item getSlot(int slot);

    void setSlot(Item item);

    List<Item> getSlotList();

    void setSlotList(List<Item> itemList);

    boolean contentItem(Item item);

    boolean addItem(Item item);

    void removeItem(Item item);

    int sizeSlot();


}
