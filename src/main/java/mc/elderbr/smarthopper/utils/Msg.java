package mc.elderbr.smarthopper.utils;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Msg {

    public static void ServidorGreen(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " SmartHopper >> " + Color(msg));
    }

    public static void ServidorGreen(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " SmartHopper >> " + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorGreen(String msg, String metodo, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " SmartHopper >> " + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorRed(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + " SmartHopper >> " + Color(msg));
    }

    public static void ServidorGold(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " SmartHopper >> " + Color(msg));
    }

    public static void ServidorGold(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " SmartHopper >> " + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorWhite(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + " SmartHopper >> " + Color(msg));
    }

    public static void ServidorColored(String msg, String msg1, String msg2) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " SmartHopper >> " + Color(msg) + " - " + ChatColor.YELLOW + msg1 + " - " + ChatColor.AQUA + msg2);
    }

    public static void ServidorBlue(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " SmartHopper >> " + Color(msg));
    }

    public static void ServidorBlue(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " SmartHopper >> " + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    /**
     * @param getClass
     */
    public static void PularLinha(Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage("SmartHopper >> #====================================# - Class: " + getClass.getSimpleName() + "\n");
    }

    //========================== PLAYERS =================================================//

    //Mensagem do player
    public static void PlayerGreen(Player player, String msg) {
        player.sendMessage(ChatColor.GREEN + Color(msg));
    }

    public static void PlayerGold(Player player, String msg) {
        player.sendMessage(ChatColor.GOLD + Color(msg));
    }

    public static void PlayerRed(Player player, String msg) {
        player.sendMessage(ChatColor.RED + Color(msg));
    }

    public static void PlayerTodos(String msg){
        Bukkit.getServer().broadcastMessage(Color(msg));
    }

    //========================== ITEM =================================================//
    public static void Item(Player player, Item item) {
        player.sendMessage(Color("$2Item: $6" + item.toTraducao(player) + "$e ID: " + item.getCodigo()));
    }

    public static void ItemNegar(Player player, Item item) {
        player.sendMessage(Color("$cBloqueado$6 o item: " + item.toTraducao(player) + "$e ID: " + item.getCodigo()));
    }

    public static void ItemNaoExiste(Player player, String name) {
        player.sendMessage(Color("$aO item $6" + name + " $4$lNÃO $r$aexiste!"));
    }

    //========================== GRUPO =================================================//
    public static void Grupo(Player player, Grupo grupo) {
        player.sendMessage(Color("$9Grupo: $e" + grupo.toTraducao(player) + " $6ID: " + grupo.getCodigo()));
    }

    public static void Grupo(Grupo grupo, @NotNull Class classe) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                "Grupo ID: " + grupo.getCodigo()
                        + "\nnome: " + grupo.getName()
                        + "\nTraducao: " + grupo.getTraducao().values()
        );
    }

    public static void GrupoNaoExiste(Player player, String name) {
        player.sendMessage(Color("$2O grupo $e" + name + "$6 NÃO existe!"));
    }

    public static void GrupoNegar(Player player, Grupo grupo) {
        player.sendMessage(Color("$cBloqueado$6 o §9grupo: §e" + grupo.toTraducao(player) + "$e ID: " + grupo.getCodigo()));
    }

    public static void PulaPlayer(Player player) {
        player.sendMessage("=====================================================");
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