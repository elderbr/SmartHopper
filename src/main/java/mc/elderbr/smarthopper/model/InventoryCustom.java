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
    }

    public Inventory getInventory() {

        if (listItem.size() > 52) {
            int position = 0;
            for (String name : listItem) {
                if (position < 53) {
                    inventory.addItem(new Item(name).getItemStack());
                }else{
                    inventory.setItem(53, pro);
                }
                position++;
            }
        } else {
            for (String name : listItem) {
                inventory.addItem(VGlobal.ITEM_MAP_NAME.get(name).getItemStack());
            }
            if(isAdm()) {// SE O JOGADOR FOR ADM DO SMART HOPPER MOSTRA O BOTÃO DE SALVAR
                inventory.setItem(53, save);
            }
        }
        return inventory;
    }

    @Override
    public InventarioType getType() {
        return type;
    }
}