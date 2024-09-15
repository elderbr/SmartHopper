package mc.elderbr.smarthopper.utils;

import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Msg {

    private final static String ServerMsg = ChatColor.GREEN + " SmartHopper >> ";

    public static void ServidorGreen(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.DARK_GREEN + Color(msg));
    }

    public static void ServidorGreen(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.DARK_GREEN + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorGreen(String msg, String metodo, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.DARK_GREEN + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorRed(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.RED + Color(msg));
    }

    public static void ServidorGold(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.GOLD + Color(msg));
    }

    public static void ServidorGold(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.GOLD + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    public static void ServidorWhite(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.WHITE + Color(msg));
    }

    public static void ServidorColored(String msg, String msg1, String msg2) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + Color(msg) + " - " + ChatColor.YELLOW + msg1 + " - " + ChatColor.AQUA + msg2);
    }

    public static void ServidorBlue(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.BLUE + Color(msg));
    }

    public static void ServidorBlue(String msg, Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + ChatColor.BLUE + Color(msg) + " - Class: " + getClass.getSimpleName());
    }

    /**
     * @param getClass
     */
    public static void PularLinha(Class getClass) {
        Bukkit.getServer().getConsoleSender().sendMessage(ServerMsg + " #====================================# - Class: " + getClass.getSimpleName() + "\n");
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

    public static void PlayerTodos(String msg) {
        Bukkit.getServer().broadcastMessage(Color(msg));
    }

    //========================== ITEM =================================================//
    public static void Item(Player player, Item item) {
        player.sendMessage(Color("$2Item: $6" + item.toTranslation(player) + "$e ID: " + item.getId()));
    }

    public static void ItemNegar(Player player, Item item) {
        player.sendMessage(Color("$cBloqueado$6 o item: " + item.toTranslation(player) + "$e ID: " + item.getId()));
    }

    public static void ItemNaoExiste(Player player, String name) {
        player.sendMessage(Color("$aO item $6" + name + " $4$lNÃO $r$aexiste!"));
    }

    //========================== GRUPO =================================================//
    public static void SystemGrupo(Grupo grupo) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n======== GRUPO ========\n");
        if (grupo.getId() > 1) {
            sb.append("ID: ").append(grupo.getId()).append("\n");
        }
        if (!grupo.getName().isBlank()) {
            sb.append("Name: ").append(grupo.getName()).append("\n");
        }
        if (grupo.getTranslations().size() > 0) {
            sb.append("lang:\n");
            sb.append(grupo.getTranslations()).append("\n");
        }

        sb.append("======== Lista de item ========");
        for (String nameItem : grupo.getItemsNames()) {
            sb.append("\n- ").append(nameItem);
        }
        Bukkit.getServer().getConsoleSender().sendMessage(sb.toString());
    }

    public static void Grupo(Player player, Grupo grupo) {
        if (grupo.isBlocked()) {
            player.sendMessage(Color("$cBloqueado$6 o §9grupo: §e" + grupo.toTranslation(player) + "$e ID: " + grupo.getId()));
        } else {
            player.sendMessage(Color("$9Grupo: $e" + grupo.toTranslation(player) + " $6ID: " + grupo.getId()));
        }
    }

    public static void Grupo(Grupo grupo, @NotNull Class classe) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                "Grupo ID: " + grupo.getId()
                        + "\nNome: " + grupo.getName()
                        + "\nTraducao: " + grupo.getTranslations()
                        + "\nItens: " + grupo.getItems()
        );
        PularLinha(classe);
    }

    public static void GrupoNaoExiste(Player player, String name) {
        player.sendMessage(Color("$9O grupo $e" + name + "$4 NÃO $r$9existe!"));
    }

    public static void GrupoNegar(Player player, Grupo grupo) {
        player.sendMessage(Color("$cBloqueado$6 o §9grupo: §e" + grupo.toTranslation(player) + "$e ID: " + grupo.getId()));
    }

    public static void getTypes(Player player, List<IItem> list) {
        if (!list.isEmpty()) {
            PulaPlayer(player);
            for (IItem obj : list) {
                if (obj instanceof Item item) {
                    if (item.isBlocked()) {
                        player.sendMessage(Color("$cBloqueado$6 o item: " + item.toTranslation(player) + "$e ID: " + item.getId()));
                    } else {
                        player.sendMessage(Color("$2Item: $6" + item.toTranslation(player) + "$e ID: " + item.getId()));
                    }
                }
                if (obj instanceof Grupo grupo) {
                    if (grupo.isBlocked()) {
                        player.sendMessage(Color("$cBloqueado$6 o §9grupo: §e" + grupo.toTranslation(player) + "$e ID: " + grupo.getId()));
                    } else {
                        player.sendMessage(Color("$9Grupo: $e" + grupo.toTranslation(player) + " $6ID: " + grupo.getId()));
                    }
                }
            }
            return;
        }
        player.sendMessage(Color("$6Funil $cNÃO $6configurado!!!"));
    }

    public static void getType(Player player, List<Grupo> list) {
        if (!list.isEmpty()) {
            PulaPlayer(player);
            for (Grupo grup : list) {
                if (grup.isBlocked()) {
                    player.sendMessage(Color("$cBloqueado$6 o §9grupo: §e" + grup.toTranslation(player) + "$e ID: " + grup.getId()));
                } else {
                    player.sendMessage(Color("$9Grupo: $e" + grup.toTranslation(player) + " $6ID: " + grup.getId()));
                }
            }
            return;
        }
        player.sendMessage(Color("$6Funil $cNÃO $6configurado!!!"));
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