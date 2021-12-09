package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Pocao implements Dados {


    private String name;
    private String type;
    private List<String> listPotion = new ArrayList<>();

    private PotionEffectType effectType;
    private PotionEffect effect;
    private PotionMeta potionMeta;

    private ItemStack itemStack;

    public Pocao() {

    }

    public Pocao(ItemStack itemStack) {
        this.itemStack = itemStack;
        if(isPotion(itemStack)){
            potionMeta = (PotionMeta) itemStack.getItemMeta();
            name = potionMeta.getBasePotionData().getType().name().replaceAll("_"," ").toLowerCase();
            effectType = potionMeta.getBasePotionData().getType().getEffectType();
        }
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void setID(int id) {

    }

    @Override
    public String getName() {
        getPotion();
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void setLocation(Block block) {

    }

    @Override
    public void setLang(Map<String, String> lang) {

    }

    @Override
    public void setLang(MemorySection memorySection) {

    }

    @Override
    public Map<String, String> getLang() {
        return null;
    }

    @Override
    public String getTraducao(String lang) {
        return null;
    }

    @Override
    public void addTraducao(String lang, String traducao) {

    }


    /***
     *  Pega o nome da poção o seu tipo e seu efeito
     * @return Pocao
     */
    public Pocao getPotion(){
        if(isPotion()) {
            potionMeta = (PotionMeta) itemStack.getItemMeta();
            switch (itemStack.getType()) {
                case POTION:
                    type = "potion";
                    break;
                case SPLASH_POTION:
                    type = "splash";
                    break;
                case LINGERING_POTION:
                    type = "lingering";
                    break;
            }
            name = toName()+" "+ type;
            effectType = potionMeta.getBasePotionData().getType().getEffectType();
        }
        return this;
    }

    public PotionEffectType getEffectType() {
        return effectType;
    }

    public String toEffectType(){
        return effectType.getName().replaceAll("_"," ").toLowerCase();
    }

    public boolean isPotion(ItemStack itemStack){
        this.itemStack = itemStack;
        return isPotion();
    }

    public boolean isPotion(){
        return (itemStack.getType() == Material.POTION
                || itemStack.getType() == Material.LINGERING_POTION
                || itemStack.getType() == Material.SPLASH_POTION);

    }

    public List<String> getListPotion() {
        listPotion = new ArrayList<>();
        for (PotionType potions : PotionType.values()) {
            name = potions.name().replaceAll("_", " ").toLowerCase();
            effectType = potions.getEffectType();
            listPotion.add(name);
            listPotion.add(name+" potion");
            listPotion.add(name+" splash");
            listPotion.add(name+" lingering");
        }
        for (PotionEffectType potions : PotionEffectType.values()) {
            name = potions.getName().replaceAll("_", " ").toLowerCase();
            effectType = potions;
            listPotion.add(name);
            listPotion.add(name+" potion");
            listPotion.add(name+" splash");
            listPotion.add(name+" lingering");
        }
        Collections.sort(listPotion);
        return listPotion;
    }

    private String toName(){
        return potionMeta.getBasePotionData().getType().name().replaceAll("_", " ").toLowerCase();
    }

    public ItemStack parseItemStack(String items) {
        if(items.contains("potion")){
            itemStack = new ItemStack(Material.POTION);
        }else if(items.contains("lingering")){
            itemStack = new ItemStack(Material.LINGERING_POTION);
        }else{
            itemStack = new ItemStack(Material.SPLASH_POTION);
        }

        items = items.replaceAll(" potion","")
                .replaceAll(" splash","")
                .replaceAll(" lingering","")
                .replaceAll("\\s","_").toUpperCase();

        potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setBasePotionData(new PotionData( PotionType.valueOf(items)));
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }
}
