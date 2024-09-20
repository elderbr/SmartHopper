package factories;

import mc.elderbr.smarthopper.model.Item;

public class ItemFactory {

    public static Item notExist() {
        return new Item(100, "not existe");
    }

    public static Item item(int code, String name) {
        return new Item(code, name);
    }

    public static Item stone() {
        Item item = new Item(1, "stone");
        item.addTranslation("pt_br", "Pedra");
        return item;
    }

    public static Item ironPickaxe() {
        Item item = new Item(2, "iron pickaxe");
        item.addTranslation("pt_br", "Picareta de ferro");
        return item;
    }

    public static Item diamondPickaxe() {
        Item item = new Item(3, "diamond pickaxe");
        item.addTranslation("pt_br", "Picareta de diamante");
        return item;
    }

    public static Item redstone() {
        Item item = new Item(4, "redstone");
        item.addTranslation("pt_br", "Pó de redstone");
        return item;
    }

    public static Item repeater() {
        Item item = new Item(5, "repeater");
        item.addTranslation("pt_br", "Repetidor");
        return item;
    }
}
