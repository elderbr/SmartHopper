package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Funil;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_NAME;

public class Item implements Funil {

    private int codigo = 0;
    private String name;
    private boolean bloqueado = false;
    private Map<String, String> traducao = new HashMap<>();

    private ItemStack itemStack;

    public Item() {
    }

    public Item(ItemStack itemStack) {
        name = toItemStack(itemStack);
        this.itemStack = itemStack;
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
        ItemStack itemStack = null;
        if (name.contains("potion")) {
            String potion = null;
            if (name.contains("lingering")) {
                potion = name.replaceAll("lingering potion", "").trim();
                itemStack = new ItemStack(Material.LINGERING_POTION);
            } else if (name.contains("splash")) {
                potion = name.replaceAll("splash potion", "").trim();
                itemStack = new ItemStack(Material.SPLASH_POTION);
            } else {
                potion = name.replaceAll("potion", "").trim();
                itemStack = new ItemStack(Material.POTION);
            }

            if (!potion.isEmpty()) {
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(potion.replaceAll("\\s", "_").toUpperCase())));
                itemStack.setItemMeta(meta);
            }
            return itemStack;
        }
        if (name.contains("enchanted book")) {
            itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            //
            String encantamento = getName().replaceAll("enchanted book", "").trim();
            NamespacedKey key = null;
            for (Enchantment enchantment : Enchantment.values()) {
                String nameEnch = enchantment.getKey().getKey().replaceAll("_", " ").toLowerCase();
                if (encantamento.equalsIgnoreCase(nameEnch)) {
                    key = enchantment.getKey();
                }
            }
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            meta.addStoredEnchant(Enchantment.getByKey(key), 1, true);
            itemStack.setItemMeta(meta);
        }
        return Item.ParseItemStack(name);
    }

    public ItemStack parseItemStack(String name) {
        return new ItemStack(Material.valueOf(name.toUpperCase().replaceAll("\\s", "_")));
    }

    public static ItemStack ParseItemStack(String name) {
        return new ItemStack(Material.valueOf(name.toUpperCase().replaceAll("\\s", "_")));
    }

    public Item parse(){
        PotionMeta meta = null;
        String potionName = null;
        switch (itemStack.getType()){
            case POTION:
            case LINGERING_POTION:
            case SPLASH_POTION:
                if(itemStack.getItemMeta()!=null){
                    meta = (PotionMeta) itemStack.getItemMeta();
                    potionName = meta.getBasePotionData().getType().name();
                }
                Msg.ServidorGreen("potion meta >> "+ potionName, getClass());
                name = toItemStack(itemStack)+" ".concat(potionName.replaceAll("_"," ").toLowerCase());
                break;
            default:
                name = toItemStack(itemStack);
        }
        return this;
    }

    public static Item PARSE(ItemStack itemStackInventory) {
        return ITEM_MAP_NAME.get(itemStackInventory.getType().getKey().getKey().toLowerCase().replaceAll("_", " "));
    }

    public static String ToName(ItemStack itemStack) {
        return itemStack.getType().getKey().getKey().toLowerCase().replaceAll("_", " ");
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

        Pocao.Create();// POÇÕES E SEU EFEITOS
        LivroEncantado.Create();// LIVRO ENCANTADOS

        Collections.sort(VGlobal.ITEM_NAME_LIST);// Organizando em ordem alfabetica

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
        return "Item:{" +
                "codigo:" + codigo +
                ", name:'" + name + '\'' +
                ", bloqueado:" + bloqueado +
                '}';
    }

    public boolean equals(@NotNull Item item) {
        return (codigo == item.getCodigo());
    }
}