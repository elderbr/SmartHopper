package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class SmartHopper {

    private Hopper hopper = null;
    private int code;
    private String name;
    private String nameHopper;
    private String silaba;

    public SmartHopper(Hopper hopper) {
        this.hopper = hopper;
    }

    public SmartHopper(Inventory inventory) {
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
    }

}