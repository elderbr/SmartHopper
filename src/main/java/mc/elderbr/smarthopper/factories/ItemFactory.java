package mc.elderbr.smarthopper.factories;

import mc.elderbr.smarthopper.model.Item;

public class ItemFactory {

    public static Item vazio(){
        Item item = new Item();
        item.setId(0);
        item.setName("vazio");
        return item;
    }
}
