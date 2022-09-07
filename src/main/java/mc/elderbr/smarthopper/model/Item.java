package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.LivroEncantado;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
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

public class Item implements Dados, LivroEncantado {

    private int codigo = 0;
    private String name;
    private Map<String, String> traducao = new HashMap<>();

    private ItemStack itemStack;
    private int size = 0;
    private int max = 1;


    public Item() {
    }

    public Item(String name) {
        this.name = name;
        itemStack = new ItemStack(Utils.ParseItemStack(name));
    }

    public Item(ItemStack itemStack) {
        size = itemStack.getAmount();
        max = itemStack.getMaxStackSize();
        parseItem(itemStack);
    }

    @Override
    public int setCodigo(int codigo) {
        return this.codigo = codigo;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public String setName(String name) {
        return this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getTraducao() {
        return this.traducao;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSize(ItemStack itemStack) {
        this.size = itemStack.getAmount();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMax(ItemStack itemStack) {
        this.max = itemStack.getMaxStackSize();
    }

    public static void CreateItem() {
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType().isItem()) {
                Item item = new Item().parseItem(itemStack);
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

    public Item parseItem(@NotNull ItemStack itemStack) {

        // ITEM DE POÇAO
        if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.LINGERING_POTION) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            String potion = potionMeta.getBasePotionData().getType().name().replaceAll("_", " ").toLowerCase();
            switch (itemStack.getType()) {
                case LINGERING_POTION:
                    name = "lingering potion " + potion;
                    break;
                case SPLASH_POTION:
                    name = "splash potion " + potion;
                    break;
                case POTION:
                    name = "potion " + potion;
                    break;
            }
            return this;
        }
        // LIVROS ENCANTADOS PEGA A PRIMEIRO ENCANTAMENTO DA LISTA DE ENCANTAMENTOS
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            if (meta.getStoredEnchants().size() > 0) {
                for (Map.Entry<Enchantment, Integer> keys : meta.getStoredEnchants().entrySet()) {
                    name = "enchanted book " + keys.getKey().getKey().getKey().toLowerCase().replaceAll("_", " ");
                    VGlobal.BOOK_ENCHANTMENTE_LIST.add(name);// ADICIONANDO LIVRO DE ENCANTAMENTO NA VARIAVEL GLOBAL
                    VGlobal.BOOK_ENCHANTEMENT_MAP.put(name, keys.getKey());
                    break;
                }
                return this;
            }
        }
        name = itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();
        return this;
    }

    public ItemStack parseItemStack() {
        try {

            if (name.contains("splash")) {
                String potion = name.replaceAll("splash potion ", "").replaceAll(" ", "_").toUpperCase();
                itemStack = new ItemStack(Material.SPLASH_POTION);
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(potion)));
                itemStack.setItemMeta(meta);
                return itemStack;
            }

            if (name.contains("lingering")) {
                String potion = name.replaceAll("lingering potion ", "").replaceAll(" ", "_").toUpperCase();
                itemStack = new ItemStack(Material.LINGERING_POTION);
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(potion)));
                itemStack.setItemMeta(meta);
                return itemStack;
            }

            if (name.contains("potion")) {
                String potion = name.replaceAll("potion ", "").replaceAll(" ", "_").toUpperCase();
                itemStack = new ItemStack(Material.POTION);
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(potion)));
                itemStack.setItemMeta(meta);
                return itemStack;
            }

            if (name.contains("enchanted book")) {
                itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                if (getEncantamento() != null) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                    meta.addStoredEnchant(getEncantamento(), 1, true);
                    itemStack.setItemMeta(meta);
                }
                return itemStack;
            }
            return new ItemStack(Material.getMaterial(name.replaceAll("\\s", "_").toUpperCase()));
        } catch (Exception e) {
            return null;
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isMax() {
        return false;
    }

    public static Item PARSE(ItemStack itemStack) {
        String name = itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();
        // ITEM DE POÇAO
        if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.LINGERING_POTION) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            String potion = potionMeta.getBasePotionData().getType().name().replaceAll("_", " ").toLowerCase();
            switch (itemStack.getType()) {
                case LINGERING_POTION:
                    name = "lingering potion " + potion;
                    break;
                case SPLASH_POTION:
                    name = "splash potion " + potion;
                    break;
                case POTION:
                    name = "potion " + potion;
                    break;
            }
        }
        // LIVROS ENCANTADOS PEGA A PRIMEIRO ENCANTAMENTO DA LISTA DE ENCANTAMENTOS
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            if (meta.getStoredEnchants().size() > 0) {
                for (Map.Entry<Enchantment, Integer> keys : meta.getStoredEnchants().entrySet()) {
                    name = "enchanted book " + keys.getKey().getKey().getKey().toLowerCase().replaceAll("_", " ");
                    VGlobal.BOOK_ENCHANTMENTE_LIST.add(name);// ADICIONANDO LIVRO DE ENCANTAMENTO NA VARIAVEL GLOBAL
                    VGlobal.BOOK_ENCHANTEMENT_MAP.put(name, keys.getKey());
                    break;
                }
            }
        }
        Item item = VGlobal.ITEM_MAP_NAME.get(name);
        item.setSize(itemStack.getAmount());
        item.setMax(itemStack.getMaxStackSize());
        return item;
    }
}