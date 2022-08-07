package mc.elderbr.smarthopper;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSmartHopper extends JavaPlugin implements Listener {

    private Config config;

    // Item
    private Item item;
    private ItemConfig itemConfig;

    // Grupo
    private GrupoConfig grupoConfig;
    private TraducaoConfig traducaoConfig;


    @Override
    public void onEnable() {

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN +
                "\n+---------------------------+\n" +
                "| Smart Hopper              |\n" +
                "| Version " + VGlobal.VERSION + "             |\n" +
                "| Dircord: ElderBR#5398     |\n" +
                "+---------------------------+");

        // Iniciando o config padrão dos YML
        saveDefaultConfig();
        config = new Config();

        // Lendo e criando os itens
        Item.CreateItem();// Criando todos os itens

        // Tradução
        traducaoConfig = new TraducaoConfig();

        // Salvando os item no arquivo item.yml
        itemConfig = new ItemConfig();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}