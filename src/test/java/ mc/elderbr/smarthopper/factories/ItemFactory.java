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
        Item item = new Item(1069, "stone");
        item.addTranslation("pt_br", "Pedra");
        return item;
    }

    public static Item stoneStairs() {
        Item item = new Item(1081, "stone stairs");
        item.addTranslation("pt_br", "Escada de pedra");
        return item;
    }

    public static Item stoneSlab() {
        Item item = new Item(1080, "stone slab");
        item.addTranslation("pt_br", "Laje de pedra");
        return item;
    }

    public static Item ironPickaxe() {
        Item item = new Item(534, "iron pickaxe");
        item.addTranslation("pt_br", "Picareta de ferro");
        return item;
    }

    public static Item diamondPickaxe() {
        Item item = new Item(338, "diamond pickaxe");
        item.addTranslation("pt_br", "Picareta de diamante");
        return item;
    }

    public static Item redstone() {
        Item item = new Item(934, "redstone");
        item.addTranslation("pt_br", "Pó de redstone");
        return item;
    }

    public static Item repeater() {
        Item item = new Item(940, "repeater");
        item.addTranslation("pt_br", "Repetidor");
        return item;
    }
}
