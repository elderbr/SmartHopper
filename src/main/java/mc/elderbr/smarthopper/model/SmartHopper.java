package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Material;
<<<<<<< HEAD
import org.bukkit.block.*;
=======
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
>>>>>>> v4.0.0
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class SmartHopper {

<<<<<<< HEAD
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
=======
    private Hopper hopper = null;
    private int code;
    private String name;
    private String nameHopper;
    private String silaba;
>>>>>>> v4.0.0

    public SmartHopper(Hopper hopper) {
        this.hopper = hopper;
    }

    public SmartHopper(Inventory inventory) {
<<<<<<< HEAD
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
=======
        if (inventory.getType() == InventoryType.HOPPER) {
            this.hopper = (Hopper) inventory.getLocation().getBlock().getState();
        }
    }

    public SmartHopper(Block block) {
        if (block.getType() == Material.HOPPER) {
            this.hopper = (Hopper) block.getState();
        }
    }

    public SmartHopper(Block block, String name) {
        hopper = (Hopper) block.getState();
        hopper.setCustomName(name);
    }

    public String getNameHopper() {
        nameHopper = (hopper.getCustomName() != null ? hopper.getCustomName().toLowerCase().replaceAll("_", " ").trim() : "HOPPER");
        return nameHopper;
    }

    public String toSmartHopper() {
        if (getType() instanceof Item item) {
            return item.toTraducao();
        }
        if (getType() instanceof Grupo grupo) {
            return grupo.toTraducao();
        }
        return "Hopper";
    }

    public void msgPlayerSmartHopper(Player player) {
        getNameHopper();
        if (nameHopper.contains(";")) {
            String[] lista = nameHopper.split(";");
            Msg.PulaPlayer(player);
            for (String values : lista) {
                SmartHopper smart = new SmartHopper(hopper.getBlock(), values);
                if (smart.getType() instanceof Item items) {
                    if (values.contains("#")) {
                        Msg.ItemNegar(player, items);
                    } else {
                        Msg.Item(player, items);
                    }
                }
                if (smart.getType() instanceof Grupo grupo) {
                    if (values.contains("#")) {
                        Msg.GrupoNegar(player, grupo);
                    } else {
                        Msg.Grupo(player, grupo);
                    }
                }
            }
            Msg.PulaPlayer(player);
            return;
        } else {
            if (getType() instanceof Item item) {
                if (nameHopper.contains("#")) {
                    Msg.ItemNegar(player, item);
                } else {
                    Msg.Item(player, item);
                }
                return;
            }
            if (getType() instanceof Grupo grupo) {
                // CRIANDO O INVENTARIO DO GRUPO
                InventoryCustom inventory = new InventoryCustom();
                inventory.create(grupo.toTraducao().concat(" §e§lID:" + grupo.getCdGrupo()));// NO DO INVENTARIO
                for (Item items : grupo.getListItem()) {// ADICIONANDO OS ITENS NO INVENTARIO
                    inventory.addItem(items.getItemStack());
                }
                player.openInventory(inventory.getInventory());
                if(nameHopper.contains("#")){
                    Msg.GrupoNegar(player, grupo);
                }else {
                    Msg.Grupo(player, grupo);
                }
                return;
            }
            Msg.PlayerGold(player, Msg.Color("Funil $cNÃO $rfoi configurado!!!"));
        }
    }

    public Object getType() {
        getNameHopper();

        silaba = nameHopper.replaceAll("[#\\*]", "").substring(0, 1);
        name = nameHopper.replaceAll("[#\\*]", "");

        // VERIFICA SE É ITEM OU GRUPO
        if (silaba.equalsIgnoreCase("i") || silaba.equalsIgnoreCase("g")) {
            name = name.substring(1, name.length());
            try {
                code = Integer.parseInt(name);
            } catch (NumberFormatException e) {
            }
        }

        // RETORNA O GRUPO
        if (nameHopper.contains("*") || silaba.equalsIgnoreCase("g")) {
            if (code > 0) {
                return VGlobal.GRUPO_MAP_ID.get(code);
            }
            return VGlobal.GRUPO_MAP_NAME.get(name);
        }

        // RETORNA O ITEM
        if (code > 0) {
            return VGlobal.ITEM_MAP_ID.get(code);
        }
        return VGlobal.ITEM_MAP_NAME.get(name);
    }

    public boolean igualContem(Item item){
        if(getType() instanceof Item it){
            if(it.getCdItem() == item.getCdItem()){
                return true;
            }
        }
        if(getType() instanceof Grupo grupo){
            for(Item items : grupo.getListItem()){
                if(items.getCdItem() == item.getCdItem()){
                    return true;
                }
            }
        }
        return false;
>>>>>>> v4.0.0
    }

}
