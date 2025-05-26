package mc.elderbr.smarthopper.interfaces;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface ITitle {

    String TITLE_SMART_HOPPER = "§f§lSmart Hopper";

    // TITULOS PARA ITENS
    String TITLE_ID_ITEM = "§lID:§r";
    String TITLE_NAME_ITEM = "§2§lItem:§r";

    // TITULOS PARA GRUPOS
    String TITLE_GROUP = "§lGrupo:§r";
    String TITLE_ID_GROUP = "§lID:§r";
    String TITLE_NAME_GROUP = "§3§lGrupo:§r";
    String TITLE_NEW_GROUP = "§f§lNovo Grupo:§r";

    // TITULOS PARA RECEITAS
    String TITLE_RECIPE = "§a§lSmart Hopper§2";
    String NAME_RECIPE = TITLE_SMART_HOPPER;

    default List<String> getTitles() {
        List<String> list = new ArrayList<>();
        for (Field field : ITitle.class.getFields()) {
            if (field.getType().equals(String.class)) {
                try {
                    if (!field.getName().startsWith("TITLE_")) continue;
                    // Pegando o valor do campo estático
                    String title = (String) field.get(null);
                    if (title.contains("ID")) continue;
                    list.add(title.trim());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    default boolean containsTitle(String title) {
        return getTitles().stream().anyMatch(t -> title.contains(t));
    }

}
