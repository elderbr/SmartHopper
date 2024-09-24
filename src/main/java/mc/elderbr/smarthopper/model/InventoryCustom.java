package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InventoryCustom implements Botao, VGlobal {

    private final String TITULO_SMART_HOPPER = "§f§lSmart Hopper";
    private final String TITULO_GRUP_NEW = "§lNovo Grupo: §r";
    private final String TITULO_GRUP = "§lGrupo: §r";
    private SmartHopper smarthopper;
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

    public InventoryCustom(InventoryClickEvent event) throws GrupoException {
        player = (Player) event.getWhoClicked();
        inventoryTop = event.getView().getTopInventory();
        titulo = event.getView().getTitle();
        grupo = null;

        if (titulo.contains(TITULO_SMART_HOPPER)) {
            String name = titulo.replaceAll(TITULO_SMART_HOPPER, "").trim();
            smarthopper = new SmartHopper(name);
            for (IItem type : smarthopper.getTypes()) {
                if (type instanceof Item item) {
                    listItem.add(item);
                    continue;
                }
                if (type instanceof Grupo grup) {
                    for (Item item : grup.getItems()) {
                        listItem.add(item);
                    }
                }
            }
            return;
        }

        if (titulo.contains("Grupo")) {
            if (titulo.contains(TITULO_GRUP_NEW)) {
                grupo = new Grupo();
                grupo.setName(titulo.replaceAll(TITULO_GRUP_NEW, ""));
                if (GRUPO_MAP_NAME.get(grupo.getName()) != null) {
                    grupo = grupoCtrl.findByName(grupo.getName());
                    throw new GrupoException("O grupo já existe!!!");
                }
            } else {
                grupo = grupoCtrl.findByIdOrName(titulo.substring(titulo.lastIndexOf("ID:")));
                listItem = grupo.getItems();
            }
        }
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
        titulo = grupo.getName();
        this.listItem = grupo.getItems();
        pag = 1;
    }

    public InventoryCustom(@NotNull Player player, @NotNull List<IItem> list) {
        this.player = player;
        if (list.size() == 1 && list.get(0) instanceof Grupo grup) {
            grupo = grup;
            listItem = grup.getItems();
        } else {
            for (IItem type : list) {
                if (type instanceof Item item) {
                    listItem.add(item);
                    continue;
                }
                if (type instanceof Grupo grup) {
                    for (Item item : grup.getItems()) {
                        listItem.add(item);
                    }
                }
            }
        }
    }

    public InventoryCustom(@NotNull Player player, @NotNull SmartHopper smartHopper) {
        this.player = player;
        this.smarthopper = smartHopper;
        List<IItem> listType = smarthopper.getTypes();

        if(listType.size() == 1){// Se lista for igual a 1 e se for igual ao grupo
            if(listType.get(0) instanceof Grupo grupo){
                titulo = Msg.Color(TITULO_GRUP + grupo.toTranslation(player) + " $lID: $r" + grupo.getId());
                this.grupo = grupo;
                // Adicionando na lista de item
                for(Item item : grupo.getItems()){
                    listItem.add(item);
                }
            }
            return;
        }
        // Nome personalizado do inventário
        StringBuilder sb = new StringBuilder();
        for (IItem type : listType) {
            if (type instanceof Item item) {
                if (item.isBlocked()) {
                    sb.append("#");
                }
                sb.append("i").append(item.getId());
                listItem.add(item);
                continue;
            }
            if (type instanceof Grupo grup) {
                if (grup.isBlocked()) {
                    sb.append("#");
                }
                sb.append("g").append(grup.getId());
                for (Item item : grup.getItems()) {
                    listItem.add(item);
                }
            }
            sb.append(";");
        }
        if(listItem.size()<54){// Se a quantidade de item for menor que 54
            titulo = TITULO_SMART_HOPPER;
        }else {// Se a quantidade for maior que 54 adicionar os ID do item ou grupo
            titulo = "§f§lSmart Hopper " + sb.toString().substring(0, sb.toString().length() - 1);
        }
    }

    private void createPagination() {
        if (listItem.size() < 53) {
            pagMap.put(1, listItem);
            return;
        }

        int pageSize = (int) Math.ceil((double) listItem.size() / 54);

        // Adicionando botões na lista
        int position = 53;
        for (int i = 1; i <= pageSize; i++) {
            if (i == 1) {
                listItem.add(position, BtnNavegation());
                position = ((position + 53) > listItem.size()) ? listItem.size() : position + 53;
                continue;
            }
            if (i > 1 && i != pageSize) {
                listItem.add(position, BtnNavegation());
                listItem.add(position + 1, BtnNavegation());
                position = ((position + 54) > listItem.size()) ? listItem.size() : position + 54;
            }
        }

        // Criando páginação
        pageSize = (int) Math.ceil((double) listItem.size() / 54);
        int initialPosition = 0;
        int lastPosition = 54;
        for (int page = 1; page <= pageSize; page++) {
            pagMap.put(page, listItem.subList(initialPosition, lastPosition));
            initialPosition += 54;
            lastPosition += 54;
            if (lastPosition > listItem.size()) {
                lastPosition = listItem.size();
            }
        }
    }

    public InventoryCustom create() throws GrupoException {
        titulo = Msg.Color(TITULO_GRUP_NEW.concat(grupo.getName()));
        inventory = Bukkit.createInventory(null, 54, titulo);// Quantidade de espaço do baú
        inventory.setItem(53, BtnSalva());
        player.openInventory(inventory);
        return this;
    }

    public InventoryCustom show() throws GrupoException {

        // Se a lista estiver vazia ou se conter apenas um Item
        if (listItem.isEmpty() || listItem.size() == 1 && listItem.get(0) instanceof Item) {
            return this;
        }
        if (Objects.nonNull(grupo)) {// Se não existir grupo renomea o inventário para Smart Hopper
            titulo = Msg.Color(TITULO_GRUP + grupo.toTranslation(player) + " $lID: $r" + grupo.getId());
        }
        createPagination();
        // Criando um inventário personalizado
        inventory = Bukkit.createInventory(null, 54, titulo);
        List<Item> list = pagMap.get(pag);// Acessando a página
        for (Item item : list) {// Carregando os itens na página
            if (pagMap.size() > 1) {
                if (pag == 1) {
                    inventory.setItem(53, BtnNextPage(2));
                } else {
                    inventory.setItem(53, BtnPreviewPage(pag - 1));
                    if (grupo != null && AdmController.ContainsAdm(player)) {
                        inventory.setItem(52, BtnPreviewPage(pag - 1));
                        inventory.setItem(53, BtnSalva());
                    }
                }
            }
            inventory.addItem(item.getItemStack());
        }
        if (pagMap.size() == 1) {
            if (grupo != null && AdmController.ContainsAdm(player)) {
                inventory.setItem(53, BtnSalva());
            }
        }
        player.openInventory(inventory);// Abrindo o inventário
        return this;
    }

    public void btnNavegation(InventoryClickEvent event) throws GrupoException {

        itemStackClicked = event.getCurrentItem();
        if (itemStackClicked == null || itemStackClicked.getType() != Material.BARRIER) return;

        if (itemStackClicked.hasItemMeta() && itemStackClicked.getItemMeta().hasCustomModelData()) {
            ItemMeta meta = itemStackClicked.getItemMeta();
            int code = meta.getCustomModelData();
            if (code > 10) {
                int pageNumber = Integer.parseInt(meta.getDisplayName().substring(9).replaceAll("[^0-9]", ""));
                pag = pageNumber;
                show();
            }
        }
    }

    public String getTitle() {
        return titulo;
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