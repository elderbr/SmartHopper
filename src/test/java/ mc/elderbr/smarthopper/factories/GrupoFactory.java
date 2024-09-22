package factories;

import mc.elderbr.smarthopper.model.Grupo;

public class GrupoFactory {

    public static Grupo notExist() {
        Grupo grupo = new Grupo();
        grupo.setId(100);
        grupo.setName("Not Existe");
        return grupo;
    }

    public static Grupo getInstance(int code, String name) {
        Grupo grupo = new Grupo();
        grupo.setId(code);
        grupo.setName(name);
        return grupo;
    }

    public static Grupo stone() {
        Grupo grupo = new Grupo();
        grupo.setId(1);
        grupo.setName("stone");
        grupo.addItems(ItemFactory.stone());
        grupo.addItems(ItemFactory.stoneStairs());
        grupo.addItems(ItemFactory.stoneSlab());
        grupo.addTranslation("pt_br", "Pedras");
        return grupo;
    }

    public static Grupo pickaxe() {
        Grupo grupo = new Grupo();
        grupo.setId(1);
        grupo.setName("pickaxe");
        grupo.addItems(ItemFactory.diamondPickaxe());
        grupo.addItems(ItemFactory.ironPickaxe());
        grupo.addTranslation("pt_br", "Picaretas");
        return grupo;
    }

    public static Grupo redstone() {
        Grupo grupo = new Grupo();
        grupo.setId(1);
        grupo.setName("redstone");
        grupo.addItems(ItemFactory.diamondPickaxe());
        grupo.addItems(ItemFactory.ironPickaxe());
        grupo.addTranslation("pt_br", "Redstones");
        return grupo;
    }
}
