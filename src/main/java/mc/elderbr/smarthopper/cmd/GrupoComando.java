package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
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
    private GrupoController grupoCtrl;

    private ItemStack itemStack;
    public InventoryCustom inventory;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            myArgs = args;
            grupoCtrl = new GrupoController();

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
                // Busca o grupo pelo o nome
                grupo = grupoCtrl.findByName(cmd);
                // Mostra mensagem se não existir o grupo
                if(grupo == null){
                    Msg.GrupoNaoExiste(player, cmd);
                    return false;
                }
                // Criando o inventário do grupo
                inventory = new InventoryCustom(player, grupo);
                // Mostra o inventário com os itens do grupo
                inventory.show();
                return true;
            } else {
                // Busca todos os grupos que contém o item no grupo
                listGrupo = grupoCtrl.findByItemStack(itemStack);
                // Se a lista de item que possuir o grupo for igual a 1
                if(listGrupo.size()==1){
                    // Criando o inventário do grupo
                    inventory = new InventoryCustom(player, listGrupo.get(0));
                    inventory.show();// Mostra o inventário com os itens do grupo
                    return true;
                }
                // Mostra a mensagem dos nomes dos grupos que possui o item
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
        if (!player.isOp() && !AdmController.ContainsAdm(player)) {
            Msg.PlayerRed(player, "Ops, você não é adm do Smart Hopper!!!");
            return false;
        }

        try {
            // Verifica se o grupo já existe
            if (grupoCtrl.findByName(cmd) != null) {
                Msg.PlayerGold(player, "O grupo já existe!!!");
                show();// Mostra o grupo
                return false;
            }
            // Criando o inventário
            inventory = new InventoryCustom(player, cmd);
            inventory.create();
        } catch (GrupoException e) {
            Msg.PlayerGold(player, e.getMessage());
        }
        return false;
    }

    private boolean delete() {
        try {
            grupo = grupoCtrl.findByName(cmd);
            if(grupo == null){
                Msg.GrupoNaoExiste(player, cmd);
                return false;
            }
            if(grupoCtrl.delete(player, grupo)){
                Msg.PlayerTodos(String.format("$eO jogador $c%s $eapagou o grupo $c%s$e!!!", player.getName(), grupo.getName()));
                return true;
            }
        } catch (GrupoException e) {
            Msg.PlayerGold(player, e.getMessage());
        }
        return false;
    }
}