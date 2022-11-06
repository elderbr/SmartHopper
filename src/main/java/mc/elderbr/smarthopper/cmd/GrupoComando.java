package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
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
    public InventoryCustom inventory;
    private ItemStack itemSalve;
    private ItemMeta meta;
    private List<String> lore;

    private GrupoController grupoController;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            grupoController = new GrupoController();

            player = (Player) sender;
            itemStack = player.getInventory().getItemInMainHand();
            cmd = Utils.NAME_ARRAY(args);

            // Buscar grupo
            if (command.getName().equalsIgnoreCase("grupo")) {
                try {
                    if (args.length == 0) {
                        grupoController.getGrupo(itemStack);// Se não houver argumentos busca o item que estiver na mão do jogador
                    }else{
                        grupoController.getGrupo(cmd);// Pega o nome ou ID digitado
                    }

                    // Mostra todos os grupos que contem o item
                    Msg.PlayerGold(player, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                    for(Grupo grupo : grupoController.getListGrupo()){
                        Msg.Grupo(player, grupo);
                    }

                    // Se existir apenas um grupo para o item mostra o inventario
                    if(grupoController.getListGrupo().size() == 1){
                        grupo = grupoController.getListGrupo().get(0);
                        inventory = new InventoryCustom(player);
                        inventory.create(grupo);
                        player.openInventory(inventory.getInventory(1));
                    }
                    return true;
                } catch (GrupoException e) {
                    Msg.PlayerGold(player, e.getMessage());
                }
                return false;
            }// FIM DE PESQUISAR GRUPO

            // ADICIONAR NOVO GRUPO
            if (command.getName().equalsIgnoreCase("addgrupo")) {
                if (!Config.CONTAINS_ADD(player)) {
                    Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
                    return false;
                }

                inventory = new InventoryCustom(player);
                inventory.create(cmd);
                player.openInventory(inventory.getInventory(1));
            }

            // REMOVER GRUPO
            if (command.getName().equalsIgnoreCase("removegrupo")) {

                if (!Config.CONTAINS_ADD(player)) {
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
                } else {
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
                    VGlobal.GRUPO_LIST.remove(grupo);
                    VGlobal.GRUPO_MAP_ID.remove(grupo.getCodigo());
                    VGlobal.GRUPO_MAP_NAME.remove(grupo.getName());
                    VGlobal.TRADUCAO_GRUPO.remove(grupo.getName().toLowerCase());
                    VGlobal.TRADUCAO_GRUPO.remove(grupo.toTraducao(player).toLowerCase());
                    Msg.PlayerTodos("$l$6O grupo $c" + grupo.getName() + "$6 foi removido pelo o ADM $e" + player.getName() + "$6!!!");
                } else {
                    Msg.PlayerRed(player, "Erro ao deletar o grupo!!!");
                }
                return false;
            }// FIM DE DELETAR GRUPO
        }
        return false;
    }
}