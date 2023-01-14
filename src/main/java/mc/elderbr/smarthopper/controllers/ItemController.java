package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
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

    public Item getItem() {
        return item;
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
            throw new ItemException("Não foi possivél encontrar o item, digite o nome ou ID do item, ou segure um item na mão!!!");
        }
        return item;
    }

    public boolean addTranslation(@NotNull Player player, @NotNull String[] args) throws ItemException {

        // Verifica se o jogar é administrador do SmartHopper
        if (!Config.CONTAINS_ADD(player)) {
            throw new ItemException("Você não tem permissão para adicionar a tradução ao item!!!");
        }

        // Verifica se existe o código do item e a tradução
        if(args.length < 2){
            throw new ItemException("Digite /traducaoitem <código do item> <tradução>!!!");
        }

        // Pegando a tradução
        String translation = convertTranslation(args);
        // A tradução precisa conter mais do 2 letras
        if (translation.length() < 3) {
            throw new ItemException("O tradução precisa conter mais do que 2 letras!!!");
        }

        try{
            id = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            throw new ItemException("O código "+ args[0] +" não é valido!!!");
        }
        item = ITEM_MAP_ID.get(id);
        if (item == null) {
            throw new ItemException("O código "+ args[0] +" não está na lista de item!!!");
        }
        item.addTranslation(player.getLocale(), translation);
        TRADUCAO_ITEM.put(translation, item);
        return ItemConfig.ADD_TRADUCAO(item);
    }

    private String convertTranslation(String[] args){
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < args.length; i++){
            sb.append(args[i].concat(" "));
        }
        return sb.toString().trim();
    }
}
