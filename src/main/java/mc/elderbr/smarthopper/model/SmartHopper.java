package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartHopper implements Dados {

    private int codigo;
    private String name;
    private boolean bloqueio = false;
    private Map<String, String> traducao = new HashMap<>();

    private Hopper hopper = null;
    private String silaba;


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
    public boolean setBloqueado(boolean value) {
        return bloqueio = value;
    }

    @Override
    public boolean isBloqueado() {
        return bloqueio;
    }

    @Override
    public Map<String, String> getTraducao() {
        return traducao;
    }

    public String getSilaba() {
        return silaba;
    }

    public boolean isCancelled(Item item) {
        Msg.ServidorGreen("nome smart >> "+ name, getClass());
        if(getType() instanceof ArrayList list){
            for(Object obj : list){
                // Se for item
                if(obj instanceof Item i){
                    if(i.isBloqueado()){
                        if(i.getCodigo() == item.getCodigo()) {
                            return true;
                        }
                    }
                    if(i.getCodigo() == item.getCodigo()){
                        return false;
                    }
                }
                // Se for grupo
                if(obj instanceof Grupo grupo){
                    if(grupo.isBloqueado()){
                        if(grupo.isContains(item)){
                            return true;
                        }
                    }
                    if(grupo.isContains(item)){
                        return false;
                    }
                }
            }
            return true;
        }
        if(getType() instanceof Item i){
            if(i.isBloqueado()){
                if(i.getCodigo()==item.getCodigo()) {
                    return true;
                }
                return false;
            }
            if(i.getCodigo() == item.getCodigo()) {
                return false;
            }
            return true;
        }
        if(getType() instanceof Grupo grupo){
            if(grupo.isBloqueado() && grupo.isContains(item)){
                return true;
            }
            if(grupo.isContains(item)){
                return false;
            }
            return true;
        }
        return false;
    }

    public Object getType() {
        List<Object> list = new ArrayList<>();
        try {
            if (name.contains(";")) {
                for (String names : name.split(";")) {

                    if(names.contains("#")) {
                        silaba = names.substring(1, 2).toLowerCase();
                        codigo = Integer.parseInt(names.substring(2, names.length()));
                        bloqueio = true;
                    }else{
                        silaba = names.substring(0, 1).toLowerCase();
                        codigo = Integer.parseInt(names.substring(1, names.length()));
                        bloqueio = false;
                    }

                    if (silaba.equalsIgnoreCase("i")) {
                        item = VGlobal.ITEM_MAP_ID.get(codigo);
                        item.setBloqueado(bloqueio);
                        list.add(item);
                    }
                    if (silaba.equalsIgnoreCase("g")) {
                        grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                        grupo.setBloqueado(bloqueio);
                        list.add(grupo);
                    }
                }
                return list;
            }

            if(name.contains("#")){
                bloqueio = true;
                silaba = name.substring(1, 2).toLowerCase();
                codigo = Integer.parseInt(name.substring(2, name.length()));
            }else{
                bloqueio = false;
                silaba = name.substring(0, 1).toLowerCase();
                codigo = Integer.parseInt(name.substring(1, name.length()));
            }

            if (silaba.equalsIgnoreCase("i")) {
                item = VGlobal.ITEM_MAP_ID.get(codigo);
                item.setBloqueado(bloqueio);
                return item;
            }

            if (silaba.equalsIgnoreCase("g")) {
                grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
                grupo.setBloqueado(bloqueio);
                return grupo;
            }
        } catch (Exception e) {
        }
        return null;
    }
}