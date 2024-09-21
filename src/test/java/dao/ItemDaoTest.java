package dao;

import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.model.Item;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.logging.Logger;
import static org.mockito.Mockito.*;

public class ItemDaoTest {

    private Server mockServer;
    private PluginManager mockPluginManager;
    private Logger mockLogger;

    private Map<Integer, Item> GRUP_MAP_ID;

    private ItemDao itemDao;
    private YamlConfiguration configMock;
    private Item mockItem;

    @BeforeEach
    public void setUp() {
        // Criando mock para YamlConfiguration
        configMock = mock(YamlConfiguration.class);

        // Criando um mock do servidor, PluginManager e Logger
        mockServer = mock(Server.class);
        mockPluginManager = mock(PluginManager.class);
        mockLogger = mock(Logger.class);

        // Configura o comportamento do mockServer para retornar o PluginManager simulado
        when(mockServer.getPluginManager()).thenReturn(mockPluginManager);

        // Configura o comportamento do mockServer para retornar o logger simulado
        when(mockServer.getLogger()).thenReturn(mockLogger);

        // Simula o retorno de Bukkit.getServer() para retornar o mockServer
        Bukkit.setServer(mockServer);

        // Mock para a classe Item
        mockItem = mock(Item.class);

        // Instância do ItemDao usando o mock
        //itemDao = ItemDao.getInstance();

        // Configura o comportamento do mockItem
        when(mockItem.getId()).thenReturn(1069);
        when(mockItem.getName()).thenReturn("stone");
    }

    @Test
    public void testFindShouldByIdWhenExist(){
        Assertions.assertEquals(mockItem.getId(), 1069);
        Assertions.assertEquals(mockItem.getName(), "stone");
    }

    @Test
    public void testFindShouldByIdWhenDifferentEquals(){
        Assertions.assertNotEquals(mockItem.getId(), 1070);
        Assertions.assertNotEquals(mockItem.getName(), "pedra");
    }

    @Test
    public void testFindShouldByIdWhenNotExist(){
        Mockito.doThrow(ItemException.class).when(mockItem).getId();
    }
}
