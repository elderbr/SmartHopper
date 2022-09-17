package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Botao;
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
    public int setCodigo(int codigo) {
        return this.codigo = codigo;
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
    public Map<String, String> getTraducao() {
        return traducao;
    }

    @Override
    public List<String> getLista() {
        return listItem;
    }

    public void create(@NotNull Object name) {
        if (name instanceof String titule) {
            this.name = titule.toLowerCase().replaceAll(Msg.Color("$lGrupo: $r"), "");
            Msg.ServidorGold("titulo: "+ this.name, getClass());
            this.codigo = Utils.ParseNumber(this.name);
            if (codigo > 0) {
                type = InventarioType.NORMAL;
                grupo = VGlobal.GRUPO_MAP_ID.get(codigo);
            } else {
                type = InventarioType.NOVO;
                grupo = new Grupo();
                grupo.setName(this.name);
                grupo.addTraducao(player.getLocale(), this.name);
            }
        }
        if (name instanceof Grupo grup) {
            grupo = grup;
            type = InventarioType.NORMAL;
            this.codigo = grup.getCodigo();
            this.name = grup.getName();
            traducao = grup.getTraducao();
        }

        inventory = Bukkit.createInventory(null, 54, toTitulo(player));

        // Paginação
        pagQuant = (grupo.getListItem().size()/54.0);
        pagMap = new HashMap<>();
        listItem = new ArrayList<>();
        for (int i = 0; i < grupo.getListItem().size(); i++) {
            listItem.add(grupo.getListItem().get(i));
            if(pagQuant < 0.99 && (grupo.getListItem().size()-1) == i){
                pagMap.put(1, listItem);
                listItem = new ArrayList<>();
            }
            if(pagQuant > 0.98 && pagQuant < 1.95){
                if(i == 52){
                    pagMap.put(1, listItem);
                    listItem = new ArrayList<>();
                }
                if( (grupo.getListItem().size()-1) == i) {
                    pagMap.put(2, listItem);
                    listItem = new ArrayList<>();
                }
            }
        }
    }

    public Inventory getInventory(@NotNull int pag) {

        if(type == InventarioType.NOVO){
            inventory.setItem(53, BtnSalva());
            return inventory;
        }

        if(pagQuant > 0 && pagQuant < 0.99){
            for(String values : pagMap.get(pag)){
                inventory.addItem(new Item(values).parseItemStack());
            }
            if(Config.CONTAINS_ADD(player)) {
                inventory.setItem(53, BtnSalva());
            }
        }
        if(pagQuant > 0.98 && pagQuant < 1.95){
            for(String values : pagMap.get(pag)){
                inventory.addItem(new Item(values).parseItemStack());
            }
            if(pag == 1){
                inventory.setItem(53, BtnProximo());
            }
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