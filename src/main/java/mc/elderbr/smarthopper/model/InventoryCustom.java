package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.Funil;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.GRUPO_MAP_ID;
import static mc.elderbr.smarthopper.interfaces.VGlobal.GRUPO_MAP_NAME;

public class InventoryCustom implements Botao {

    private Player player;
    private int codigo = 0;
    private String name, titulo;
    private Inventory inventory;
    private Grupo grupo;
    private GrupoController grupoController = new GrupoController();
    private boolean isGrupo = false;

    // PAGINAÇÃO
    private int pag = 1;
    private HashMap<Integer, List<Item>> pagMap = new HashMap<>();
    private List<Item> listItem = new ArrayList<>();

    public InventoryCustom(@NotNull Player player) {
        this.player = player;
    }

    public InventoryCustom(@NotNull Player player, @NotNull String name) throws GrupoException {
        this.player = player;
        this.name = name;

        if(name.isEmpty()){
            throw new GrupoException("O nome do grupo não pode está vazio!!!");
        }
        grupoController.getGrupo(this.name);
        grupo = grupoController.getGrupo();
    }

    public InventoryCustom(@NotNull Player player, @NotNull InventoryClickEvent inventoryClick) throws GrupoException {
        // VERIFICA SE O INVENTARIO É BAÚ

        if (inventoryClick.getView().getTopInventory().getType() != InventoryType.CHEST) {
            return;
        }
        inventory = inventoryClick.getView().getTopInventory();// Pegando o invetário superior
        titulo = inventoryClick.getView().getTitle();// Pagando o nome do baú
        this.player = player;// O jogador que efetuo o comando

        // Verifica se o baú de cima é um grupo
        if (!titulo.contains("Grupo")) {
            return;
        }

        isGrupo = true;

        // Grupo existente
        if(titulo.contains("ID")) {
            codigo = Utils.ParseNumber(titulo.substring(titulo.indexOf("ID:"), titulo.length()));// Pegando o código do grupo
            grupoController.getGrupo(codigo);
            grupo = grupoController.getGrupo();
        }else{
            grupo = new Grupo();
            grupo.setName(name);
        }
    }

    public InventoryCustom(@NotNull Player player,@NotNull  Grupo grupo) {
        this.player = player;
        this.grupo = grupo;

        // Dados do grupo
        codigo = grupo.getId();
        name = grupo.getName();
    }

    public InventoryCustom create() throws GrupoException {
        // Se o grupo exitir busca informações
        if (grupo != null) {
            // Nome do grupo
            titulo = Msg.Color("$lGrupo: $r" + grupo.toTranslation(player) + " $lID: $r" + grupo.getId());
            inventory = Bukkit.createInventory(null, 54, titulo);// Quantidade de espaço do baú

            // Paginação
            int listSize = grupo.getListItem().size();
            pagMap = new HashMap<>();

            // Adicionando os itens no inventário
            if (listSize > 53) {
                for (int i = 0; i < 53; i++) {
                    Item item = grupo.getListItem().get(i);
                    listItem.add(item);
                }
                pagMap.put(1, listItem);

                listItem = new ArrayList<>();
                for (int i = 53; i < listSize; i++) {
                    Item item = grupo.getListItem().get(i);
                    listItem.add(item);
                }
                pagMap.put(2, listItem);
            } else {
                for (int i = 0; i < listSize; i++) {
                    Item item = grupo.getListItem().get(i);
                    listItem.add(item);
                }
                pagMap.put(1, listItem);
            }
        } else {
            if (name == null || name.isEmpty()) {
                throw new GrupoException("Para criar um novo grupo digite o nome do grupo!!!");
            }
            titulo = Msg.Color("$lGrupo: $r" + name);
            inventory = Bukkit.createInventory(null, 54, titulo);
        }
        return this;
    }

    public void addItem(@NotNull ItemStack itemStack) {
        inventory.addItem(itemStack);
    }

    public void addItem(@NotNull Item item) {
        inventory.addItem(ItemController.ParseItemStack(item));
    }

    public InventoryCustom show() throws GrupoException {
        show(1);
        player.openInventory(inventory);
        return this;
    }

    public InventoryCustom show(int pag) throws GrupoException {

        this.pag = pag;

        if (pagMap.get(pag) == null) {
            throw new GrupoException("Nâo foi possivel exibir os itens do grupo!");
        }

        switch (pag) {
            case 1:
                if (Config.CONTAINS_ADD(player)) {
                    inventory.setItem(53, BtnSalva());
                }
                if (pagMap.size() > 1) {// Se conter mais do que uma página
                    inventory.setItem(53, BtnProximo());
                }
                break;
            case 2:
                inventory.setItem(53, BtnAnterior());
                if (pagMap.size() > 2) {// SE HOUVER MAIS DE 2 PÁGINA
                    inventory.setItem(52, BtnAnterior());
                    inventory.setItem(53, BtnProximo());
                } else {
                    if (Config.CONTAINS_ADD(player)) {
                        inventory.setItem(52, BtnAnterior());
                        inventory.setItem(53, BtnSalva());
                    }
                }
                break;
            default:
                if (Config.CONTAINS_ADD(player)) {
                    inventory.setItem(53, BtnSalva());
                }
        }

        for (Item item : pagMap.get(pag)) {
            addItem(item);
        }

        player.openInventory(inventory);
        return this;
    }

    public boolean isGrupo() {
        return isGrupo;
    }

    public int getMaxPag() {
        return pagMap.size();
    }

    public boolean save() {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            Item item = new Item(itemStack);
            if (!itemStack.equals(BtnSalva())
                    && !itemStack.equals(BtnAnterior())
                    && !itemStack.equals(BtnProximo())
                    && !grupo.getListItem().contains(item)
            ) {
                grupo.addListItem(item);
            }
        }
        return GrupoConfig.UPDATE(grupo);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String toNameGrupo() {
        return grupo.getName();
    }
}