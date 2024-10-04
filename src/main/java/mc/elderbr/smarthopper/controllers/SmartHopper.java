package mc.elderbr.smarthopper.controllers;


import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.minecart.HopperMinecart;
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

    private Block block = null;

    public SmartHopper() {
    }

    public SmartHopper(@NotNull String title) {
        listType = new ArrayList<>();
        if (title.isBlank()) {
            return;
        }
        if (title.contains(";")) {
            for (String nameHopper : title.split(";")) {
                listType.add(getTypes(nameHopper));
            }
        } else {
            listType.add(getTypes(title));
        }
    }

    public SmartHopper(@NotNull Hopper hopper) {
        block = hopper.getBlock();
        listType = new ArrayList<>();
        name = hopper.getCustomName();
        if (name == null) {
            return;
        }
        if (name.contains(";")) {
            for (String nameHopper : name.split(";")) {
                listType.add(getTypes(nameHopper));
            }
        } else {
            listType.add(getTypes(name));
        }
    }

    public SmartHopper(@NotNull HopperMinecart hopperMinecart) {
        block = hopperMinecart.getLocation().getBlock();
        listType = new ArrayList<>();
        name = hopperMinecart.getCustomName();
        if (name == null) {
            return;
        }
        if (name.contains(";")) {
            for (String nameHopper : name.split(";")) {
                listType.add(getTypes(nameHopper));
            }
        } else {
            listType.add(getTypes(name));
        }
    }

    public SmartHopper(Block block) {
        this.block = block;
        listType = new ArrayList<>();
        if (block.getType() == Material.HOPPER) {
            myHopper = (Hopper) block.getState();
            name = myHopper.getCustomName();
            if (name == null) {
                return;
            }
            if (name.contains(";")) {
                for (String nameHopper : name.split(";")) {
                    listType.add(getTypes(nameHopper));
                }
            } else {
                listType.add(getTypes(name));
            }
        }
    }

    public IItem getTypes(String name) {
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
            if (nameCustom.contains("grupo")) {
                grupo = grupCtrl.findById(code);
                if (name.contains("#")) {
                    grupo.setBlocked(true);
                }
                return grupo;
            }
        } catch (NumberFormatException e) {
            return null;
        } catch (ItemException | GrupoException ex) {
            if (Objects.nonNull(block)) {
                Msg.ServidorBlue(String.format("Erro o funil nomeado como %s localizado na posição %d %d %d no mundo %s",
                        name, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ(), block.getWorld().getName()));
            } else {
                Msg.ServidorRed(ex.getMessage());
            }
        }
        return null;
    }

    public List<IItem> getTypes() {
        return listType;
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