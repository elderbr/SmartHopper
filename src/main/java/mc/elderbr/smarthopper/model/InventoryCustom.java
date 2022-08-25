package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.InterfaceInventario;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InventoryCustom implements InterfaceInventario {

    private Player player;
    private int codigo;
    private String name;
    private List<String> listItem = new ArrayList<>();
    private Map<String, String> traducao = new HashMap<>();
    private Inventory inventory;
    private InventarioType type;

    private ItemStack barreira = new ItemStack(Material.BARRIER);
    private ItemStack save;
    private ItemStack ant;
    private ItemStack pro;
    private ItemMeta meta;

    // PAGINAÇÃO
    private int pag = 1;
    private Map<Integer, List<ItemStack>> pagMap = new HashMap<>();

    public InventoryCustom(@NotNull Player player) {

        save = new ItemStack(Material.BARRIER, 1);
        meta = save.getItemMeta();
        meta.setDisplayName("§2§lSalva");
        meta.setLore(Arrays.asList("§fSalva a lista"));
        meta.setCustomModelData(10);
        save.setItemMeta(meta);

        ant = new ItemStack(Material.BARRIER, 1);
        meta = ant.getItemMeta();
        meta.setDisplayName("§9§lRetorna");
        meta.setLore(Arrays.asList("§fRetorna a lista anteior"));
        meta.setCustomModelData(12);
        ant.setItemMeta(meta);

        pro = new ItemStack(Material.BARRIER, 1);
        meta = pro.getItemMeta();
        meta.setDisplayName("§9§lPróximo");
        meta.setLore(Arrays.asList("§fVai para próxima lista"));
        meta.setCustomModelData(11);
        pro.setItemMeta(meta);

        this.player = player;
    }

    @Override
    public String setPlayer(Player player) {
        this.player = player;
        return player.getName();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isAdm() {
        return Config.CONTAINS_ADD(player);
    }

    @Override
    public int setCodigo(int codigo) {
        return this.codigo = codigo;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public String setName(String name) {
        return this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getTraducao() {
        return traducao;
    }

    @Override
    public List<String> getLista() {
        return listItem;
    }

    public void create(@NotNull Object name) {
        if (name instanceof String titule) {
            this.name = titule.toLowerCase();
            type = InventarioType.NOVO;
        }
        if (name instanceof Grupo grup) {
            type = InventarioType.NORMAL;
            this.codigo = grup.getCodigo();
            this.name = grup.getName();
            traducao = grup.getTraducao();
            for (String names : grup.getListItem()) {
                listItem.add(names);
            }
        }
        inventory = Bukkit.createInventory(null, 54, toTitulo(player));

        // CRIANDO PAGINAÇÃO DOS ITENS
        pag = 1;
        List<ItemStack> lista = new ArrayList<>();
        int position = 1;
        for (String names : listItem) {
            lista.add(VGlobal.ITEM_MAP_NAME.get(names).parseItemStack());
            if (listItem.size() < 53) {
                if (position == listItem.size()) {
                    pagMap.put(pag, lista);
                }
            }
            if (listItem.size() > 52 && listItem.size() < 104) {
                if (position == 52) {
                    pagMap.put(1, lista);
                    lista = new ArrayList<>();
                }

                if (position == listItem.size()) {
                    pagMap.put(2, lista);
                    pag = 2;
                }
            }
            if (listItem.size() > 103 && listItem.size() < 155) {
                if (position == 52) {
                    pagMap.put(1, lista);
                    lista = new ArrayList<>();
                }
                if (position == 103) {
                    pagMap.put(2, lista);
                    lista = new ArrayList<>();
                }
                if (position == listItem.size()) {
                    pagMap.put(3, lista);
                    pag = 3;
                }
            }
            position++;
        }

    }

    public Inventory getInventory(@NotNull int pag) {

        for (ItemStack itemStack : pagMap.get(pag)) {
            itemStack.setAmount(1);
            inventory.addItem(itemStack);
        }

        if (pag == 1) {
            if (this.pag == 1) {
                if (isAdm()) {
                    inventory.setItem(53, save);
                }
            } else {
                inventory.setItem(53, pro);
            }
        }
        if (pag > 1) {
            inventory.setItem(52, ant);
            if (isAdm() && this.pag == 2) {
                inventory.setItem(53, save);
            } else {
                inventory.setItem(53, pro);
            }
        }

        return inventory;
    }

    @Override
    public InventarioType getType() {
        return type;
    }
}