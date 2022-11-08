package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InformacaoComando implements CommandExecutor {

    private Player player;
    private String cmd;
    private ItemStack itemStack;
    private BookMeta bookMeta;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            cmd = Utils.NAME_ARRAY(args);

            if (command.getName().equalsIgnoreCase("informacao")) {
                itemStack = new ItemStack(Material.WRITTEN_BOOK);
                bookMeta = (BookMeta) itemStack.getItemMeta();
                bookMeta.setTitle("SmartHopper Informação");

                List<String> infor = new ArrayList<>();
                infor.add("§6§lSmartHopper Infor§r" +
                        "\n\n§802 - Configuração" +
                        "\n§003 - ITEM" +
                        "\n§804 - Grupo" +
                        "\n§006 - Novo Grupo" +
                        "\n§808 - Deleta Grupo" +
                        "\n§009 - Atualizar Grupo" +
                        "\n§810 - Administrador" +
                        "\n§012 - Tradução"
                );

                // CONFIGURAÇÃO DO FUNIL
                infor.add("§6§lConfigurando Funil§r" +
                        "\nPara configurar o funil precisa colocar o número do código do §6item§r ou §2grupo§r" +
                        " e para isso é só usar uma bigorna mudando o nome do funil." +
                        "\nPode ser adicionado mais de um item ou grupo para o mesmo funil separando por ponto e virgula (§l ; §r).");

                // COMANDO PARA O ITEM
                infor.add("§6§lITEM\n§rPara descobri o código do item digite §9§l/item§r segurando o item na mão ou digite o código ou nome." +
                        "\nPara configurar coloque o funil na bigorna renomei colocando a letra §li§r seguida com o código do item." +
                        "\nEx: I10");

                // COMANDO PARA O GRUPO
                infor.add("§2§lGrupo\n§rPara descobrir o código do grupo digite §9§l/grupo§r segurando o item na mão ou escreva o código ou nome." +
                        "\nO mesmo item pode está presente em mais de um grupo, será mostrado todos os grupo que tem o item.");
                infor.add("Para configurar o grupo na bigorna renomei o funil colocando a letra §lg§r seguida com o código do grupo." +
                        "\nEx: G20");

                // COMANDO PARA ADICIONAR NOVO GRUPO
                infor.add("§2§lNovo Grupo\n§rPara adicionar novo grupo digite §l§9/addgrupo§r e o nome desejado e aperte §lENTER§r." +
                        "\nSerá aberto um inventário. Deixe todos os itens que deseja colocar no grupo no seu inventário para ser transferido para o novo grupo.");
                infor.add("Com o botão esquerdo do mouse adicionar o item e com o direito remove." +
                        "\nDepois de adicionar clique na §2§llã verde§r para salvar");

                // COMANDO PARA REMOVER O GRUPO
                infor.add("§2§lRemover Grupo§r" +
                        "\nPara remover o grupo digite §9/removegrupo§r o código ou nome do grupo e aperte §lENTER§r" +
                        "\nOs grupos criados pelo o sistema não podem ser removidos!");

                // COMANDO PARA ATUALIZAR O GRUPO
                infor.add("§2§lAtualizar Grupo§r" +
                        "\nPara atualizar o grupo o jogador precisa ser administrador do §6§lSmart Hopper§r." +
                        "\nDigite §9§l/grupo§r segurando o item na mão ou digitando o código o nome." +
                        "\nCom o botão esquerdo do mouse adicionar o item e com o direito remove." +
                        "\nClique na §2§llã verde§r para atualizar");

                // COMANDO PARA ADICIONAR ADMINISTRADOR DO SMART HOPPER
                infor.add("§6§lADMINISTRADOR§r" +
                        "\nAdminsitrador é responsável por cuidar dos grupos adicionando e atualizando." +
                        "\nPara adicionar adminsitrador do §6§lSmart Hopper§r o jogador precisa ser operador do servidor.");
                infor.add("Digite §9/addadm§r e o nome do jogador, o jogador precisa está online." +
                        "\nPara remover digite §9/removeradm§r e o nome do jogador.");

                infor.add("§6§lAdiciona tradução§r" +
                        "\n\nItem: " +
                        "\nSegure o item na mão e digite §9/addtraducao§r e digite a tradução!" +
                        "\nGrupo: " +
                        "\nDigite §9/addtraducao grupo §r e o código do grupo e a tradução!");

                bookMeta.setPages(infor);
                bookMeta.setAuthor("§eSmartHopper");
                itemStack.setItemMeta(bookMeta);
                player.getInventory().addItem(itemStack);
                player.sendMessage("Você recebeu o livro com a lista dos grupos");
                player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_YES, 20, 120);

            }

        }

        return false;
    }
}
