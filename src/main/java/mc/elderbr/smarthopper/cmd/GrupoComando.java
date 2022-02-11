package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GrupoComando  implements CommandExecutor {

    private Player player;
    private String cmd;
    private String langPlayer;
    private ItemStack itemStack;

    // Grupo
    private Grupo grupo;
    private GrupoDao grupoDao;

    // Tradução
    private TraducaoDao traducaoDao;

    public GrupoComando() {
        grupoDao = new GrupoDao();
        traducaoDao = new TraducaoDao();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){
            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args).toLowerCase();// PEGA O NOME DO ITEM DIGITADO
            itemStack = player.getInventory().getItemInMainHand();// PEGA O NOME DO ITEM NA MÃO
            langPlayer = player.getLocale();// LINGUAGEM DO JOGADOR

            if(command.getName().equalsIgnoreCase("grupo")){
                grupo = new Grupo();
                grupo.setDsTraducao(cmd);

                grupo = grupoDao.select(grupo);
                if(grupo != null) {
                    traducaoDao.selectGrupo(grupo);
                }
            }

        }

        return false;
    }
}
