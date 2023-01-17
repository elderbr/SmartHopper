package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.Funil;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryCustom implements Botao {

    private Player player;
    private int codigo = 0;
    private String name;
    private Inventory inventory;
    private String titulo = null;
    private InventarioType type;

    private Grupo grupo;

    // PAGINAÇÃO
    private int pag = 1;
    private double pagQuant;

    public InventoryCustom(@NotNull Player player) {
        this.player = player;
    }
    public InventoryCustom(@NotNull Player player, @NotNull String name) {
        this.player = player;
        this.name = name;
    }

    public InventoryCustom(Player player, Grupo grupo) {
        this.player = player;
        this.grupo = grupo;
    }

    public InventoryCustom create() throws GrupoException {
        if(grupo != null) {
            titulo = Msg.Color("$lGrupo: $r" +grupo.toTranslation(player)+ " $lID: $r" + grupo.getId());
            inventory = Bukkit.createInventory(null, 54, titulo);
            for(Item item : grupo.getListItem()){
                addItem(item);
            }
        }else{
            if(name == null || name.isEmpty()){
                throw new GrupoException("Para criar um novo grupo digite o nome do grupo!!!");
            }
            titulo = Msg.Color("$lGrupo: $r" + name);
            inventory = Bukkit.createInventory(null, 54, titulo);
        }
        return this;
    }
    public void addItem(@NotNull ItemStack itemStack){
        inventory.addItem(itemStack);
    }
    public void addItem(@NotNull Item item){
        inventory.addItem(ItemController.ParseItemStack(item));
    }

    public InventoryCustom show(){
        player.openInventory(inventory);
        return this;
    }
}