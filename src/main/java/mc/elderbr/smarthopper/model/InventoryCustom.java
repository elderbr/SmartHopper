package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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

    private final String TITULO_GRUP_NEW = "§lNovo Grupo: §r";
    private final String TITULO_GRUP = "§lGrupo: §r";
    private Player player;
    private String titulo;
    private Inventory inventory;
    private Inventory inventoryTop;
    private ItemController itemCtrl = new ItemController();
    private ItemStack itemStack;
    private ItemStack itemStackClicked;
    private Grupo grupo;
    private boolean isGrupo = false;

    private GrupoController grupoCtrl = new GrupoController();

    // PAGINAÇÃO
    private int pag = 1;
    private HashMap<Integer, List<Item>> pagMap = new HashMap<>();
    private List<Item> listItem = new ArrayList<>();

    private ConfigDao configDao = new ConfigDao();

    private InventoryCustom() {
    }

    public InventoryCustom(InventoryOpenEvent event) throws GrupoException {
        player = (Player) event.getPlayer();
        inventoryTop = event.getView().getTopInventory();
        titulo = event.getView().getTitle();
        grupo = null;

        if (titulo.contains("Grupo")) {

            if (titulo.contains(TITULO_GRUP_NEW)) {
                grupo = new Grupo();
                grupo.setName(titulo.replaceAll(TITULO_GRUP_NEW, ""));
                if (grupoCtrl.findByName(grupo.getName()) != null) {
                    grupo = grupoCtrl.findByName(grupo.getName());
                    throw new GrupoException("O grupo já existe!!!");
                }
            } else {
                grupo = grupoCtrl.findByIdOrName(titulo.substring(titulo.lastIndexOf("ID:")));
            }
            createPagination();
        }
    }

    public InventoryCustom(@NotNull Player player) {
        this.player = player;
    }

    public InventoryCustom(@NotNull Player player, @NotNull String name) throws GrupoException {
        this.player = player;

        // Verifica se o jogador é adm
        if (!player.isOp() && !AdmController.ContainsAdm(player)) {
            throw new GrupoException("Ops, você não Adm do Smart Hopper!!!");
        }

        // Verificar se o nome do grupo contem texto
        if (name.isBlank()) {
            throw new GrupoException("O nome do grupo não pode está vazio!!!");
        }
        grupo = new Grupo();
        grupo.setName(name);
    }

    public InventoryCustom(@NotNull Player player, @NotNull Grupo grupo) {
        this.player = player;
        this.grupo = grupo;
        createPagination();
        pag = 1;
    }

    private void createPagination() {
        List<Item> grupList = grupo.getItems();
        pag = 1;
        int index = 0;
        listItem = new ArrayList<>();

        if (grupList.size() < 53) {
            for (int i = 0; i < grupList.size(); i++) {
                listItem.add(grupList.get(i));
            }
            pagMap.put(1, listItem);
            return;
        }

        for (int i = 0; i < grupList.size(); i++) {
            listItem.add(grupList.get(i));
            if (index == 53) {
                index = 0;
                pagMap.put(pag, listItem);
                listItem = new ArrayList<>();
                pag++;
            }
            index++;
        }
    }

    public InventoryCustom create() throws GrupoException {
        if (GRUPO_MAP_NAME.get(grupo.getName().toLowerCase()) != null) {
            throw new GrupoException("O grupo " + grupo.getName() + " já existe!!!");
        }
        // Nome do grupo
        titulo = Msg.Color(TITULO_GRUP_NEW.concat(grupo.getName()));
        inventory = Bukkit.createInventory(null, 54, titulo);// Quantidade de espaço do baú
        inventory.setItem(53, BtnSalva());
        player.openInventory(inventory);
        return this;
    }

    public InventoryCustom show() throws GrupoException {
        // Titulo do grupo
        titulo = Msg.Color(TITULO_GRUP + grupo.toTranslation(player) + " $lID: $r" + grupo.getId());
        inventory = Bukkit.createInventory(null, 54, titulo);
        List<Item> list = pagMap.get(pag);
        for (Item item : list) {
            inventory.addItem(item.getItemStack());
        }
        if (inventory.getItem(53) == null && player.isOp() || configDao.containsAdm(player)) {
            inventory.setItem(53, BtnSalva());
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

        // Verifica se existe grupo
        if (grupo != null) {

            // Se não for adm do servidor ou do SmartHopper retorna
            if (!player.isOp() && !AdmController.ContainsAdm(player)) {
                return;
            }
            // Pegando o item que foi clicado
            itemStackClicked = event.getCurrentItem();
            if (itemStackClicked == null || itemStackClicked.getType().isAir()) return;

            // Copiando o tipo do item clicado
            itemStack = new ItemStack(itemStackClicked.getType());
            itemStack.setAmount(1);
            // Se for clicado com o botão esquerdo adiciona o item
            if (event.isLeftClick()) {
                if (!grupo.getItems().contains(Item.TO_ItemStack(itemStack))) {
                    inventoryTop.addItem(itemStack);
                    grupo.addItems(itemCtrl.findByItemStack(itemStack));
                }
            }
        }
    }

    public void btnNavegation(InventoryClickEvent event) throws GrupoException {

        itemStackClicked = event.getCurrentItem();
        if (grupo == null || itemStackClicked == null || itemStackClicked.getType() == Material.AIR) return;

        if (BtnAnteriorPag1().equals(itemStackClicked)) {
            pag = 1;
            show();
        } else if (BtnProximoPag2().equals(itemStackClicked)) {
            pag = 2;
            show();
        } else if (BtnProximoPag3().equals(itemStackClicked)) {
            pag = 3;
            show();
        } else if (BtnAnteriorPag2().equals(itemStackClicked)) {
            pag = 2;
            show();
        }
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public boolean isGrupo() {
        return isGrupo;
    }

    public int getMaxPag() {
        return pagMap.size();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}