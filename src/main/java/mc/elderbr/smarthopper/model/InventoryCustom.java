package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.Funil;
import mc.elderbr.smarthopper.interfaces.InterfaceInventario;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
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

public class InventoryCustom implements InterfaceInventario, Botao {

    private Player player;
    private int codigo = 0;
    private String name;
    private List<String> listItem = new ArrayList<>();
    private Map<String, String> traducao = new HashMap<>();
    private Inventory inventory;
    private InventarioType type;

    private ItemStack barreira = new ItemStack(Material.BARRIER);
    private Grupo grupo;

    // PAGINAÇÃO
    private int pag = 1;
    private double pagQuant;
    List<ItemStack> listItemStack;
    private Map<Integer, List<String>> pagMap = new HashMap<>();

    public InventoryCustom(@NotNull Player player) {
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
    public Funil setCodigo(int codigo) {
        this.codigo = codigo;
        return this;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public InventoryCustom setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name.replaceAll(Msg.Color("$lGrupo: $r"), "");
    }

    @Override
    public boolean isBloqueado() {
        return false;
    }

    @Override
    public Funil setBloqueado(boolean bloqueado) {
        return null;
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

    }

    public Inventory getInventory(@NotNull int pag) {

        if (type == InventarioType.NOVO) {
            inventory.setItem(53, BtnSalva());
            return inventory;
        }


        return inventory;
    }

    @Override
    public InterfaceInventario setType(InventarioType type) {
        this.type = type;
        return this;
    }

    @Override
    public InventarioType getType() {
        return type;
    }

    public Grupo getGrupo() {
        return grupo;
    }
}