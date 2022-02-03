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
}
