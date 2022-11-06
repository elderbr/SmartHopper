package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GrupoComando implements CommandExecutor {

    private Player player;
    private String cmd;
    private String[] myArgs;
    public Grupo grupo;
    private ItemStack itemStack;
    public InventoryCustom inventory;

    private GrupoController grupoController;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            grupoController = new GrupoController();
            myArgs = args;

            player = (Player) sender;
            itemStack = player.getInventory().getItemInMainHand();
            cmd = Utils.NAME_ARRAY(args);

            switch (command.getName().toLowerCase()) {
                case "grupo":// Buscar grupo
                    show();
                    break;
                case "addgrupo":// ADICIONAR NOVO GRUPO
                    add();
                    break;
                case "removegrupo":// REMOVER GRUPO
                    delete();
                    break;
            }
        }
        return false;
    }

    private boolean show() {
        try {
            if (myArgs.length == 0) {
                grupoController.getGrupo(itemStack);// Se não houver argumentos busca o item que estiver na mão do jogador
            } else {
                grupoController.getGrupo(cmd);// Pega o nome ou ID digitado
            }

            // Mostra todos os grupos que contem o item
            Msg.PlayerGold(player, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            for (Grupo grupo : grupoController.getListGrupo()) {
                Msg.Grupo(player, grupo);
            }

            // Se existir apenas um grupo para o item mostra o inventario
            if (grupoController.getListGrupo().size() == 1) {
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
    }

    private boolean add() {
        if (!Config.CONTAINS_ADD(player)) {
            Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
            return false;
        }

        inventory = new InventoryCustom(player);
        inventory.create(cmd);
        player.openInventory(inventory.getInventory(1));
        return false;
    }

    private boolean delete() {
        try {
            if (myArgs.length == 0) {
                grupoController.getGrupo(itemStack);// Se não houver argumentos busca o item que estiver na mão do jogador
            } else {
                grupoController.getGrupo(cmd);// Pega o nome ou ID digitado
            }
            grupo = grupoController.getListGrupo().get(0);
            grupoController.delete(player, grupo);
            Msg.PlayerTodos("$l$6O grupo $c" + grupo.getName() + "$6 foi removido pelo o ADM $e" + player.getName() + "$6!!!");
            return true;
        } catch (GrupoException e) {
            Msg.PlayerGold(player, e.getMessage());
        }
        return false;
    }
}