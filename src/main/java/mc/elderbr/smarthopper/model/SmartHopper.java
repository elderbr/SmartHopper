package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SmartHopper implements Dados {

    private int codigo = 0;
    private String name = "hopper";
    private boolean bloqueio = false;
    private Map<String, String> traducao = new HashMap<>();
    private Object type = null;
    private List<Object> list = null;

    private Hopper hopper = null;
    private String silaba;
    private int size = 0;
    private int max = 0;


    // ITEM
    private Item item;
    private Item itemInventory, itemDestino;

    private Grupo grupo;

    public SmartHopper(@NotNull Object hopper) {
        if (hopper instanceof Hopper funil) {
            this.hopper = funil;
        }
        if (hopper instanceof Inventory inventory) {
            if (inventory.getType() == InventoryType.HOPPER) {
                this.hopper = (Hopper) inventory.getLocation().getBlock().getState();
            }
        }
        if (hopper instanceof Block block) {
            if (block.getType() == Material.HOPPER) {
                this.hopper = (Hopper) block.getState();
            }
        }
        if (this.hopper != null) {

            name = (this.hopper.getCustomName() == null ? "hopper" : this.hopper.getCustomName().toLowerCase());

            if (name.equals("hopper")) return;

            try {
                if (name.contains(";")) {
                    list = new ArrayList<>();
                    for (String names : name.split(";")) {

                        if (names.contains("#")) {
                            silaba = names.substring(1, 2).toLowerCase();
                            codigo = Integer.parseInt(names.substring(2, names.length()));
                            bloqueio = true;
                        } else {
                            silaba = names.substring(0, 1).toLowerCase();
                            codigo = Integer.parseInt(names.substring(1, names.length()));
                            bloqueio = false;
                        }

                        if (silaba.equalsIgnoreCase("i")) {
                            item = VGlobal.ITEM_MAP_ID.get(codigo);
                            list.add(item);
                        }
                        if (silaba.equalsIgnoreCase("g")) {
                            grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                            list.add(grupo);
                        }
                    }
                    type = list;
                    return;
                }

                if (name.contains("#")) {
                    bloqueio = true;
                    silaba = name.substring(1, 2).toLowerCase();
                    codigo = Integer.parseInt(name.substring(2, name.length()));
                } else {
                    bloqueio = false;
                    silaba = name.substring(0, 1).toLowerCase();
                    codigo = Integer.parseInt(name.substring(1, name.length()));
                }

                if (silaba.equalsIgnoreCase("i")) {
                    item = VGlobal.ITEM_MAP_ID.get(codigo);
                    traducao = item.getTraducao();
                    type = item;
                    return;
                }

                if (silaba.equalsIgnoreCase("g")) {
                    grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                    traducao = grupo.getTraducao();
                    type = grupo;
                }
            } catch (NumberFormatException ex) {
            } catch (Exception e) {
                Msg.ServidorErro("Erro ao pegar o tipo de smarthopper", "getType()", getClass(), e);
            }
        }
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
        this.name = name;
        hopper.setCustomName(name);
        return this.name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isBloqueio() {
        return bloqueio;
    }

    @Override
    public Map<String, String> getTraducao() {
        return traducao;
    }

    public String getSilaba() {
        return silaba;
    }

    public Object getType() {
        return type;
    }

    public boolean cancelled(Inventory inventory, Inventory destino) {

        if (hopper == null || name.equals("hopper")) return false;

        if (type instanceof ArrayList listObj) {
            for (Object type : listObj) {
                if (type instanceof Item itemSmart) {
                    for (ItemStack itemStackInventory : inventory.getStorageContents()) {
                        if (itemStackInventory == null) continue;
                        itemInventory = Item.PARSE(itemStackInventory);
                        if (bloqueio) {
                            if (itemSmart.getCodigo() != itemInventory.getCodigo()) {
                                for (ItemStack itemStackDestino : destino.getStorageContents()) {
                                    if (itemStackDestino == null) {
                                        destino.addItem(itemStackInventory);
                                        inventory.removeItem(itemStackInventory);
                                        return true;
                                    }
                                    itemDestino = Item.PARSE(itemStackDestino);
                                    if (itemDestino.getSize() < itemDestino.getMax()) {
                                        destino.addItem(itemStackInventory);
                                        inventory.removeItem(itemStackInventory);
                                        return true;
                                    }
                                }
                            }
                        } else if (itemSmart.getCodigo() == itemInventory.getCodigo()) {
                            for (ItemStack itemStackDestino : destino.getStorageContents()) {
                                if (itemStackDestino == null) {
                                    destino.addItem(itemStackInventory);
                                    inventory.removeItem(itemStackInventory);
                                    return true;
                                }
                                itemDestino = Item.PARSE(itemStackDestino);
                                if (itemDestino.getSize() < itemDestino.getMax()) {
                                    destino.addItem(itemStackInventory);
                                    inventory.removeItem(itemStackInventory);
                                    return true;
                                }
                            }
                        }
                    }
                }// FIM DO ITEM
                if (type instanceof Grupo grupo) {
                    for (ItemStack itemStackInventory : inventory.getStorageContents()) {
                        if (itemStackInventory == null) continue;
                        itemInventory = Item.PARSE(itemStackInventory);
                        if (bloqueio) {
                            if (!grupo.isContains(itemInventory)) {
                                for (ItemStack itemStackDestino : destino.getStorageContents()) {
                                    if (itemStackDestino == null) {
                                        destino.addItem(itemStackInventory);
                                        inventory.removeItem(itemStackInventory);
                                        return true;
                                    }
                                    if (itemStackDestino.getAmount() < itemStackDestino.getMaxStackSize()) {
                                        destino.addItem(itemStackInventory);
                                        inventory.removeItem(itemStackInventory);
                                        return true;
                                    }
                                }
                            }
                        } else if (grupo.isContains(itemInventory)) {
                            for (ItemStack itemStackDestino : destino.getStorageContents()) {
                                if (itemStackDestino == null) {
                                    destino.addItem(itemStackInventory);
                                    inventory.removeItem(itemStackInventory);
                                    return true;
                                }
                                if (itemStackDestino.getAmount() < itemStackDestino.getMaxStackSize()) {
                                    destino.addItem(itemStackInventory);
                                    inventory.removeItem(itemStackInventory);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (type instanceof Item itemSmart) {
            for (ItemStack itemStackInventory : inventory.getStorageContents()) {
                if (itemStackInventory == null) continue;
                itemInventory = Item.PARSE(itemStackInventory);
                if (bloqueio) {
                    if (itemSmart.getCodigo() != itemInventory.getCodigo()) {
                        for (ItemStack itemStackDestino : destino.getStorageContents()) {
                            if (itemStackDestino == null) {
                                destino.addItem(itemStackInventory);
                                inventory.removeItem(itemStackInventory);
                                return true;
                            }
                            itemDestino = Item.PARSE(itemStackDestino);
                            if (itemSmart.getCodigo() == itemDestino.getCodigo() && itemStackDestino.getAmount() < itemStackDestino.getMaxStackSize()) {
                                destino.addItem(itemStackInventory);
                                inventory.removeItem(itemStackInventory);
                                return true;
                            }
                        }
                    }
                } else if (itemSmart.getCodigo() == itemInventory.getCodigo()) {
                    for (ItemStack itemStackDestino : destino.getStorageContents()) {
                        ItemStack itemMove = itemStackInventory.clone();
                        itemMove.setAmount(1);
                        if (itemStackDestino == null) {
                            destino.addItem(itemMove);
                            return true;
                        }
                        itemDestino = Item.PARSE(itemStackDestino);
                        if (itemSmart.getCodigo() == itemDestino.getCodigo() && itemDestino.getSize() < itemDestino.getMax()) {
                            destino.addItem(itemMove);
                            inventory.removeItem(itemMove);
                            return true;
                        }
                    }
                }
            }
        }// FIM DO ITEM

        if (type instanceof Grupo grupo) {
            for (ItemStack itemStackInventory : inventory.getStorageContents()) {
                if (itemStackInventory == null) continue;
                itemInventory = Item.PARSE(itemStackInventory);
                if (bloqueio) {
                    if (!grupo.isContains(itemInventory)) {
                        for (ItemStack itemStackDestino : destino.getStorageContents()) {
                            if (itemStackDestino == null) {
                                destino.addItem(itemStackInventory);
                                inventory.removeItem(itemStackInventory);
                                return true;
                            }
                            if (itemStackDestino.getAmount() < itemStackDestino.getMaxStackSize()) {
                                destino.addItem(itemStackInventory);
                                inventory.removeItem(itemStackInventory);
                                return true;
                            }
                        }
                    }
                } else if (grupo.isContains(itemInventory)) {
                    for (ItemStack itemStackDestino : destino.getStorageContents()) {
                        if (itemStackDestino == null) {
                            destino.addItem(itemStackInventory);
                            inventory.removeItem(itemStackInventory);
                            return true;
                        }
                        itemDestino = Item.PARSE(itemStackDestino);
                        if (itemInventory.getCodigo() == itemDestino.getCodigo() && itemStackDestino.getAmount() < itemStackDestino.getMaxStackSize()) {
                            destino.addItem(itemStackInventory.clone());
                            inventory.removeItem(itemStackInventory.clone());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCancelled(Item item) {
        if (type instanceof ArrayList listObj) {
            for (Object type : listObj) {
                if (type instanceof Item itemSmart) {
                    if (bloqueio) {
                        if(itemSmart.getCodigo() == item.getCodigo()) {
                            return true;
                        }else{
                            return false;
                        }
                    } else if (itemSmart.getCodigo() == item.getCodigo()) {
                        return false;
                    }
                }
                if (type instanceof Grupo grupo) {
                    if (bloqueio) {
                        if(grupo.isContains(item)) {
                            return true;
                        }else{
                            return false;
                        }
                    } else if (grupo.isContains(item)) {
                        return false;
                    }
                }
            }
        }

        if (type instanceof Item itemSmart) {
            if (bloqueio) {
                if(itemSmart.getCodigo() == item.getCodigo()) {
                    return true;
                }else{
                    return false;
                }
            } else if (itemSmart.getCodigo() == item.getCodigo()) {
                return false;
            }
        }
        if (type instanceof Grupo grupo) {
            if (bloqueio) {
                if(grupo.isContains(item)) {
                    return true;
                }else{
                    return false;
                }
            } else if (grupo.isContains(item)) {
                return false;
            }
        }
        return true;
    }


}