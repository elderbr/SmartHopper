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

import java.util.List;

public class GrupoComando implements CommandExecutor {

    private Player player;
    private String cmd;
    private String[] myArgs;
    private Grupo grupo;
    private List<Grupo> listGrupo;
    private GrupoController grupoController;

    private ItemStack itemStack;
    public InventoryCustom inventory;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            myArgs = args;
            grupoController = new GrupoController();

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
            if (cmd.length() > 0) {
                grupo = grupoController.findName(cmd);
                inventory = new InventoryCustom(player, grupo);
                inventory.show();
                return true;
            } else {
                listGrupo = grupoController.getGrupo(itemStack);
                Msg.getType(player, listGrupo);
                return true;
            }
        } catch (Exception e) {
            Msg.PlayerGold(player, e.getMessage());
        }
        return false;
    }

    private boolean add() {

        // Verifica se o jogador é adm do Smart hopper
        if (!Config.CONTAINS_ADD(player)) {
            Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
            return false;
        }

        try {
            inventory = new InventoryCustom(player, cmd);
            inventory.create();
        } catch (GrupoException e) {
            Msg.PlayerGold(player, e.getMessage());
        }
        return false;
    }

    private boolean delete() {

        return false;
    }
}