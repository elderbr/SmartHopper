package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Funil;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_NAME;

public class Item implements Funil {

    private int codigo = 0;
    private String name;
    private boolean bloqueado = false;
    private Map<String, String> traducao = new HashMap<>();

    public Item() {
    }

    public Item(ItemStack itemStack) {
        name = toItemStack(itemStack);
    }

    public Item(String name) {
        this.name = name;
    }



    @Override
    public Funil setCodigo(int codigo) {
        this.codigo = codigo;
        return this;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public Funil setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBloqueado() {
        return bloqueado;
    }

    @Override
    public Funil setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
        return this;
    }

    @Override
    public Map<String, String> getTraducao() {
        return traducao;
    }

    public String toItemStack(ItemStack itemStack) {
        return itemStack.getType().getKey().getKey().toLowerCase().replaceAll("_", " ");
    }

    public ItemStack parseItemStack() {
        return new ItemStack(Material.valueOf(name.toUpperCase().replaceAll("\\s", "_")));
    }

    public ItemStack parseItemStack(String name) {
        return new ItemStack(Material.valueOf(name.toUpperCase().replaceAll("\\s", "_")));
    }

    public static Item PARSE(ItemStack itemStackInventory) {
        return ITEM_MAP_NAME.get(itemStackInventory.getType().getKey().getKey().toLowerCase().replaceAll("_", " "));
    }

    public static void CreateItem() {
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType().isItem()) {
                Item item = new Item(itemStack);
                if (!VGlobal.ITEM_NAME_LIST.contains(item.getName())) {
                    VGlobal.ITEM_NAME_LIST.add(item.getName());
                }
            }
        }
        // POÇÕES E SEU EFEITOS
        for (PotionType potion : PotionType.values()) {
            String name = "potion " + potion.name().replaceAll("_", " ").toLowerCase();
            if (!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add(name);
            }
            if (!VGlobal.ITEM_NAME_LIST.contains("splash " + name)) {
                VGlobal.ITEM_NAME_LIST.add("splash " + name);
            }
            if (!VGlobal.ITEM_NAME_LIST.contains("lingering " + name)) {
                VGlobal.ITEM_NAME_LIST.add("lingering " + name);
            }
        }


        // LIVRO ENCANTADOS
        for (Enchantment enchantment : Enchantment.values()) {
            String name = "enchanted book " + enchantment.getKey().getKey().replaceAll("_", " ");
            if (!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add(name);
            }
        }
        Collections.sort(VGlobal.ITEM_NAME_LIST);

        // Criando itens com código e nome e adicionando na lista global
        int cod = 1;
        for (String name : VGlobal.ITEM_NAME_LIST) {
            Item item = new Item();
            item.setCodigo(cod);
            item.setName(name);
            VGlobal.ITEM_LIST.add(item);
            cod++;
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "codigo=" + codigo +
                ", name='" + name + '\'' +
                ", bloqueado=" + bloqueado +
                '}';
    }
}