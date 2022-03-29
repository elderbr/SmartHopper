package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Linguagem;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Item implements Linguagem {

    private int cdItem = 0;
    private String dsItem;
    // LANG
    private int cdLang;
    private String dsLang;
    // TRADUCAO
    private int cdTraducao;
    private String dsTraducao;

    // LISTA DE TRADUÇÃO
    private Map<String, String> traducao = new HashMap<>();

    public Item() {
    }

    public Item(String name) {
        dsItem = name;
    }

    public Item(ItemStack itemStack) {
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
    public Item setCdLang(int codigo) {
        cdLang = codigo;
        return this;
    }

    @Override
    public int getCdLang() {
        return cdLang;
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

    @Override
    public Item setCdTraducao(int codigo) {
        cdTraducao = codigo;
        return this;
    }

    @Override
    public int getCdTraducao() {
        return cdTraducao;
    }

    @Override
    public Item setDsTraducao(String traducao) {
        dsTraducao = traducao;
        return this;
    }

    @Override
    public String getDsTraducao() {
        return dsTraducao;
    }

    public Map<String, String> getTraducao() {
        return traducao;
    }

    public void addTraducao(String lang, String traducao) {
        if(this.traducao == null) this.traducao = new HashMap<>();
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
            String name = potion.name().replaceAll("_", " ").toLowerCase();
            if (!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add("potion "+name);
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
            String name = "book "+enchantment.getKey().getKey().replaceAll("_", " ");
            if (!VGlobal.ITEM_NAME_LIST.contains(name)) {
                VGlobal.ITEM_NAME_LIST.add(name);
            }
        }
        Collections.sort(VGlobal.ITEM_NAME_LIST);
    }

    public Item parseItem(@NotNull ItemStack itemStack) {

        // ITEM DE POÇAO
        if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.LINGERING_POTION) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            String potion = potionMeta.getBasePotionData().getType().name().replaceAll("_", " ").toLowerCase();
            switch (itemStack.getType()) {
                case LINGERING_POTION:
                    dsItem = "lingering " + potion;
                    break;
                case SPLASH_POTION:
                    dsItem = "splash " + potion;
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
                    dsItem = "book "+keys.getKey().getKey().getKey().toLowerCase().replaceAll("_", " ");
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
        return new ItemStack(Material.getMaterial(item.getDsItem().replaceAll("\\s", "_").toUpperCase()));
    }

    public ItemStack getItemStack() {
        return new ItemStack(Material.getMaterial(dsItem.replaceAll("\\s", "_").toUpperCase()));
    }

}
