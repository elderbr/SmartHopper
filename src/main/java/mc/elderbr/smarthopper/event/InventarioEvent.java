package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.cmd.GrupoComando;
import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventarioEvent implements Listener {

    private String titulo;
    private Player player;
    private ItemStack itemStack;

    // Navegador
    private ItemStack itemBtnSalve;
    private ItemStack itemBtnAnt;
    private ItemStack itemBtnProx;
    private ItemStack itemClick;
    private int pag = 1;

    // GRUPO
    private int codigo;
    private String name;
    private String type;
    private Grupo grupo;
    private Inventory inventory;

    private InventoryCustom inventoryCustom;

    private Grupo grupoNovo;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        inventory = player.getOpenInventory().getTopInventory();

        // VERIFICA SE O ITEM É DIFERENTE DE NULL, SE O INVENTARIO É CHEST E SE O PLAYER É ADM OU OPERADOR
        if (event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR
                || inventory.getType() != InventoryType.CHEST
        ) {
            return;
        }

        titulo = event.getView().getTitle();// NOME DO INVENTARIO
        if (!titulo.contains(":") && !titulo.contains("Grupo")) return;

        inventoryCustom = GrupoComando.INVENTORY;

        itemClick = event.getCurrentItem();// ITEM CLICADO
        itemClick.setAmount(1);
        itemStack = new ItemStack(itemClick.getType());// PEGANDO O TIPO DE ITEM QUE FOI CLICADO

        // Navegador
        itemBtnSalve = player.getOpenInventory().getItem(53);// BOTÃO DE SALVAR OU ATUALIZAR VAI SER A LÃ DA COR LIMÃO COM LORE
        itemBtnAnt = player.getOpenInventory().getItem(52);
        itemBtnProx = player.getOpenInventory().getItem(53);

        // SE O INVENTARIO ABERTO É UM GRUPO
        if (inventoryCustom != null && inventoryCustom.getType() != null) {
            event.setCancelled(true);

            if (inventoryCustom.isAdm()) {
                if (event.isLeftClick()) {
                    if (itemBtnSalve != null
                            && itemBtnSalve.equals(itemClick)
                            && itemBtnSalve.getItemMeta().getDisplayName().equals("§2§lSalva")
                    ) {
                        if (inventoryCustom.getType() == InventarioType.NOVO) {
                            grupo = new Grupo();
                            grupo.setName(inventoryCustom.getName());
                            grupo.setCodigo(VGlobal.CD_MAX.get(0) + 1);// Adicionando novo código do grupo
                            grupo.addTraducao(player.getLocale(), Utils.ToUTF(grupo.getName()));// Adicionando tradução do grupo
                            inventory.removeItem(itemBtnSalve);// Remove o botão salvar antes de passar pelo os item
                            // Percorrendo todos os itens do inventario
                            for (ItemStack itemStack : inventory.getContents()) {
                                if (itemStack != null) {
                                    grupo.addList(new Item(itemStack).getName());
                                }
                            }
                            player.closeInventory();
                            if (GrupoConfig.ADD(grupo)) {
                                Msg.PlayerTodos("§e§lNovo grupo §6§l" + grupo.getName() + "§e§l criado por §d§l" + player.getName() + "§e§l!!!");
                            } else {
                                Msg.PlayerRed(player, "Erro ao adicionar o grupo");
                            }
                        } else {
                            grupo = VGlobal.GRUPO_MAP_NAME.get(inventoryCustom.getName());
                            grupo.getListItem().clear();
                            inventory.removeItem(itemBtnSalve);// Remove o botão salvar antes de passar pelo os item
                            // Percorrendo todos os itens do inventario
                            for (ItemStack itemStack : inventory.getContents()) {
                                if (itemStack != null) {
                                    grupo.addList(new Item(itemStack).getName());
                                }
                            }
                            player.closeInventory();
                            if (GrupoConfig.UPDATE(grupo)) {
                                Msg.PlayerTodos("§eAlterado grupo §6" + grupo.getName() + "§e pelo §d" + player.getName() + "§e!!!");
                            } else {
                                Msg.PlayerRed(player, "Erro ao alterar o grupo");
                            }
                        }
                        return;
                    }
                    if (!inventory.contains(itemClick)) {
                        inventory.addItem(itemClick);
                    }
                }

                if (event.isRightClick() && inventory.contains(itemClick)) {
                    if (itemClick.getType() != Material.BARRIER) {
                        inventory.removeItem(itemClick);
                    }
                }
            }
            if (event.isLeftClick()) {
                if (itemBtnAnt != null && itemBtnAnt.getItemMeta().getDisplayName().equalsIgnoreCase("§9§lRetorna")) {
                    pag--;
                    inventory.clear();
                    for (ItemStack itemStack : inventoryCustom.getInventory(pag)) {
                        if (itemStack != null) {
                            inventory.addItem(itemStack);
                        }
                    }
                }
                if (itemBtnProx != null && itemBtnProx.getItemMeta().getDisplayName().equalsIgnoreCase("§9§lPróximo")) {
                    pag++;
                    inventory.clear();
                    for (ItemStack itemStack : inventoryCustom.getInventory(pag)) {
                        if (itemStack != null) {
                            inventory.addItem(itemStack);
                        }
                    }
                }
            }

        } else {
            event.setCancelled(false);
        }

    }
}