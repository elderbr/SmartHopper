package mc.elderbr.smarthopper.controllers;


import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static mc.elderbr.smarthopper.interfaces.VGlobal.GRUPO_MAP_ID;
import static mc.elderbr.smarthopper.interfaces.VGlobal.ITEM_MAP_ID;

public class SmartHopper {


    private int codigo;
    private String name;
    private Object type = null;
    private List<Object> listType = null;
    private Hopper myHopper = null;
    private Block block;

    // ITEM
    private Item item;

    private Grupo grupo;

    public SmartHopper() {

    }


    public SmartHopper(@NotNull Object hopper) throws Exception {
        myHopper = null;
        if (hopper instanceof Hopper funil) {
            myHopper = funil;
        }
        if (hopper instanceof Inventory inventory) {
            if (inventory.getType() == InventoryType.HOPPER) {
                myHopper = (Hopper) inventory.getLocation().getBlock().getState();
            }
        }
        if (hopper instanceof Block block) {
            if (block.getType() == Material.HOPPER) {
                myHopper = (Hopper) block.getState();
            }
        }
        if (myHopper == null) {// Se não existir o hopper
            return;
        }

        // Verifica se existe nome personalizado para o hopper
        if (myHopper.getCustomName() == null) {
            return;
        }

        // Pegando o bloco hopper
        block = myHopper.getBlock();

        // Pegando o nome do hopper
        name = myHopper.getCustomName().toLowerCase();

        if (name.contains(";")) {
            listType = new ArrayList<>();
            for (String names : name.split(";")) {

                if (names.substring(0, 2).contains("i")) {// Se o nome do hopper conter a letra i se trata de item
                    // Pegando o código do item
                    try {
                        codigo = Integer.parseInt(names.replaceAll("[#i]", ""));
                    } catch (NumberFormatException e) {
                        throw new ItemException("O funil configurado com o nome " + names + " não é valido!", block);
                    }
                    // Pegando o item na memória
                    item = ITEM_MAP_ID.get(codigo);
                    if (item == null) {
                        throw new ItemException("O funil configurado como " + names + " não está na lista de itens!", block);
                    }
                    // Se conter cerquilha o item é negado
                    item.setBlocked(name.contains("#"));
                    listType.add(item);
                }

                // Se o nome do hopper conter a letra G se trata de grupo
                if (names.substring(0, 2).contains("g")) {
                    // Pegando o código do grupo
                    try {
                        codigo = Integer.parseInt(names.replaceAll("[#g]", ""));
                    } catch (NumberFormatException e) {
                        throw new ItemException("O funil configurado com o nome " + names + " não é valido!", block);
                    }
                    // Buscando o grupo em memória
                    grupo = GRUPO_MAP_ID.get(codigo);
                    if (grupo == null) {
                        throw new ItemException("O funil configurado com o nome " + names + " não está na lista de grupos!", block);
                    }
                    // Se conter cerquilha o grupo é negado
                    grupo.setBlocked(name.contains("#"));
                    listType.add(grupo);
                }
            }
            type = listType;
            return;
        }

        // Se o nome do hopper conter a letra i se trata de item
        if (name.substring(0, 2).contains("i")) {
            // Pegando o código do item
            try {
                codigo = Integer.parseInt(name.replaceAll("[#i]", ""));
            } catch (NumberFormatException e) {
                throw new ItemException("O funil configurado com o nome " + name + " não é valido!", block);
            }
            // Pegando o item na memória
            item = ITEM_MAP_ID.get(codigo);
            if (item == null) {
                throw new ItemException("O funil configurado como " + name + " não está na lista de itens!", block);
            }
            // Se conter cerquilha o item é negado
            item.setBlocked(name.contains("#"));
            type = item;
            return;
        }

        // Se o nome do hopper conter a letra G se trata de grupo
        if (name.substring(0, 2).contains("g")) {
            // Pegando o código do grupo
            try {
                codigo = Integer.parseInt(name.replaceAll("[#g]", ""));
            } catch (NumberFormatException e) {
                throw new ItemException("O funil configurado com o nome " + name + " não é valido!", block);
            }
            // Buscando o grupo em memória
            grupo = GRUPO_MAP_ID.get(codigo);
            if (grupo == null) {
                throw new ItemException("O funil configurado com o nome " + name + " não está na lista de grupos!", block);
            }
            // Se conter cerquilha o grupo é negado
            grupo.setBlocked(name.contains("#"));
            type = grupo;
        }
    }


    public Object getType() {
        return type;
    }

    public Object getType(Inventory inventory) throws Exception {
        myHopper = null;
        if (inventory instanceof Hopper funil) {
            myHopper = funil;
        }

        if (inventory.getType() == InventoryType.HOPPER) {
            myHopper = (Hopper) inventory.getLocation().getBlock().getState();
        }

        if (inventory instanceof Block block) {
            if (block.getType() == Material.HOPPER) {
                myHopper = (Hopper) block.getState();
            }
        }
        if (myHopper == null) {// Se não existir o hopper
            return null;
        }

        // Verifica se existe nome personalizado para o hopper
        if (myHopper.getCustomName() == null) {
            return null;
        }

        // Pegando o bloco hopper
        block = myHopper.getBlock();

        // Pegando o nome do hopper
        name = myHopper.getCustomName().toLowerCase();

        if (name.contains(";")) {
            listType = new ArrayList<>();
            for (String names : name.split(";")) {

                if (names.substring(0, 2).contains("i")) {// Se o nome do hopper conter a letra i se trata de item
                    // Pegando o código do item
                    try {
                        codigo = Integer.parseInt(names.replaceAll("[#i]", ""));
                    } catch (NumberFormatException e) {
                        throw new ItemException("O funil configurado com o nome " + names + " não é valido!", block);
                    }
                    // Pegando o item na memória
                    item = ITEM_MAP_ID.get(codigo);
                    if (item == null) {
                        throw new ItemException("O funil configurado como " + names + " não está na lista de itens!", block);
                    }
                    // Se conter cerquilha o item é negado
                    item.setBlocked(name.contains("#"));
                    listType.add(item);
                }else
                // Se o nome do hopper conter a letra G se trata de grupo
                if (names.substring(0, 2).contains("g")) {
                    // Pegando o código do grupo
                    try {
                        codigo = Integer.parseInt(names.replaceAll("[#g]", ""));
                    } catch (NumberFormatException e) {
                        throw new ItemException("O funil configurado com o nome " + names + " não é valido!", block);
                    }
                    // Buscando o grupo em memória
                    grupo = GRUPO_MAP_ID.get(codigo);
                    if (grupo == null) {
                        throw new ItemException("O funil configurado com o nome " + names + " não está na lista de grupos!", block);
                    }
                    // Se conter cerquilha o grupo é negado
                    grupo.setBlocked(name.contains("#"));
                    listType.add(grupo);
                }else{
                    throw new ItemException("O funil configurado com o nome " + names + " não está na lista de grupos!", block);
                }
            }
            type = listType;
            return type;
        }

        // Se o nome do hopper conter a letra i se trata de item
        if (name.substring(0, 2).contains("i")) {
            // Pegando o código do item
            try {
                codigo = Integer.parseInt(name.replaceAll("[#i]", ""));
            } catch (NumberFormatException e) {
                throw new ItemException("O funil configurado com o nome " + name + " não é valido!", block);
            }
            // Pegando o item na memória
            item = ITEM_MAP_ID.get(codigo);
            if (item == null) {
                throw new ItemException("O funil configurado como " + name + " não está na lista de itens!", block);
            }
            // Se conter cerquilha o item é negado
            item.setBlocked(name.contains("#"));
            type = item;
            return type;
        }

        // Se o nome do hopper conter a letra G se trata de grupo
        if (name.substring(0, 2).contains("g")) {
            // Pegando o código do grupo
            try {
                codigo = Integer.parseInt(name.replaceAll("[#g]", ""));
            } catch (NumberFormatException e) {
                throw new ItemException("O funil configurado com o nome " + name + " não é valido!", block);
            }
            // Buscando o grupo em memória
            grupo = GRUPO_MAP_ID.get(codigo);
            if (grupo == null) {
                throw new ItemException("O funil configurado com o nome " + name + " não está na lista de grupos!", block);
            }
            // Se conter cerquilha o grupo é negado
            grupo.setBlocked(name.contains("#"));
            type = grupo;
        }
        return type;
    }
}