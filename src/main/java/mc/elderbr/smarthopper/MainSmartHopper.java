package mc.elderbr.smarthopper;


import mc.elderbr.smarthopper.cmd.*;
import mc.elderbr.smarthopper.event.ClickHopper;
import mc.elderbr.smarthopper.event.InventarioEvent;
import mc.elderbr.smarthopper.event.MoveHopper;
import mc.elderbr.smarthopper.event.TextureEvent;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
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

        // Lendo e criando os grupos
        Grupo.CreateGrupos();// Criando todos os grupos

        // Tradução
        traducaoConfig = new TraducaoConfig();

        // Salvando os item no arquivo item.yml
        itemConfig = new ItemConfig();

        // Salvando os grupo no arquivo grupo.yml
        grupoConfig = new GrupoConfig();

        // Comandos
        commands();

        // Eventos
        events();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void commands() {

        getCommand("item").setExecutor(new ItemComando());
        getCommand("item").setTabCompleter(new ItemTabCompleter());

        // GRUPOS
        getCommand("grupo").setExecutor(new GrupoComando());
        getCommand("grupo").setTabCompleter(new GrupoTabCompleter());
        getCommand("addgrupo").setExecutor(new GrupoComando());
        getCommand("removegrupo").setExecutor(new GrupoComando());

        // TRADUÇÃO
        getCommand("traducaoitem").setExecutor(new TraducaoComando());
        getCommand("traducaogrupo").setExecutor(new TraducaoComando());

        // CONFIGURAÇÃO
        getCommand("addadm").setExecutor(new AdministradorComando());
    }

    private void events(){
        getServer().getPluginManager().registerEvents(new InventarioEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickHopper(), this);
        getServer().getPluginManager().registerEvents(new MoveHopper(), this);
        getServer().getPluginManager().registerEvents(new TextureEvent(), this);
    }


}