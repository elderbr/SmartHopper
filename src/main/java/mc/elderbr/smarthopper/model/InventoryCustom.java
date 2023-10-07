package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
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
import java.util.Iterator;
import java.util.List;

public class InventoryCustom implements Botao, VGlobal {

    private final Class CLAZZ = getClass();
    private Player player;
    private String name, titulo;
    private Inventory inventory;
    private Inventory inventoryTop;
    private Inventory inventoryBottom;

    private ItemStack itemStack;
    private ItemStack itemStackClicked;
    private Grupo grupo;
    private boolean isGrupo = false;

    private GrupoController grupoCtrl = new GrupoController();

    // PAGINAÇÃO
    private int pag = 1;
    private HashMap<Integer, List<Item>> pagMap = new HashMap<>();
    private List<Item> listItem = new ArrayList<>();

    private InventoryCustom() {
    }

    public InventoryCustom(InventoryClickEvent event) throws GrupoException {
        player = (Player) event.getWhoClicked();
        inventoryTop = event.getView().getTopInventory();
        titulo = event.getView().getTitle();
        itemStackClicked = event.getCurrentItem();

        if (titulo.contains("Grupo")) {
            event.setCancelled(true);
            if (grupo != null && !grupo.equals(grupoCtrl.getGrupo(titulo))) {
                grupo = grupoCtrl.getGrupo(titulo);
            } else {
                grupo = grupoCtrl.getGrupo(titulo);
            }
            createPagination();
        }
    }

    public InventoryCustom(@NotNull Player player) {
        this.player = player;
    }

    public InventoryCustom(@NotNull Player player, @NotNull String name) throws GrupoException {
        this.player = player;
        this.name = name;

        // Verifica se o jogador é adm
        if (!Config.CONTAINS_ADD(player)) {
            throw new GrupoException("Ops, você não Adm do Smart Hopper!!!");
        }

        // Verificar se o nome do grupo contem texto
        if (name.isEmpty()) {
            throw new GrupoException("O nome do grupo não pode está vazio!!!");
        }
    }

    public InventoryCustom(@NotNull Player player, @NotNull Grupo grupo) {
        this.player = player;
        this.grupo = grupo;
        createPagination();
        pag = 1;
    }

    private void createPagination() {
        List<Item> grupList = new ArrayList<>(grupo.getListItem());
        Iterator<Item> iterator = grupList.iterator();

        listItem = new ArrayList<>();
        pag = 1;
        while (iterator.hasNext()) {
            Item item = iterator.next();
            listItem.add(item);
            if (pag == 1) {
                if (listItem.size() == 53) {
                    pagMap.put(pag, listItem);
                    pag++;
                    listItem = new ArrayList<>();
                }
            } else {
                if (listItem.size() == 52) {
                    pagMap.put(pag, listItem);
                    pag++;
                    listItem = new ArrayList<>();
                }
            }
            if (grupList.size() == 1) {
                if (grupo.getListItem().size() > 53 || grupo.getListItem().size() < 53) {
                    pagMap.put(pag, listItem);
                }
            }
            iterator.remove();
        }
    }

    public InventoryCustom create() throws GrupoException {
        // Se o grupo exitir busca informações
        if (grupo != null && grupo.getId() > 0) {
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

    public InventoryCustom show() throws GrupoException {
        // Titulo do grupo
        titulo = Msg.Color("$lGrupo: $r" + grupo.toTranslation(player) + " $lID: $r" + grupo.getId());
        inventory = Bukkit.createInventory(null, 54, titulo);
        if (pagMap.get(pag) == null) {
            pag -= 1;
        }
        for (Item item : pagMap.get(pag)) {
            inventory.addItem(item.parseItemStack());
        }

        switch (pag) {
            case 1:
                if (pagMap.size() == 1) {
                    if (player.isOp() || Config.CONTAINS_ADD(player)) {
                        inventory.setItem(53, BtnSalva());
                    }
                } else {
                    inventory.setItem(53, BtnProximoPag2());
                }
                break;
            case 2:
                if (pagMap.size() == 2) {
                    if (player.isOp() || Config.CONTAINS_ADD(player)) {
                        inventory.setItem(52, BtnAnteriorPag1());
                        inventory.setItem(53, BtnSalva());
                    } else {
                        inventory.setItem(53, BtnAnteriorPag1());
                    }
                } else {
                    inventory.setItem(53, BtnProximoPag3());
                }
                break;
            case 3:
                if (pagMap.size() == 3) {
                    if (player.isOp() || Config.CONTAINS_ADD(player)) {
                        inventory.setItem(52, BtnAnteriorPag2());
                        inventory.setItem(53, BtnSalva());
                    } else {
                        inventory.setItem(53, BtnAnteriorPag2());
                    }
                }
                break;
        }
        player.openInventory(inventory);
        return this;
    }

    public void addItem(@NotNull Item item) {
        inventory.addItem(ItemController.ParseItemStack(item));
    }

    public void addItem(InventoryClickEvent event) {

        // Inventario do superior
        inventoryTop = player.getOpenInventory().getTopInventory();
        if (inventoryTop.getType() != InventoryType.CHEST) {
            return;
        }

        if (grupo != null) {

            // Se não for adm do servidor ou do SmartHopper retorna
            if (!player.isOp() && !Config.CONTAINS_ADD(player)) {
                return;
            }
            itemStackClicked = event.getCurrentItem();
            if (itemStackClicked == null || itemStackClicked.getType().isAir()) return;

            // Copiando o tipo do item clicado
            itemStack = new ItemStack(itemStackClicked.getType());
            itemStack.setAmount(1);
            // Se for clicado com o botão esquerdo adiciona o item
            if (event.isLeftClick()) {
                if (!inventoryTop.contains(itemStack)) {
                    inventoryTop.addItem(itemStack);
                }
            }
            // Removendo o item do inventario superior
            if (event.isRightClick() && inventoryTop.contains(itemStack)) {
                inventoryTop.removeItem(itemStack);
            }
        }
    }

    public void btnNavegation() throws GrupoException {

        if (grupo == null || itemStackClicked == null || itemStackClicked.getType() == Material.AIR) return;

        if(BtnAnteriorPag1().equals(itemStackClicked)){
            pag = 1;
            show();
        }else
        if (BtnProximoPag2().equals(itemStackClicked)) {
            pag = 2;
            show();
        } else if (BtnProximoPag3().equals(itemStackClicked)) {
            pag = 3;
            show();
        }
    }

    public Grupo getGrupo(){
        return grupo;
    }

    public boolean isGrupo() {
        return isGrupo;
    }

    public int getMaxPag() {
        return pagMap.size();
    }

    public boolean save() {
        grupo.setId(CD_MAX.get(0) + 1);
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            Item item = new Item(itemStack);
            if (!itemStack.equals(BtnSalva())
                    && !itemStack.equals(BtnAnteriorPag1())
                    && !itemStack.equals(BtnProximo())
                    && !grupo.getListItem().contains(item)
            ) {
                grupo.addListItem(item);
            }
        }
        return GrupoConfig.ADD(grupo);
    }

    public void saves() {
        if (player.isOp() || Config.CONTAINS_ADD(player)) {
            if (itemStackClicked.equals(BtnSalva())) {
                for (ItemStack itemStack : inventoryTop.getContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getType() == Material.BARRIER)
                        continue;
                    grupo.addListItem(itemStack);
                }
                Msg.ServidorBlue("salvar o lista de item do grupo", CLAZZ);
                for (Item item : grupo.getListItem()) {
                    Msg.ServidorBlue("item: " + item, CLAZZ);
                }
            }
        }
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isContains(ItemStack itemStack){
        itemStack = new ItemStack(itemStack.getType());
        for(Item item : grupo.getListItem()){
            if(item.parseItemStack().hashCode() == itemStack.hashCode()){
                return true;
            }
        }
        return false;
    }
}