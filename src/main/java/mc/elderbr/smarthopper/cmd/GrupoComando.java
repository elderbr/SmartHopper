package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.Dados;
import mc.elderbr.smarthopper.interfaces.InterfaceInventario;
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

    public Grupo grupo;
    private List<Grupo> listGrupo;

    private Item item;
    private ItemStack itemStack;
    public static InventoryCustom INVENTORY;
    private ItemStack itemSalve;
    private ItemMeta meta;
    private List<String> lore;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            itemStack = player.getInventory().getItemInMainHand();
            cmd = Utils.NAME_ARRAY(args);

            // Buscar grupo
            if (command.getName().equalsIgnoreCase("grupo")) {
                grupo = null;
                if (cmd.length() > 0) {
                    try {
                        grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo = VGlobal.GRUPO_MAP_NAME.get(cmd.toLowerCase());
                    }
                } else {
                    if (itemStack != null && itemStack.getType() == Material.AIR) {
                        Msg.PlayerGold(player, "Segure um item na mão ou digite o código ou nome do grupo!!!");
                        return false;
                    }

                    item = new Item(itemStack);
                    // Verificando se o item esta presenta mais de um grupo
                    listGrupo = new ArrayList<>();
                    for (Grupo grups : VGlobal.GRUPO_LIST) {
                        if (grups.getListItem().contains(item.getName())) {
                            listGrupo.add(grups);
                        }
                    }

                    if (listGrupo.size() > 1) {
                        Msg.PlayerGold(player, "#====================================#");
                        Msg.PlayerGold(player, "        LISTA DE GRUPOS");
                        for (Grupo grups : listGrupo) {
                            Msg.Grupo(player, grups);
                        }
                        Msg.PlayerGold(player, "#====================================#");
                        return false;
                    }
                    grupo = listGrupo.get(0);// PEGA O PRIMEIRO GRUPO ENCONTRADO
                }

                if (grupo != null) {
                    INVENTORY = new InventoryCustom(player);
                    INVENTORY.create(grupo);
                    player.openInventory(INVENTORY.getInventory());
                } else {
                    Msg.GrupoNaoExiste(player, cmd);
                }
                return false;
            }// FIM DE PESQUISAR GRUPO

            // ADICIONAR NOVO GRUPO
            if (command.getName().equalsIgnoreCase("addgrupo")) {
                if(!Config.CONTAINS_ADD(player)){
                    Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
                    return false;
                }

                INVENTORY = new InventoryCustom(player);
                INVENTORY.create(cmd);
                player.openInventory(INVENTORY.getInventory());
            }

            // REMOVER GRUPO
            if (command.getName().equalsIgnoreCase("removegrupo")) {

                if(!Config.CONTAINS_ADD(player)){
                    Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
                    return false;
                }

                grupo = null;
                if (cmd.length() > 0) {
                    try {
                        grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo = VGlobal.GRUPO_MAP_NAME.get(cmd.toLowerCase());
                    }
                }else{
                    Msg.PlayerRed(player, "Digite o nome ou código do grupo!!!");
                    return false;
                }

                // SE O GRUPO NÃO EXISTIR
                if (grupo == null) {
                    Msg.GrupoNaoExiste(player, cmd);
                    return false;
                }
                // DELETANDO O GRUPO
                if (GrupoConfig.DELETE(grupo)) {
                    Msg.PlayerTodos("§l§6O grupo §c" + grupo.getName() + "§6 foi removido pelo o ADM §e" + player.getName() + "§6!!!");
                }else{
                    Msg.PlayerRed(player, "Erro ao deletar o grupo!!!");
                }
                return false;
            }
        }
        return false;
    }
}