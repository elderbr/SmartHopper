package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface InterfaceInventario extends Dados {

    // JOGADOR
    String setPlayer(@NotNull Player player);
    Player getPlayer();
    boolean isAdm();

    default String toTitulo(Player player) {
        if (getCodigo() > 0) {
            return Msg.Color("§lGrupo: §r" + toTraducao(getPlayer().getLocale()) + " §lID: §r" + getCodigo());
        } else {
            return Msg.Color("§lGrupo: §r" + toTraducao(getPlayer().getLocale()));
        }
    }

    List<String> getLista();

    default List<String> addLista(@NotNull Object item) {
        String name = null;
        if (item instanceof ItemStack stack) {
            name = new Item(stack).getName();
        }
        if (item instanceof Item items) {
            name = items.getName();
        }
        if (name != null && !getLista().contains(name)) {
            getLista().add(name);
        }
        Collections.sort(getLista());
        return getLista();
    }

    default List<String> removeLista(@NotNull ItemStack itemStack) {
        Item item = new Item(itemStack);
        if (getLista().contains(item.getName())) {
            getLista().remove(item.getName());
        }
        Collections.sort(getLista());
        return getLista();
    }

    default boolean containsItem(@NotNull Object item) {
        String name = null;
        if (item instanceof ItemStack stack) {
            name = new Item(stack).getName();
        }
        if (item instanceof Item items) {
            name = items.getName();
        }
        if (item instanceof String n) {
            name = n;
        }
        return getLista().contains(name);
    }

    InventarioType getType();

}
