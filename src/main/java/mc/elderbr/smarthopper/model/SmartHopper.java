package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartHopper implements Dados {

    private int codigo = 0;
    private String name = "hopper";
    private boolean bloqueio = false;
    private boolean trava = true;
    private Map<String, String> traducao = new HashMap<>();
    private Object type = null;
    private List<Object> listType = null;

    private Hopper myHopper = null;
    private String silaba;
    private int size = 0;
    private int max = 0;


    // ITEM
    private Item item;
    private Item itemInventory, itemDestino;

    private Grupo grupo;

    private Block block;

    public SmartHopper(@NotNull Object hopper) throws Exception {
        if (hopper instanceof Hopper funil) {
            myHopper = funil;
        }
        if (hopper instanceof Inventory inventory) {
            if (inventory.getType() == InventoryType.HOPPER) {
                myHopper = (Hopper) inventory.getLocation().getBlock().getState();
            }
        }
        if (hopper instanceof Block block) {
            if (block.getType() == Material.HOPPER) {
                myHopper = (Hopper) block.getState();
            }
        }
        if (myHopper == null) {// Se não existir o hopper
            return;
        }

        // Verifica se existe nome personalizado para o hopper
        if (myHopper.getCustomName() == null) {
            return;
        }

        // Pegando o bloco hopper
        block = myHopper.getBlock();

        // Pegando o nome do hopper
        name = myHopper.getCustomName().toLowerCase();

        if (name.contains(";")) {
            listType = new ArrayList<>();
            for (String names : name.split(";")) {

                if (names.substring(0, 2).contains("i")) {// Se o nome do hopper conter a letra i se trata de item
                    // Pegando o código do item
                    try {
                        codigo = Integer.parseInt(names.replaceAll("[#i]", ""));
                    } catch (NumberFormatException e) {
                        throw new GrupoException("O funil configurado com o nome " + names + " não é valido!");
                    }
                    // Pegando o item na memória
                    item = VGlobal.ITEM_MAP_ID.get(codigo);
                    if (item == null) {
                        throw new ItemException("O funil configurado como " + names + " não está na lista de itens!", block);
                    }
                    traducao = item.getTraducao();
                    listType.add(item);
                }

                // Se o nome do hopper conter a letra G se trata de grupo
                if (names.substring(0, 2).contains("g")) {
                    // Pegando o código do grupo
                    try {
                        codigo = Integer.parseInt(names.replaceAll("[#g]", ""));
                    } catch (NumberFormatException e) {
                        throw new GrupoException("O funil configurado com o nome " + names + " não é valido!", block);
                    }
                    // Buscando o grupo em memória
                    grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                    if (grupo == null) {
                        throw new GrupoException("O funil configurado com o nome " + names + " não está na lista de grupos!", block);
                    }
                    traducao = grupo.getTraducao();
                    listType.add(grupo);
                }
            }
            type = listType;
            return;
        }

        // Se o nome do hopper conter a letra i se trata de item
        if (name.substring(0, 2).contains("i")) {
            // Pegando o código do item
            try {
                codigo = Integer.parseInt(name.replaceAll("[#i]", ""));
            } catch (NumberFormatException e) {
                throw new ItemException("O funil configurado com o nome " + name + " não é valido!", block);
            }
            // Pegando o item na memória
            item = VGlobal.ITEM_MAP_ID.get(codigo);
            if (item == null) {
                throw new ItemException("O funil configurado como " + name + " não está na lista de itens!", block);
            }
            traducao = item.getTraducao();
            type = item;
            return;
        }

        // Se o nome do hopper conter a letra G se trata de grupo
        if (name.substring(0, 2).contains("g")) {
            // Pegando o código do grupo
            try {
                codigo = Integer.parseInt(name.replaceAll("[#g]", ""));
            } catch (NumberFormatException e) {
                throw new GrupoException("O funil configurado com o nome " + name + " não é valido!", block);
            }
            // Buscando o grupo em memória
            grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
            if (grupo == null) {
                throw new GrupoException("O funil configurado com o nome " + name + " não está na lista de grupos!", block);
            }
            traducao = grupo.getTraducao();
            type = grupo;
        }
        // Se conter # o item deve ser bloqueado
        if (name.contains("#")) {
            bloqueio = true;
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
    public SmartHopper setName(String name) {
        this.name = name;
        myHopper.setCustomName(name);
        return this;
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

        if (myHopper == null || name.equals("hopper")) return false;

        if (type instanceof ArrayList listTypeObj) {
            for (Object type : listTypeObj) {
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
        if (type instanceof ArrayList listTypeObj) {
            trava = true;
            for (Object type : listTypeObj) {
                if (type instanceof Item itemSmart) {
                    if (bloqueio) {
                        if (itemSmart.getCodigo() == item.getCodigo()) {
                            trava = true;
                            break;
                        } else {
                            trava = false;
                        }
                    } else if (itemSmart.getCodigo() == item.getCodigo()) {
                        trava = false;
                    }
                }
                if (type instanceof Grupo grupo) {
                    if (bloqueio) {
                        if (grupo.isContains(item)) {
                            trava = true;
                            break;
                        } else {
                            trava = false;
                        }
                    } else if (grupo.isContains(item)) {
                        trava = false;
                    }
                }
            }
            return trava;

        }

        if (type instanceof Item itemSmart) {
            if (bloqueio) {
                if (itemSmart.getCodigo() == item.getCodigo()) {
                    return true;
                } else {
                    return false;
                }
            }
            if (itemSmart.getCodigo() == item.getCodigo()) {
                return false;
            }
        }
        if (type instanceof Grupo grupo) {
            if (bloqueio) {
                if (grupo.isContains(item)) {
                    return true;
                } else {
                    return false;
                }
            } else if (grupo.isContains(item)) {
                return false;
            }
        }
        return true;
    }


}