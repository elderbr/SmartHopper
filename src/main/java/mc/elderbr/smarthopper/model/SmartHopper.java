package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class SmartHopper {

    private Hopper hopper;
    private String nameHopper;

    // ITEM
    private int idItem;
    private String nameItem;
    private Item item;


    // GRUPO
    private int idGrupo;
    private String nameGrupo;
    private Grupo grupo;

    public SmartHopper(Hopper hopper) {
        this.hopper = hopper;
    }

    public SmartHopper(Inventory inventory) {
        if (inventory.getType() == InventoryType.HOPPER)
            hopper = (Hopper) inventory.getLocation().getBlock().getState();
        else
            nameHopper = inventory.getType().name();
    }

    public SmartHopper(Block block) {
        if (block.getState().getType() == Material.HOPPER) {
            hopper = (Hopper) block.getState();
        } else {
            switch (block.getState().getType()) {
                case BARREL:
                    nameHopper = ((Barrel) block.getState()).getType().name();
                    break;
                case FURNACE:
                    nameHopper = ((Furnace) block.getState()).getType().name();
                    break;
                case DISPENSER:
                    nameHopper = ((Dispenser) block.getState()).getType().name();
                    break;
                case DROPPER:
                    nameHopper = ((Dropper) block.getState()).getType().name();
                    break;
                case SHULKER_BOX:
                    nameHopper = ((ShulkerBox) block.getState()).getType().name();
                    break;
                default:
                    nameHopper = "vazio";
            }
        }
    }

    public String getName() {
        if (hopper != null) {
            if (hopper.getCustomName() != null) {
                nameHopper = hopper.getCustomName();
            } else {
                nameHopper = hopper.getType().name();
            }
        }
        return nameHopper.toLowerCase();
    }

    public Object getType() {
        String name = getName();
        String letra = name.substring(0, 1).toLowerCase();
        int code = 0;

        if (letra.equalsIgnoreCase("g")) {
            Grupo g = null;
            try {
                code = Integer.parseInt(name.substring(1, name.length()));
                g = VGlobal.GRUPO_ID_MAP.get(code);
            } catch (NumberFormatException e) {
                g = VGlobal.GRUPO_NAME_MAP.get(name.substring(1, name.length()));
            }
            if (g != null) {
                return g;
            }
        }
        if (letra.equalsIgnoreCase("i")) {
            Item item = null;
            try {
                code = Integer.parseInt(name.substring(1, name.length()));
                item = VGlobal.ITEM_ID_MAP.get(code);
            }catch (NumberFormatException e) {
                item = VGlobal.ITEM_NAME_MAP.get(name.substring(1, name.length()));
            }
            if (item != null) {
                return item;
            }
        }
        if(VGlobal.ITEM_NAME_MAP.get(name)!=null){
            return VGlobal.ITEM_NAME_MAP.get(name);
        }
        if(VGlobal.GRUPO_NAME_MAP.get(name)!=null) {
            return VGlobal.ITEM_NAME_MAP.get(name);
        }
        return null;
    }

}
