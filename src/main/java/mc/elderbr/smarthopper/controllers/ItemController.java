package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.LivroEncantado;
import mc.elderbr.smarthopper.model.Pocao;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_ID;
import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_NAME;

public class ItemController {

    private int codigo;
    private String name;

    private Item item;
    private Pocao pocao;
    private LivroEncantado livroEncantado;

    public ItemController() {
    }

    public Item getItem(@NotNull Object name) throws ItemException {

        if (name instanceof String itemName) {// Se for digitado texto
            this.name = name.toString();
            if (itemName.isEmpty()) {
                throw new ItemException("O nome do item não pode ser vazio!");
            }
            try {
                codigo = Integer.parseInt(itemName);
                item = ITEM_MAP_ID.get(codigo);
            } catch (NumberFormatException e) {
                item = ITEM_MAP_NAME.get(this.name);
            }
        }

        // Se for número
        if (name instanceof Integer codigoItem) {
            codigo = codigoItem;
            item = ITEM_MAP_ID.get(codigo);
        }

        // Se for Item do minecraft
        if (name instanceof ItemStack itemStack) {

            if (itemStack.getType() == Material.AIR) {
                throw new ItemException("Segure um item na mão ou escreva o nome ou ID!!!");
            }

            this.name = Item.ToName(itemStack);
            item = ITEM_MAP_NAME.get(this.name);

            if (this.name.contains("potion")) {
                pocao = new Pocao(itemStack);
                item = pocao.getItem();
            }
            if (this.name.contains("enchanted book")) {// ENCHANTED_BOOK
                livroEncantado = new LivroEncantado(itemStack);
                item = livroEncantado.getItem();
            }
        }

        if (item == null) {
            throw new ItemException("$eO item$4 " + this.name + "$c NÃO $eexiste!!!");
        }

        codigo = item.getCodigo();
        this.name = item.getName();

        return item;
    }
}
