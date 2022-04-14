package mc.elderbr.smarthopper;

import mc.elderbr.smarthopper.cmd.*;
import mc.elderbr.smarthopper.dao.*;
import mc.elderbr.smarthopper.event.AnvilCreate;
import mc.elderbr.smarthopper.event.ClickHopper;
import mc.elderbr.smarthopper.event.InventarioEvent;
import mc.elderbr.smarthopper.event.MoveHopper;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.recipes.HopperRecipe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSmartHopper extends JavaPlugin implements Listener {

    private Config config;
    private ItemConfig itemConfig;
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

        // CRIANDO TODAS AS TABELAS NO BANCO DE DADOS
        Conexao.CREATE_TABLES();

        // LANGS
        LangDao.INSERT_DEFAULT();
        LangDao.SELECT_ALL();// LANGS CARREGA A LISTA DE LANGS DO BANCO DE DADOS

        // Tradução dos itens
        traducaoConfig = new TraducaoConfig();

        // ARQUIVO ITEM.YML
        itemConfig = new ItemConfig();
        itemConfig.loadYmlAddBanco();

        grupoConfig = new GrupoConfig();

        // ADM E OPERADORES
        AdmDao.SELECT_ALL();// PEGA A LISTA DE ADM E OPERADORES

        // CRIANDO ITENS E SALVANDO NO BANCO
        //Item.CreateItem();// CRIANDO ITENS
        //ItemDao.CreateDefault();// SALVANDO NO BANCO
        ItemDao.selectAll();// CARREGA TODOS OS ITENS DO BANCO E ADICIONA NO OBJETO GLOBAL
        //itemConfig.updateYML();

        // TRADUÇÃO
        //TraducaoDao.createBR();
        //TraducaoDao.createPT();
        TraducaoDao.SELECT_ALL();

        // GRUPO
        // Verifica se a versão do plugin e inferior a 4.0.0 se for ler o arquivo grupo.yml e salva no banco de dados
        //grupoConfig.loadYmlAddBanco();
        // Cria grupos atraves do nome dos itens
        Grupo.CreateGrupos();
        // Salva os grupos no banco de dados
        GrupoDao.CREATE_GRUPO();
        // Ler todos os grupos salvos no banco
        GrupoDao.SELECT_ALL();
        //grupoConfig.updateYML();

        config.SET_VERSION(VGlobal.VERSION);// ALTERA A VERSÃO DO PLUGIN NO CONFIG

        // ADICIONANDO OS EVENTOS
        getServer().getPluginManager().registerEvents(new MoveHopper(), this);
        getServer().getPluginManager().registerEvents(new ClickHopper(), this);
        getServer().getPluginManager().registerEvents(new InventarioEvent(), this);
        getServer().getPluginManager().registerEvents(new AnvilCreate(), this);// ABRE O ANVIL BIGORNA

        // Comandos
        getCommand("item").setExecutor(new ItemComando());
        getCommand("item").setTabCompleter(new ItemTabCompleter());

        // GRUPO
        getCommand("grupo").setExecutor(new GrupoComando());
        getCommand("grupo").setTabCompleter(new GrupoTabCompleter());

        // ADICIONAR GRUPO
        getCommand("addgrupo").setExecutor(new GrupoComando());
        getCommand("addgrupo").setTabCompleter(new GrupoTabCompleter());

        // REMOVE O GRUPO
        getCommand("removegrupo").setExecutor(new GrupoComando());
        getCommand("removegrupo").setTabCompleter(new GrupoTabCompleter());

        // ADICIONA OU REMOVER ADMINISTRADOR
        getCommand("addAdm").setExecutor(new AdminstradorComando());
        getCommand("addAdm").setTabCompleter(new AdminstradorTabCompleter());
        getCommand("removerAdm").setExecutor(new AdminstradorComando());
        getCommand("removerAdm").setTabCompleter(new AdminstradorTabCompleter());

        // TRADUÇÃO DO ITEM OU GRUPO
        getCommand("addTraducao").setExecutor(new TraducaoComando());
        getCommand("addTraducao").setTabCompleter(new TraducaoTabCompleter());

        getCommand("livro").setExecutor(new LivroComando());

        // ADICIONANDO NOVAS RECEITAS
        HopperRecipe recipes = new HopperRecipe();
        getServer().addRecipe(recipes.createSmartHopper());// SMARTHOPPER PERSONALIZADO
        getServer().addRecipe(recipes.createSmartHopper1());// SMARTHOPPER ANTIGO PARA O ATUAL
        getServer().addRecipe(recipes.createSmartHopper3());// CRIA 3 SMARTHOPPER

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
