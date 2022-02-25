package mc.elderbr.smarthopper;

import mc.elderbr.smarthopper.cmd.GrupoComando;
import mc.elderbr.smarthopper.cmd.ItemComando;
import mc.elderbr.smarthopper.dao.*;
import mc.elderbr.smarthopper.event.AnvilCreate;
import mc.elderbr.smarthopper.event.ClickHopper;
import mc.elderbr.smarthopper.event.InventarioEvent;
import mc.elderbr.smarthopper.event.MoveHopper;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoFile;
import mc.elderbr.smarthopper.file.ItemFile;
import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.recipes.HopperRecipe;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSmartHopper extends JavaPlugin implements Listener {

    // CONFIGS
    private Config config;
    private ItemFile itemFile;
    private GrupoFile grupoFile;

    private AdmDao admDao;
    private ItemDao itemDao;
    private GrupoDao grupoDao;
    private LangDao langDao;
    private TraducaoDao traducaoDao;
    private TraducaoConfig traducaoConfig;
    private CargoDao cargoDao;


    @Override
    public void onEnable() {

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN +
                "\n+--------------------------+\n" +
                "| Smart Hopper             |\n" +
                "| Version " + VGlobal.VERSION + "            |\n" +
                "| Dircord: ElderBR#5398    |\n" +
                "+--------------------------+");
        // CARREGANDO A LISTA DE MATERIAS DO JOGO
        for (Material m : Material.values()) {
            if (!m.isAir() && new ItemStack(m) != null) {
                VGlobal.LIST_MATERIAL.add(m);
            }
        }

        // Iniciando o config padrão dos YML
        saveDefaultConfig();
        config = new Config();
        itemFile = new ItemFile();
        grupoFile = new GrupoFile();
        Config.setVersion();// ATUALIZA A VERSÃO DO PLUGIN


        admDao = new AdmDao();
        itemDao = new ItemDao();
        itemDao.create();
        grupoDao = new GrupoDao();
        langDao = new LangDao();
        traducaoDao = new TraducaoDao();
        traducaoConfig = new TraducaoConfig();
        cargoDao = new CargoDao();


        // ADICIONANDO OS EVENTOS
        getServer().getPluginManager().registerEvents(new MoveHopper(), this);
        getServer().getPluginManager().registerEvents(new ClickHopper(), this);
        getServer().getPluginManager().registerEvents(new InventarioEvent(), this);
        getServer().getPluginManager().registerEvents(new AnvilCreate(), this);// ABRE O ANVIL BIGORNA

        // Comandos
        getCommand("item").setExecutor(new ItemComando());
        getCommand("traducaoItem").setExecutor(new ItemComando());

        // COMANDO PARA O GRUPO
        getCommand("grupo").setExecutor(new GrupoComando());
        getCommand("addgrupo").setExecutor(new GrupoComando());
        getCommand("traducaoGrupo").setExecutor(new GrupoComando());
        getCommand("removegrupo").setExecutor(new GrupoComando());

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
