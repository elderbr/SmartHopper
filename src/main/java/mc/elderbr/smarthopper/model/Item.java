package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.interfaces.msg.ItemMsg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_NAME_LIST;

public class Item implements IItem, ItemMsg, Comparable<Item> {

    private int id = 0;
    private String name;
    private boolean blocked = false;
    private Map<String, String> translation = new HashMap<>();
    private List<Integer> grups = new ArrayList<>();

    public Item() {
    }

    public Item(int code, String name) {
        id = code;
        this.name = name;
    }

    public Item(ItemStack itemStack) {
        name = toItemStack(itemStack);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Item setId(Integer code) {
        id = code;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Item setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public Item setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    @Override
    public Map<String, String> getTranslations() {
        return translation;
    }

    public List<Integer> getGrups() {
        return grups;
    }

    public void addGrups(Integer code) {
        this.grups.add(code);
    }

    /*************************************************************
     *
     *              Metodo que criar os itens
     *
     **************************************************************/
    public static void CreateItem() {
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack.getType() != Material.AIR && itemStack.getType().isItem()) {
                Item item = new Item(itemStack);
                // Se item não pode está na lista
                if (item.getName().equalsIgnoreCase("enchanted book")) continue;
                ITEM_NAME_LIST.add(item.getName());
            }
        }

        Pocao.Create();// POÇÕES E SEU EFEITOS
        LivroEncantado.Create();// LIVRO ENCANTADOS

        // Criando itens com código e nome e adicionando na lista global
        int cod = 1;
        for (String name : ITEM_NAME_LIST) {
            Item item = new Item(cod, name);
            cod++;
        }
    }

    public String toInfor() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blocked=" + blocked +
                ", listGrupo=" + grups +
                '}';
    }

    @Override
    public int compareTo(@NotNull Item newItem) {
        return this.name.compareTo(newItem.name);
    }

    public static String TO_ItemStack(ItemStack itemStack){
        return new Item().toItemStack(itemStack);
    }
}