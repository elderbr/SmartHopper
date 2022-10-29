package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Funil;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Item implements Funil {

    private int codigo = 0;
    private String name;
    private boolean bloqueado;
    private Map<String, String> traducao = new HashMap<>();

    public Item() {
    }

    public Item(ItemStack itemStack) {
        name = toItemStack(itemStack);
        this = ITEM_MAP_NAME.get(name);
    }

    public Item(String name) {
        this = ITEM_MAP_NAME.get(name);
    }

    public static ItemStack ParseItemStack(){
        return new ItemStack(Material.valueOf(name.toUpperCase().replaceAll("\\s","_")));
    }

    public static Item PARSE(ItemStack itemStack) {
        return new Item(itemStack);
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

    public ItemStack parseItemStack(String name){
        return new ItemStack(Material.valueOf(name.toUpperCase().replaceAll("\\s","_")));
    }


}