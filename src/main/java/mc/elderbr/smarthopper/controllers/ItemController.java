package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class ItemController {

    private int id;
    private String name;
    private Item item;

    public ItemController() {
    }

    public Item getItem(@NotNull Object obj) throws ItemException {
        id = 0;
        name = null;
        if (obj instanceof Integer number) {
            id = number;
        }
        if (obj instanceof String nameItem) {
            try {
                id = Integer.parseInt(nameItem);
            } catch (NumberFormatException e) {
                name = nameItem;
            }
        }
        if (obj instanceof ItemStack itemStack) {
            name = Item.ToName(itemStack);
        }

        if (id > 0) {
            item = ITEM_MAP_ID.get(id);
        }
        if (name != null) {
            item = ITEM_MAP_NAME.get(name);
            if (item == null) {
                item = TRADUCAO_ITEM.get(name);
            }
        }
        if (item == null) {
            throw new ItemException("Digite o nome ou ID do item, ou segure um item na mão!!!");
        }
        return item;
    }

    public boolean addTranslation(@NotNull Player player, @NotNull String translation) throws ItemException {
        if (!Config.CONTAINS_ADD(player)) {
            throw new ItemException("Você não tem permissão para adicionar a tradução ao item!!!");
        }
        if (translation.length() < 3) {
            throw new ItemException("O tradução precisa conter mais do que 2 letras!!!");
        }
        if (TRADUCAO_ITEM.get(translation) != null) {
            throw new ItemException("Essa tradução já existe!!!");
        }
        if (item == null) {
            throw new ItemException("Selecione o item para traduzir!!!");
        }
        item.addTranslation(player.getLocale(), translation);
        TRADUCAO_ITEM.put(translation, item);
        ItemConfig.ADD_TRADUCAO(item);
        Item.SET(item);
        return true;
    }
}
