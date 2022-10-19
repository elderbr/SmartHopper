package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class LivroController {

    public LivroController() {
    }

    public void informacao(Player player){
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setTitle("Informações");


        List<String> lista = new ArrayList<>();

        //Pagina 1
        lista.add("Para configurar o iltro vai precisar de uma bigorna e XP, coloque um ou mais funil na bigorna e " +
                "renome com o nome do item ou grupo em inglês, mas é recomendado usar o ID. Pronto o funil está " +
                "configurado. Se você não sabe qual é o ID configurou para o funil, basta clicar no ");

        // Pagina 2
        lista.add("funil com graveto na mão será mostrando o nome do item ou grupo." +
                "\n§l -Item\n§r" +
                "Para saber o ID do item basta escrever o nome do item tanto no inglês ou português ou segurar o item na mão e escrever /item.");

        // Pagina 3
        lista.add("§lGrupos\n§rGrupo são vários itens da mesma família ou do mesmo seguimento. O plugin vem pré-configurado grupos. Para saber o ID do grupo segure o item na mão ou escreva o código ou nome.\n" +
                "O mesmo item pode está representa em mais de um grupo, o ");

        // Pagina 4
        lista.add("tronco de pinheiro pode está no grupo Troncos e Pinheiro, escolha qual grupo deseja. Se não souber o nome do grupo pode obter uma lista de todos os nomes dos grupos usando o comando:" +
                "\nPode ver todos os itens que estão no grupo digitando /grupo nome ou ID, vai ");

        // Pagina 5
        lista.add("aparecer um inventário com todos os itens do grupo.");

        // Pagina 6
        lista.add("§l- Configurar mais de um item ou grupo\n§r" +
                "Para configurar mais de um item ou grupo para  o mesmo funil, separe  usando o ponto e virgula ( ; ) " +
                "O limite  é padrão da bigorna de 32 caracteres.");

        lista.add("§6Quer saber mais acesse:§r\n\n" +
                "§8Vídeo tutorial§r\n" +
                "https://youtu.be/fBIeZ57ka1M\n\n" +
                "https://github.com/elderbr/SmartHopper");

        bookMeta.setPages(lista);

        bookMeta.setAuthor("§eSmartHopper");
        itemStack.setItemMeta(bookMeta);
        player.getInventory().addItem(itemStack);
        Msg.PlayerGreen(player, "Você recebeu o livro com a informações do $eSmart Hopper");
        Msg.PlayerGold(player,"$lhttps://github.com/elderbr/SmartHopper");
        Msg.PlayerGreen(player,"$lhttps://youtu.be/fBIeZ57ka1M");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 40, 120);
    }
}
