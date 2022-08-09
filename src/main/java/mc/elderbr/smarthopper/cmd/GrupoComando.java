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

    private Grupo grupo;
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

            grupo = null;

            if (command.getName().equalsIgnoreCase("grupo")) {

                if (cmd.length() > 0) {
                    try {
                        grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo = VGlobal.GRUPO_MAP_NAME.get(cmd);
                    }
                } else if (itemStack.getType() == Material.AIR) {
                    Msg.ServidorGold("Segure um item na mão!!!");
                    return false;
                }else{
                    item = new Item(itemStack);

                    listGrupo = new ArrayList<>();
                    Msg.PlayerGold(player,"Lista de grupo");
                    for(Grupo grupo : VGlobal.GRUPO_LIST){
                        if(grupo.getListItem().contains(item.getDsItem()) && !listGrupo.contains(grupo)){
                            listGrupo.add(grupo);
                            Msg.Grupo(player, grupo);
                        }
                    }
                    Msg.PlayerGold(player,"#====================================#");
                    return false;
                }

                if(grupo == null){
                    Msg.GrupoNaoExiste(player, cmd);
                    return false;
                }

                Msg.Grupo(player, grupo);

            }

        }
        return false;
    }
}