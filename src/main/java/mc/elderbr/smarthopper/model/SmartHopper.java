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

import java.util.HashMap;
import java.util.Map;

public class SmartHopper implements Dados {

    private int codigo;
    private String name;
    private Map<String, String> traducao = new HashMap<>();

    private Hopper hopper = null;
    private String silaba;
    private boolean bloqueio = false;

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
            silaba = name.substring(0, 1);
            // Verifica se o item está bloqueado
            if (silaba.equals("#")) {
                silaba = name.substring(1, 2);
                bloqueio = true;
            }
            try {
                if (bloqueio)
                    codigo = Integer.parseInt(name.substring(2, name.length()));
                else
                    codigo = Integer.parseInt(name.substring(1, name.length()));
            } catch (NumberFormatException e) {
                codigo = 0;
            }
        } else {
            name = "hopper";
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

    @Override
    public Map<String, String> getTraducao() {
        return traducao;
    }

    public boolean isMove(Item item) {
        try {
            if (name.contains(";")) {
                for (String names : name.split(";")) {
                    silaba = names.substring(0, 1).toLowerCase();
                    codigo = Integer.parseInt(names.substring(1, names.length()));

                    if (silaba.equalsIgnoreCase("i")) {
                        Msg.ServidorGreen("Item >> " + names, getClass());
                        itemSH = VGlobal.ITEM_MAP_ID.get(codigo);
                        if (itemSH != null && item.getCodigo() == itemSH.getCodigo()) {
                            return true;
                        }
                    }
                    if (silaba.equalsIgnoreCase("g")) {
                        grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                        if (grupo != null && grupo.isContains(item)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            if(bloqueio){
                if(silaba.equalsIgnoreCase("i")){
                    if(codigo == item.getCodigo()){
                        return false;
                    }
                }
                if(silaba.equalsIgnoreCase("g")){
                    grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                    if(grupo.isContains(item)){
                        return false;
                    }
                }
            }

            if (silaba.equalsIgnoreCase("i")) {
                if (item.getCodigo() == codigo) {
                    return true;
                }
            }
            if (silaba.equalsIgnoreCase("g")) {
                grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                if (grupo != null && grupo.isContains(item)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public Object getType() {
        if (silaba.equalsIgnoreCase("i")) {
            return VGlobal.ITEM_MAP_ID.get(codigo).getClass();
        }
        if (silaba.equalsIgnoreCase("g")) {
            return VGlobal.GRUPO_MAP_ID.get(codigo).getClass();
        }
        return null;
    }

    public boolean isBloqueio(){
        return bloqueio;
    }
}