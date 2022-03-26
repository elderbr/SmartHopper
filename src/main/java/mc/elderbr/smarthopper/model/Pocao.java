package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pocao {

    private ItemStack itemStack;
    private String name;
    private String type;

    private List<Pocao> listPocao = new ArrayList<>();
    private List<String> listPotion = new ArrayList<>();

    private PotionMeta meta;
    private PotionEffectType effectType;

    public Pocao(ItemStack itemStack) {
        if (isPotion(itemStack)) {
            this.itemStack = itemStack;
            name = getPotion(itemStack);
        }
    }


    public Pocao() {
        // Percorrendo todas os efeitos e poções
        listPotion = new ArrayList<>();
        for (PotionType potion : PotionType.values()) {
            name = potion.name().replaceAll("_", " ").toLowerCase();
            listPotion.add(name);// Lista dos itens
            listPotion.add("splash " + name);// Lista dos itens
            listPotion.add("lingering " + name);// Lista dos itens
        }
        Collections.sort(listPotion);// ORGANIZANDO EM ORDEM ALFABETICA
    }

    public String getPotion(ItemStack itemStack) {
        if (Utils.ToItemStack(itemStack).contains("potion")) {
            meta = (PotionMeta) itemStack.getItemMeta();
            type = toPotion(meta);
            switch (itemStack.getType()) {
                case POTION:
                    return type;
                case LINGERING_POTION:
                    return "lingering " + type;
                case SPLASH_POTION:
                    return "splash " + type;
            }
        }
        return null;
    }


    public String getDsGrupo() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item getItem() {
        Item item = new Item();
        item.setName(name);
        return item;
    }

    public ItemStack getItemStack() {
        itemStack = new ItemStack(Material.POTION);
        meta = (PotionMeta) itemStack.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(name), 3,3), true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public List<String> getListPotion(){
        return listPotion;
    }

    public List<Pocao> getListPocao() {
        // Adicionando na lista de poções
        listPocao = new ArrayList<>();
        for (String potion : listPotion) {
            Pocao pocao = new Pocao();
            pocao.setName(potion);
            listPocao.add(pocao);
        }
        return listPocao;
    }

    public void setListPocao(List<Pocao> listPocao) {
        this.listPocao = listPocao;
    }

    public String toPotion(PotionMeta potionMeta) {
        return potionMeta.getBasePotionData().getType().name().toLowerCase().replaceAll("_", " ");
    }

    public Item toItem() {
        Item item = new Item();
        item.setName(name);
        return item;
    }

    public Item toItem(ItemStack itemStack) {
        Item item = new Item(itemStack);
        item.setName(name);
        return item;
    }

    public boolean isPotion(ItemStack itemStack) {
        if (Utils.ToItemStack(itemStack).contains("potion")) {
            return true;
        }
        return false;
    }

    public boolean isPotion() {
        if (itemStack != null) {
            return isPotion(itemStack);
        }
        return false;
    }

    public PotionEffect parseEffect(String potion){
        PotionEffect effect = new PotionEffect(PotionEffectType.getByName(potion), 3,3);
        itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(effect, true);
        itemStack.setItemMeta(potionMeta);
        return effect;
    }
    public ItemStack parseItemStack(String potion){
        String name = potion.replace("splash","")
                .replace("lingering","")
                .replaceAll("\\s","").toUpperCase();
        try {
            PotionEffect effect = new PotionEffect(PotionEffectType.getByName(name), 3, 3);
            itemStack = new ItemStack(Material.POTION);
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            potionMeta.addCustomEffect(effect, true);
            itemStack.setItemMeta(potionMeta);
        }catch (Exception e){
            return new ItemStack(Material.AIR);
        }
        return itemStack;
    }

}
