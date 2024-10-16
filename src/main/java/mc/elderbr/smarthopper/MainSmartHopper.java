package mc.elderbr.smarthopper;


import mc.elderbr.smarthopper.cmd.*;
import mc.elderbr.smarthopper.controllers.ConfigController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.event.*;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSmartHopper extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN +
                "\n+---------------------------+\n" +
                "| Smart Hopper              |\n" +
                "| Version " + VGlobal.VERSION + "             |\n" +
                "| Discord: ElderBR#5398     |\n" +
                "+---------------------------+");

        // Iniciando o config padrão dos YML
        saveDefaultConfig();

        ItemController.findAll();// Busca todos os item e salva na variavel global
        GrupoController.findAll();// Busca todos os grupos e salva na variavel global

        // Tradução
        new TraducaoConfig();

        // Verifica se os dados das configurações estão atualizados
        ConfigController.RESET();

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

        // ITEM
        getCommand("item").setExecutor(new ItemComando());
        getCommand("item").setTabCompleter(new ItemTabCompleter());

        // GRUPOS
        getCommand("grupo").setExecutor(new GrupoComando());
        getCommand("grupo").setTabCompleter(new GrupoTabCompleter());
        getCommand("addgrupo").setExecutor(new GrupoComando());
        getCommand("addgrupo").setTabCompleter(new GrupoTabCompleter());
        getCommand("removegrupo").setExecutor(new GrupoComando());
        getCommand("removegrupo").setTabCompleter(new GrupoTabCompleter());
        getCommand("gruponame").setExecutor(new GrupoComando());// Criação dos nomes dos grupos para tradução

        // TRADUÇÃO
        getCommand("traducaoitem").setExecutor(new TraducaoComando());
        getCommand("traducaoitem").setTabCompleter(new TraducaoTabCompleter());
        getCommand("traducaogrupo").setExecutor(new TraducaoComando());
        getCommand("traducaogrupo").setTabCompleter(new TraducaoTabCompleter());
        getCommand("traducaoreload").setExecutor(new TraducaoComando());

        // CONFIGURAÇÃO
        getCommand("reload").setExecutor(new AdministradorComando());
        getCommand("addadm").setExecutor(new AdministradorComando());
        getCommand("addadm").setTabCompleter(new AdministradorTabCompleter());
        getCommand("removeradm").setExecutor(new AdministradorComando());
        getCommand("removeradm").setTabCompleter(new AdministradorTabCompleter());

        getCommand("tutorial").setExecutor(new InformacaoComando());

        // Comando para deixa a textura obrigatoria
        getCommand("useTexture").setExecutor(new TextureComando());
    }

    private void events() {
        getServer().getPluginManager().registerEvents(new InventarioEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickHopper(), this);
        getServer().getPluginManager().registerEvents(new MoveHopperEvent(), this);
        getServer().getPluginManager().registerEvents(new PickupItemEvent(), this);
        getServer().getPluginManager().registerEvents(new TextureEvent(), this);
    }
}