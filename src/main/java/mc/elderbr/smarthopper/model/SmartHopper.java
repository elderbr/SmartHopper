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
    private Item itemSH;
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

    public boolean isBloqueio(){
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

    public boolean isTransf(Inventory inventory) {

        List<ItemStack> listItem = new ArrayList<>();

        if (type instanceof ArrayList lista) {

            for (ItemStack itemStack : inventory.getStorageContents()) {
                if (itemStack == null) continue;
                Item itemInventory = Item.PARSE(itemStack);
                boolean transf = false;
                for (Object obj : lista) {
                    if (obj instanceof Item itemHopper) {
                        if (bloqueio) {
                            transf = true;
                            if (itemHopper.getCodigo() == itemInventory.getCodigo()) {
                                transf = false;
                                break;
                            }
                        }else{
                            if (itemHopper.getCodigo() == itemInventory.getCodigo()) {
                                transf = true;
                            }
                        }
                    }
                    if(obj instanceof Grupo grupo){
                        if(bloqueio){
                            transf = true;
                            if(grupo.isContains(itemInventory)){
                                transf = false;
                                break;
                            }
                        }else{
                            if(grupo.isContains(itemInventory)){
                                transf = true;
                            }
                        }
                    }
                }
                if(transf){
                    hopper.getInventory().addItem(itemStack);
                    inventory.removeItem(itemStack);
                }
            }
        }

        if (type instanceof Item itemSmartHopper) {

            for (ItemStack itemStackInventory : inventory.getStorageContents()) {

                if (itemStackInventory == null) continue;
                Item itemInventory = Item.PARSE(itemStackInventory);

                if (bloqueio) {
                    if (itemSmartHopper.getCodigo() != itemInventory.getCodigo()) {
                        hopper.getInventory().addItem(itemStackInventory);
                        inventory.removeItem(itemStackInventory);
                    }
                } else {
                    if (itemSmartHopper.getCodigo() == itemInventory.getCodigo()) {
                        hopper.getInventory().addItem(itemStackInventory);
                        inventory.removeItem(itemStackInventory);
                    }
                }
            }
        }
        if (type instanceof Grupo grupo) {
            for (ItemStack itemStackInventory : inventory.getStorageContents()) {
                if (itemStackInventory == null) continue;
                Item itemInventory = Item.PARSE(itemStackInventory);

                if (bloqueio) {
                    if (!grupo.isContains(itemInventory)) {
                        hopper.getInventory().addItem(itemStackInventory);
                        inventory.removeItem(itemStackInventory);
                    }
                } else {
                    if (grupo.isContains(itemInventory)) {
                        hopper.getInventory().addItem(itemStackInventory);
                        inventory.removeItem(itemStackInventory);
                    }
                }
            }
        }
        return false;
    }


}