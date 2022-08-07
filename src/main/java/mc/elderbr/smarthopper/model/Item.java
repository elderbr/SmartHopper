package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Linguagem;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Item implements Linguagem {

    private int cdItem = 0;
    private String dsItem;
    private ItemStack itemStack;
    private int size = 0;
    private int max = 1;

    // TRADUCAO
    private String dsLang;
    private String dsTraducao;

    // LISTA DE TRADUÇÃO
    private Map<String, String> traducao = new HashMap<>();

    public Item() {
    }

    public Item(String name) {
        dsItem = name;
    }

    public Item(ItemStack itemStack) {
        size = itemStack.getAmount();
        max = itemStack.getMaxStackSize();
        parseItem(itemStack);
    }


    public int getCdItem() {
        return cdItem;
    }

    public void setCdItem(int cdItem) {
        this.cdItem = cdItem;
    }

    public String getDsItem() {
        return dsItem;
    }

    public void setDsItem(String dsItem) {
        this.dsItem = dsItem;
    }

    @Override
    public Item setDsLang(String lang) {
        dsLang = lang;
        return this;
    }

    @Override
    public Item setDsLang(Player player) {
        dsLang = player.getLocale();
        return this;
    }

    @Override
    public String getDsLang() {
        return dsLang;
    }

    public Item setSize(int size){
        this.size = size;
        return this;
    }

    public Item setSize(ItemStack itemStack){
        this.size = itemStack.getAmount();
        return this;
    }

    public int getSize(){
        return size;
    }

    public Item setMax(int max){
        this.max = max;
        return this;
    }

    public Item setMax(ItemStack itemStack){
        this.max = itemStack.getMaxStackSize();
        return this;
    }

    public int getMax(){
        return max;
    }

    public boolean isMax(){
        if(size < max){
            return true;
        }
        return false;
    }

    public Item setDsTraducao(String traducao) {
        dsTraducao = traducao;
        return this;
    }

    public String getDsTraducao() {
        return dsTraducao;
    }

    public Map<String, String> getTraducao() {
        return traducao;
    }

    public void addTraducao(String lang, String traducao) {
        this.traducao.put(lang, traducao);
    }

    public String toTraducao(){
        dsTraducao = traducao.get(dsLang);
        return ( dsTraducao == null ? dsItem : dsTraducao );
    }

    @Override
    public String toString() {
        return dsItem;
    }

    public static void CreateItem() {
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType().isItem()) {
                Item item = new Item().parseItem(itemStack);
                if (!VGlobal.ITEM_NAME_LIST.contains(item.getDsItem())) {
                    VGlobal.ITEM_NAME_LIST.add(item.getDsItem());
                }
            }
        }
        // POÇÕES E SEU EFEITOS
        for (PotionType potion : PotionType.values()) {
            String name = "potion "+potion.name().replaceAll("_", " ").toLowerCase();
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
            String name = "enchanted book "+enchantment.getKey().getKey().replaceAll("_", " ");
            if (!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add(name);
            }
        }
        Collections.sort(VGlobal.ITEM_NAME_LIST);

        // Criando itens com código e nome e adicionando na lista global
        int cod = 1;
        for(String name : VGlobal.ITEM_NAME_LIST){
            Item item = new Item();
            item.setCdItem(cod);
            item.setDsItem(name);
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
                    dsItem = "lingering potion " + potion;
                    break;
                case SPLASH_POTION:
                    dsItem = "splash potion " + potion;
                    break;
                case POTION:
                    dsItem = "potion "+potion;
                    break;
            }
            return this;
        }
        // LIVROS ENCANTADOS PEGA A PRIMEIRO ENCANTAMENTO DA LISTA DE ENCANTAMENTOS
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            if(meta.getStoredEnchants().size()>0) {
                for (Map.Entry<Enchantment, Integer> keys : meta.getStoredEnchants().entrySet()) {
                    dsItem = "enchanted book "+keys.getKey().getKey().getKey().toLowerCase().replaceAll("_", " ");
                    VGlobal.BOOK_ENCHANTMENTE_LIST.add(dsItem);// ADICIONANDO LIVRO DE ENCANTAMENTO NA VARIAVEL GLOBAL
                    VGlobal.BOOK_ENCHANTEMENT_MAP.put(dsItem, keys.getKey());
                    break;
                }
                return this;
            }
        }
        dsItem = itemStack.getType().getKey().getKey().replaceAll("_", " ").toLowerCase();
        return this;
    }

    public Item parseItem(@NotNull Material material) {
        dsItem = material.getKey().getKey().replaceAll("_", " ").toLowerCase();
        return this;
    }

    public ItemStack parseItemStack(@NotNull Item item) {
        try {

            if (item.dsItem.contains("splash")) {
                String name = item.getDsItem().replaceAll("splash potion ", "").replaceAll(" ", "_").toUpperCase();
                itemStack = new ItemStack(Material.SPLASH_POTION);
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(name)));
                itemStack.setItemMeta(meta);
                return itemStack;
            }

            if (item.dsItem.contains("lingering")) {
                String name = item.getDsItem().replaceAll("lingering potion ", "").replaceAll(" ", "_").toUpperCase();
                itemStack = new ItemStack(Material.LINGERING_POTION);
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(name)));
                itemStack.setItemMeta(meta);
                return itemStack;
            }

            if (item.dsItem.contains("potion")) {
                String name = item.getDsItem().replaceAll("potion ", "").replaceAll(" ", "_").toUpperCase();
                itemStack = new ItemStack(Material.POTION);
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(name)));
                itemStack.setItemMeta(meta);
                return itemStack;
            }

            if(item.getDsItem().contains("enchanted book")){
                itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                BookMeta meta = (BookMeta) itemStack.getItemMeta();
                meta.addEnchant(VGlobal.BOOK_ENCHANTEMENT_MAP.get(item.getDsItem()), 0, true);
                itemStack.setItemMeta(meta);
                return itemStack;
            }
            return new ItemStack(Material.getMaterial(item.getDsItem().replaceAll("\\s", "_").toUpperCase()));
        }catch (Exception e){
            return null;
        }
    }

    public ItemStack getItemStack() {
        return parseItemStack(this);
    }

}