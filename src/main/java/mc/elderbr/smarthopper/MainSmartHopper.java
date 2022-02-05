package mc.elderbr.smarthopper;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.dao.LangDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.event.AnvilCreate;
import mc.elderbr.smarthopper.event.ClickHopper;
import mc.elderbr.smarthopper.event.InventarioEvent;
import mc.elderbr.smarthopper.event.MoveHopper;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.recipes.HopperRecipe;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSmartHopper extends JavaPlugin implements Listener {

    private ItemDao itemDao;
    private GrupoDao grupoDao;
    private LangDao langDao;
    private TraducaoDao traducaoDao;

    @Override
    public void onEnable() {

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN +
                "\n+------------------------+\n" +
                "| Smart Hopper           |\n" +
                "| Version " + VGlobal.VERSION + "            |\n" +
                "| Dircord: ElderBR#5398  |\n" +
                "+------------------------+");

        // Iniciando o config padrão dos YML
        saveDefaultConfig();

        itemDao = new ItemDao();
        grupoDao = new GrupoDao();
        langDao = new LangDao();
        traducaoDao = new TraducaoDao();

        Msg.ServidorGreen("finalizado");


        // ADICIONANDO OS EVENTOS
        getServer().getPluginManager().registerEvents(new MoveHopper(), this);
        getServer().getPluginManager().registerEvents(new ClickHopper(), this);
        getServer().getPluginManager().registerEvents(new InventarioEvent(), this);
        getServer().getPluginManager().registerEvents(new AnvilCreate(), this);// ABRE O ANVIL BIGORNA

        // Comandos

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
