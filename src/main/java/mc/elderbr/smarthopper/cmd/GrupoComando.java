package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GrupoComando implements CommandExecutor {

    private Player player;
    private String cmd;

    public static Grupo GRUPO;
    private String nameGrupo;
    private List<Grupo> listGrupo;

    private Item item;
    private ItemStack itemStack;
    private InventoryCustom inventory;
    private ItemStack itemSalve;
    private ItemMeta meta;
    private List<String> lore;


    private InventoryCustom inventoryCustom;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            itemStack = player.getInventory().getItemInMainHand();
            cmd = Utils.NAME_ARRAY(args);

            GRUPO = null;

            // Buscar grupo
            if (command.getName().equalsIgnoreCase("grupo")) {

                if (cmd.length() > 0) {
                    try {
                        GRUPO = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        GRUPO = VGlobal.GRUPO_MAP_NAME.get(cmd);
                    }
                } else {
                    if (itemStack.getType() == Material.AIR) {
                        Msg.ServidorGold("Segure um item na mão!!!");
                        return false;
                    }
                    item = new Item(itemStack);

                    listGrupo = new ArrayList<>();
                    for (Grupo grupo : VGlobal.GRUPO_LIST) {
                        if (grupo.getListItem().contains(item.getDsItem()) && !listGrupo.contains(grupo)) {
                            listGrupo.add(grupo);
                        }
                    }
                    if (listGrupo.size() > 1) {
                        Msg.PlayerGold(player, "#====================================#");
                        Msg.PlayerGold(player, "Lista de grupo");
                        for (Grupo grupo : listGrupo) {
                            grupo.setDsLang(player);
                            Msg.Grupo(player, grupo);
                        }
                        Msg.PlayerGold(player, "#====================================#");
                    }
                    return false;
                }

                if (GRUPO == null) {
                    Msg.GrupoNaoExiste(player, cmd);
                    return false;
                }

                inventoryCustom = new InventoryCustom();
                inventoryCustom.create(GRUPO.toTraducao().concat(Msg.Color(" $lID:$r" + GRUPO.getCdGrupo())));
                for (String names : GRUPO.getListItem()) {
                    inventoryCustom.addItem(new Item(names).getItemStack());
                }
                player.openInventory(inventoryCustom.getInventory());

                Msg.Grupo(player, GRUPO);
                return false;
            }

            /************  ADICIONA NOVO GRUPO     ************/
            if (command.getName().equalsIgnoreCase("addgrupo")) {
                if (!VGlobal.ADM_LIST.contains(player.getName())) {
                    Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
                    return false;
                }
                if (cmd.length() < 4) {
                    Msg.PlayerRed(player, "O nome do grupo precisar conter mais do que 3 letras!!!");
                    return false;
                }

                if(VGlobal.GRUPO_MAP_NAME.get(cmd)!=null){
                    Msg.PlayerRed(player, String.format("Esse grupo já existe §4§l%s§r§c!!!", cmd));
                    return false;
                }

                inventoryCustom = new InventoryCustom();
                GRUPO = new Grupo();
                GRUPO.setDsGrupo(cmd);
                GRUPO.setDsTraducao(cmd);

                inventory = new InventoryCustom();
                inventory.createNewGrupo(cmd);

                // CRIANDO O BOTÃO PARA SALVAR
                itemSalve = new ItemStack(Material.LIME_WOOL);
                meta = itemSalve.getItemMeta();
                meta.setDisplayName(Msg.Color("$aSalva"));
                lore = new ArrayList<>();
                lore.add(Msg.Color("$3Salva novo grupo"));
                meta.setLore(lore);
                itemSalve.setItemMeta(meta);

                inventory.getInventory().setItem(53, itemSalve);

                player.openInventory(inventory.getInventory());

            }
        }
        return false;
    }
}