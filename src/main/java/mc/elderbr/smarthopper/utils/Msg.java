package mc.elderbr.smarthopper.utils;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Msg {

    public static void ServidorGreen(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " SmartHopper >> " + msg);
    }

    public static void ServidorGreen(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " SmartHopper >> " + msg + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorGreen(String msg, String metodo, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " SmartHopper >> " + msg + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorRed(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + " SmartHopper >> " + msg);
    }

    public static void ServidorGold(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " SmartHopper >> " + msg);
    }

    public static void ServidorGold(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " SmartHopper >> " + msg + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorWhite(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + " SmartHopper >> " + msg);
    }

    public static void ServidorColored(String msg, String msg1, String msg2) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " SmartHopper >> " + msg + " - " + ChatColor.YELLOW + msg1 + " - " + ChatColor.AQUA + msg2);
    }

    public static void ServidorBlue(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " SmartHopper >> " + msg);
    }

    public static void ServidorBlue(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " SmartHopper >> " + msg + " - Class: " + getClass.getSimpleName());
    }

    /**
     * @param getClass
     */
    public static void PularLinha(Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage("SmartHopper >> #====================================# - Class: " + getClass.getSimpleName() + "\n");
    }

    //========================== PLAYERS =================================================//

    //Mensagem do player
    public static void PlayerGreen(@NotNull Player player, String msg) {
        player.sendMessage(ChatColor.GREEN + msg);
    }

    public static void PlayerGold(@NotNull Player player, String msg) {
        player.sendMessage(ChatColor.GOLD + msg);
    }

    public static void PlayerRed(@NotNull Player player, String msg) {
        player.sendMessage(ChatColor.RED + msg);
    }

    public static void PlayerBlue(@NotNull Player player, String msg) {
        player.sendMessage(ChatColor.BLUE + msg);
    }

    public static void PlayerGreenLine(@NotNull Player player) {
        player.sendMessage(ChatColor.GREEN + "-----------------------------------------------------");
    }

    public static void PlayerGoldLine(@NotNull Player player) {
        player.sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
    }

    public static void PlayerRedLine(@NotNull Player player) {
        player.sendMessage(ChatColor.RED + "-----------------------------------------------------");
    }

    public static void PlayerBlueLine(@NotNull Player player) {
        player.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
    }

    //========================== ITEM =================================================//

    public static void Item(@NotNull Item item,@NotNull Class classe) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                ChatColor.GREEN + "Item " + ChatColor.YELLOW + item.toString()
                        + ChatColor.GREEN + " - ID: " + ChatColor.GOLD + item.getCdItem()+"\n§r - classe: "+ classe.getName()
        );
    }

    public static void ItemPlayer(@NotNull Player player, Item item) {
        player.sendMessage(
                ChatColor.GREEN + "Item " + ChatColor.YELLOW + item.toString()
                        + ChatColor.GREEN + " - ID: " + ChatColor.GOLD + item.getCdItem()
        );
    }

    public static void ItemNaoExiste(@NotNull Player player, String name) {
        player.sendMessage(Color("$aO item $6" + name + " $4$lNÃO $r$aexiste!"));
    }

    //========================== GRUPO =================================================//
    public static void Grupo(@NotNull Grupo grupo, @NotNull Class classe){
        Bukkit.getServer().getConsoleSender().sendMessage(
                ChatColor.GREEN + "grupo: " + ChatColor.GOLD + grupo.toString()
                        + ChatColor.GREEN + " - ID: " + ChatColor.GOLD + grupo.getCdGrupo()+"\n§rclasse: "+ classe.getName()
        );
    }
    public static void GrupoNaoExiste(@NotNull Player player, String name) {
        player.sendMessage(Color("$2O grupo $e" + name + " $6NÃO existe!"));
    }

    public static void GrupoPlayer(@NotNull Player player, Grupo grupo) {
        player.sendMessage(
                ChatColor.AQUA + "Grupo: " + ChatColor.YELLOW + (grupo.getDsTraducao() == null ? grupo.getDsGrupo() : grupo.getDsTraducao())
                        + ChatColor.AQUA + " - ID: " + ChatColor.YELLOW + grupo.getCdGrupo());
    }


    //======================= ERRROS ====================================================//

    public static void ServidorErro(Exception error, String metodo, Class classe) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                Color("$dErro: " + error.getMessage() +
                        "\nmetodo: " + metodo +
                        "\nCausa: " + error.getCause() +
                        "\nClasse: " + classe.getSimpleName()));
        error.printStackTrace();
    }

    public static void ServidorErro(String msg, String metodo, Class classe, Exception error) {
        Bukkit.getServer().getConsoleSender().sendMessage(Color("$dErro: " + error.getMessage()
                + "\nmetodo: " + metodo
                + "\nCausa: " + error.getCause()
                + "\nClasse: " + classe.getSimpleName()));
        error.printStackTrace();
    }

    /***
     * Adiciona cores aos textos substituindo o $ por $
     * @param text texto que vai ser modificado
     * @return o texto com a cor setada
     */
    public static String Color(String text) {
        return ChatColor.translateAlternateColorCodes('$', text);
    }
}
