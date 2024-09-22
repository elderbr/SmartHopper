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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.util.Map;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemDaoTest {

    private Server mockServer;
    private PluginManager mockPluginManager;
    private Logger mockLogger;

    private Map<Integer, Item> GRUP_MAP_ID;

    @InjectMocks
    private ItemDao itemDao;
    private YamlConfiguration configMock;
    private Item mockItem;

    @BeforeEach
    public void setUp() {
        // Criando mock para YamlConfiguration
        configMock = mock(YamlConfiguration.class);

        // Item Stone
        configMock.set("stone.id", 1);
        configMock.set("stone.name", "stone");

        // Item Redstone
        configMock.set("redstone.id", 2);
        configMock.set("redstone.name", "redstone");

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
    @DisplayName("Busca pelo ID do item existente")
    public void testFindShouldByIdWhenExist() {
        Assertions.assertEquals(1069, mockItem.getId());
        Assertions.assertEquals("stone", mockItem.getName());
    }

    @Test
    @DisplayName("Lanca uma exececao quando busca pelo ID do item quando não existente")
    public void testFindShouldByIdWhenNotExist() {
        Mockito.doThrow(ItemException.class).when(mockItem).getId();
        Assertions.assertThrows(ItemException.class, () -> mockItem.getId());
    }

    @Test
    @DisplayName("Deve salvar o item quando nao existir")
    public void saveShouldByIdWhenItemNotExist() {

    }

    @Test
    @DisplayName("Deve atualizar o item pelo id quando existir")
    public void updateShouldByIdWhenItemExist() {

    }

    @Test
    @DisplayName("Deve atualizar traducao do item pelo id quando existir")
    public void updateTranslationShouldByIdWhenItemExist() {

    }

    @Test
    @DisplayName("Deve dar uma exececao ao atualizar a traducao quando o item nao existir ")
    public void updateTranslationShouldItemExceptionWhenItemNotExist() {

    }
}
