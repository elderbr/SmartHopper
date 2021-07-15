package mc.elderbr.smarthopper.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Inventario implements Inventory{

    private Inventory inventory;
    private Hopper hopper;
    private Chest chest;

    public Inventario(Inventory inventory){
        this.inventory = inventory;
    }

    public String getName(){
        if(inventory instanceof Hopper){
            hopper = (Hopper) inventory;
            return (hopper.getCustomName() != null ? hopper.getCustomName() : "HOPPER");
        }
        if(inventory instanceof Chest){
            chest = (Chest) inventory;
            return (chest.getCustomName() != null ? chest.getCustomName() : "CHEST");
        }
        return inventory.toString();
    }


    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    public int getMaxStackSize(Item item){
        return getItem(firtEmpty(item)).getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int size) {

    }

    @Nullable
    @Override
    public ItemStack getItem(int index) {
        return inventory.getItem(index);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item) {

    }

    public int getAmount(Item item){
        return getItem(firtEmpty(item)).getAmount();
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        return null;
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        return null;
    }

    @NotNull
    @Override
    public ItemStack[] getContents() {
        return inventory.getContents();
    }

    @Override
    public void setContents(@NotNull ItemStack[] items) throws IllegalArgumentException {

    }

    @NotNull
    @Override
    public ItemStack[] getStorageContents() {
        return inventory.getContents();
    }

    @Override
    public void setStorageContents(@NotNull ItemStack[] items) throws IllegalArgumentException {

    }

    @Override
    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        return inventory.contains(material);
    }

    @Override
    public boolean contains(@Nullable ItemStack item) {
        return inventory.contains(item);
    }

    @Override
    public boolean contains(@NotNull Material material, int amount) throws IllegalArgumentException {
        return inventory.contains(material, amount);
    }

    @Override
    public boolean contains(@Nullable ItemStack item, int amount) {
        return inventory.contains(item, amount);
    }

    @Override
    public boolean containsAtLeast(@Nullable ItemStack item, int amount) {
        return false;
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        return null;
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
        return null;
    }

    @Override
    public int first(@NotNull Material material) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public int first(@NotNull ItemStack item) {
        int position = 0;
        for(ItemStack items : inventory.getContents()){
            if(items.getType() != Material.AIR){
                if(items.equals(item)){
                    return position;
                }
            }
            position++;
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        int position = 0;
        for(ItemStack items : inventory.getContents()){
            if(items.getType() == Material.AIR){
                return position;
            }
            position++;
        }
        return -1;
    }
    public int firtEmpty(Item item){
        int position = 0;
        for(ItemStack items : hopper.getInventory().getContents()){
            if(items.getType() != Material.AIR){
                if(items.equals(item)){
                    return position;
                }
            }
            position++;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void remove(@NotNull Material material) throws IllegalArgumentException {
inventory.remove(material);
    }

    @Override
    public void remove(@NotNull ItemStack item) {
        inventory.remove(item);
    }

    @Override
    public void clear(int index) {

    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public List<HumanEntity> getViewers() {
        return null;
    }

    @NotNull
    @Override
    public InventoryType getType() {
        return inventory.getType();
    }

    @Nullable
    @Override
    public InventoryHolder getHolder() {
        return inventory.getHolder();
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator() {
        return null;
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator(int index) {
        return null;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return null;
    }
}
