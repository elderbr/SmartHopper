package mc.elderbr.smarthopper.controllers;


import mc.elderbr.smarthopper.factories.ItemFactory;
import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmartHopper {


    private int code;
    private String name;
    private List<IItem> listType = new ArrayList<>();
    private Hopper myHopper = null;

    // ITEM
    private Item item;
    private ItemController itemCtrl = new ItemController();

    private Grupo grupo;
    private GrupoController grupCtrl = new GrupoController();

    public SmartHopper() {
    }

    public SmartHopper(@NotNull Hopper hopper) {
        listType = new ArrayList<>();
        if (hopper.getCustomName() == null) {
            return;
        }
        name = hopper.getCustomName();
        if (name.contains(";")) {
            for (String nameHopper : name.split(";")) {
                listType.add(getType(nameHopper));
            }
        } else {
            listType.add(getType(name));
        }
    }

    public SmartHopper(Block block) {
        listType = new ArrayList<>();
        if (block.getType() == Material.HOPPER) {
            myHopper = (Hopper) block.getState();
            name = myHopper.getCustomName();
            if(name == null){
                return;
            }
            if (name.contains(";")) {
                for (String nameHopper : name.split(";")) {
                    listType.add(getType(nameHopper));
                }
            } else {
                listType.add(getType(name));
            }
        }
    }

    public IItem getType(String name) {
        try {
            code = Integer.parseInt(name.replaceAll("[^0-9]", ""));
            String nameCustom = name.toLowerCase().replaceAll("#", "");
            if (nameCustom.charAt(0) == 'i') {
                item = itemCtrl.findByID(code);
                if (name.contains("#")) {
                    item.setBlocked(true);
                }
                return item;
            }
            if (nameCustom.charAt(0) == 'g') {
                grupo = grupCtrl.findById(code);
                if (name.contains("#")) {
                    grupo.setBlocked(true);
                }
                return grupo;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    public Object getType() {
        if (listType.isEmpty()) {
            return null;
        }
        if (listType.size() > 1) {
            return listType;
        }
        return listType.get(0);
    }

    public boolean isEqualsItem(Item item) {
        if (listType.isEmpty()) {
            return false;
        }
        for (IItem type : listType) {
            if (type instanceof Item itemSmart) {
                return itemSmart.equals(item);
            }
            if (type instanceof Grupo grupSmart) {
                return grupSmart.containsItem(item);
            }
        }
        return false;
    }
}