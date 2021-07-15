package mc.elderbr.smarthopper.model;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SmartHopper{

    private Hopper hopper;

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
        this.hopper = (Hopper) inventory.getLocation().getBlock().getState();
    }

    public SmartHopper(Block block) {
        this.hopper = (Hopper) block.getState();
    }

    public String getName() {
        return (hopper.getCustomName() != null ? hopper.getCustomName().toLowerCase().replaceAll("_", " ").trim() : "HOPPER");
    }

    public void setName(String name) {
        this.hopper.setCustomName(name);
    }

    public static SmartHopper ParseHopper(Inventory inventory) {
        return new SmartHopper(inventory);
    }

    public Item getItem() {
        nameItem = getName().replaceAll("#", "").trim();
        try {
            idItem = Integer.parseInt(nameItem.replace("i", ""));
            item = VGlobal.ITEM_MAP_ID.get(idItem);
        } catch (NumberFormatException e) {
            item = VGlobal.ITEM_MAP_NAME.get(nameItem);
        }
        return item;
    }

    public Item parseItem(String name) {
        nameItem = name.replace("#", "");
        try {
            idItem = Integer.parseInt(nameItem.replace("i", ""));
            this.item = VGlobal.ITEM_MAP_ID.get(idItem);
        } catch (NumberFormatException e) {
            this.item = VGlobal.ITEM_MAP_NAME.get(nameItem);
        }
        Msg.ServidorGreen("Item >> " + item.getName(), getClass());
        return item;
    }

    public boolean isItem() {
        getItem();// BUSCA NO BANCO O NOME DO ITEM
        return (item != null);
    }

    public boolean equalsItem(Item item) {
        getItem();// BUSCA NO BANCO O NOME DO ITEM
        if (this.item.getName().equals(item.getName())) {
            return true;
        }
        return false;
    }

    public boolean equalsITem(String item) {
        return getName().equals(new Item(item).getName());
    }


    /**
     * GRUPO
     **/

    public Grupo parseGrupo(String hopper) {

        nameGrupo = hopper.replaceAll("[*#]", "");// REMOVENDO A LETRA (G e * )

        try {
            // CONVERTE PARA INTEIRO
            idGrupo = Integer.parseInt(nameGrupo.replace("g", ""));
            grupo = VGlobal.GRUPO_MAP_ID.get(idGrupo);// BUSCA GRUPO PELO ID
        } catch (NumberFormatException e) {
            grupo = VGlobal.GRUPO_MAP_NAME.get(nameGrupo);// BUSCA PELO NOME DO GRUPO
        }
        return grupo;
    }

    public Grupo getGrupo() {
        if (isGrupo()) {
            nameGrupo = getName().replaceAll("[*#]", "").trim();// REMOVENDO A LETRA (#*)
            try {
                // CONVERTE PARA INTEIRO
                idGrupo = Integer.parseInt(nameGrupo.replace("g", ""));
                grupo = VGlobal.GRUPO_MAP_ID.get(idGrupo);// BUSCA GRUPO PELO ID
            } catch (NumberFormatException e) {
                grupo = VGlobal.GRUPO_MAP_NAME.get(nameGrupo);// BUSCA PELO NOME DO GRUPO
            }
            return grupo;
        }
        return null;
    }

    public boolean equalsGrupo(Item item) {
        return getGrupo().isContentItem(item);
    }

    public boolean isGrupo() {
        if (getName().contains("*") || getName().startsWith("g") || getName().startsWith("#g")) {
            return true;
        }
        return false;
    }

    public boolean isGrupoContains(Item item) {
        this.item = VGlobal.ITEM_MAP_NAME.get(item.getName());// BUSCA O ITEM PELO O NOME

        grupo = parseGrupo(getName());

        // VERICA SE O HOPPER CONTEM O * E SE ELE É IGUAL A NULO
        if (grupo == null) {
            return false;
        }
        return grupo.getItemList().contains(this.item);// VERIFICAR SE EXISTE O ITEM NO GRUPO
    }


    public boolean isContainerList() {
        return getName().contains(";");
    }

    public boolean isItemGrupo(Item item) {
        for (String args : getName().split(";")) {
            if (parseItem(args) instanceof Item) {
                Msg.ServidorGreen("Item type");
                return parseItem(args).equals(item);
            } else if (parseGrupo(args) instanceof Grupo) {
                Msg.ServidorGreen("grupo type");
                return parseGrupo(args).getItemList().contains(item);
            }
        }
        return false;
    }

    /*** CRIANDO FILTRO ***/
    public void addLore(String name) {
        ItemStack itemStack = new ItemStack(hopper.getType());
        ItemMeta meta = itemStack.getItemMeta();
        List<String> list = new ArrayList<>();
        list.add(name);
        meta.setLore(list);
        itemStack.setItemMeta(meta);
        hopper.setType(itemStack.getType());
    }

    public Hopper getHopper() {
        return hopper;
    }

    public Object getType() {
        String name = getName().replace("#", "");

        // VERIFICA SE O HOPPER É ITEM
        try {
            item = VGlobal.ITEM_MAP_ID.get(Integer.parseInt(name.replace("i", "")));
        } catch (NumberFormatException e) {
            item = VGlobal.ITEM_MAP_NAME.get(name);
        }

        // VERIFICA SE O HOPPER É ITEM
        try {
            grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(name.replaceAll("[g*]", "")));
        } catch (NumberFormatException e) {
            grupo = VGlobal.GRUPO_MAP_NAME.get(name.replaceAll("[*]", ""));
        }
        // SE O ITEM FOR DIFERENTE DE NULL RETORNA O ITEM
        if (item != null) {
            return item;
        } else {// SE NÃO RETORNA O RESULTADO DO GRUPO
            return grupo;
        }
    }

    /*============  SLOTS  =============================*/
    public Item getItem(int slot){
        return new Item(hopper.getInventory().getItem(slot));
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

    public int firstEmpty(){
        int position = 0;
        for(ItemStack items : hopper.getInventory().getContents()){
            if(items.getType() == Material.AIR){
                    return position;
            }
            position++;
        }
        return -1;
    }

    public int getAmount(Item item){
        return getItem(firtEmpty(item)).getAmount();
    }

    public int getMaxStackSize(Item item){
        return getItem(firtEmpty(item)).getMaxStackSize();
    }
}
