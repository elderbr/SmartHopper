package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.cmd.GrupoComando;
import mc.elderbr.smarthopper.enums.InventarioType;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.Botao;
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

public class InventarioEvent implements Listener, Botao {

    private String titulo;
    private Player player;
    private ItemStack itemStack;
    private ItemStack itemClick;
    private int pag = 1;

    // GRUPO
    private Grupo grupo;
    private Inventory inventory;

    private InventoryCustom inventoryCustom;

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
        if (!titulo.contains(":") && !titulo.contains("Grupo")) {
            event.setCancelled(false);
            return;
        }

        inventoryCustom = new InventoryCustom(player);
        inventoryCustom.create(titulo);

        itemClick = event.getCurrentItem().clone();// ITEM CLICADO
        itemClick.setAmount(1);
        itemStack = new ItemStack(itemClick.getType());// PEGANDO O TIPO DE ITEM QUE FOI CLICADO
        itemStack.setAmount(1);

        // SE O INVENTARIO ABERTO É UM GRUPO

        event.setCancelled(true);

        if (inventoryCustom.isAdm()) {
            if (event.isLeftClick()) {
                if (itemClick != null && itemClick.equals(BtnSalva())) {
                    inventory.removeItem(BtnSalva());// Remove o botão salvar antes de passar pelo os item
                    if(inventory.isEmpty()){
                        Msg.PlayerGold(player,"Adicione item para o grupo!!!");
                        inventory.setItem(53, BtnSalva());
                        return;
                    }

                    if (inventoryCustom.getType() == InventarioType.NOVO) {

                        grupo = inventoryCustom.getGrupo();
                        grupo.setName(titulo.replaceAll(Msg.Color("$lGrupo: $r"), ""));
                        grupo.setCodigo(VGlobal.CD_MAX.get(0) + 1);// Adicionando novo código do grupo
                        grupo.addTraducao(player.getLocale(), Utils.ToUTF(grupo.getName()));// Adicionando tradução do grupo

                        // Percorrendo todos os itens do inventario
                        for (ItemStack itemStack : inventory.getContents()) {
                            if (itemStack != null) {
                                grupo.addList(new Item(itemStack).parse().getName());
                            }
                        }
                        player.closeInventory();

                        if (GrupoConfig.ADD(grupo)) {
                            // Adicionando na variavel global
                            VGlobal.GRUPO_LIST.add(grupo);
                            VGlobal.GRUPO_MAP_ID.put(grupo.getCodigo(), grupo);
                            VGlobal.GRUPO_MAP_NAME.put(grupo.getName().toLowerCase(), grupo);
                            VGlobal.TRADUCAO_GRUPO.put(grupo.getName().toLowerCase(), grupo);

                            Msg.PlayerTodos("$e$lNovo grupo $6$l" + grupo.getName() + "$e$l criado por $2$l" + player.getName() + "$e$l!!!");
                        } else {
                            Msg.PlayerRed(player, "Erro ao adicionar o grupo");
                        }
                        return;
                    } else {

                        grupo = VGlobal.GRUPO_MAP_ID.get(inventoryCustom.getCodigo());
                        grupo.getListItem().clear();
                        inventory.removeItem(BtnSalva());// Remove o botão salvar antes de passar pelo os item
                        // Percorrendo todos os itens do inventario
                        for (ItemStack itemStack : inventory.getContents()) {
                            if (itemStack != null) {
                                grupo.addList(new Item(itemStack).parse().getName());
                            }
                        }
                        player.closeInventory();
                        if (GrupoConfig.UPDATE(grupo)) {
                            Msg.PlayerTodos("$eAlterado grupo $6" + grupo.getName() + "$e pelo $2" + player.getName() + "$e!!!");
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
            if (itemClick != null && itemClick.equals(BtnAnterior())) {
                pag--;
                if (pag < 1) {
                    pag = 1;
                }
                inventory.clear();
                int position = 0;
                for (ItemStack itemStack : inventoryCustom.getInventory(pag)) {
                    if (itemStack != null) {
                        inventory.setItem(position, itemStack);
                    }
                    position++;
                }
                inventory.setItem(53, BtnProximo());
            }
            if (itemClick != null && itemClick.equals(BtnProximo())) {
                pag++;
                inventory.clear();
                for (ItemStack itemStack : inventoryCustom.getInventory(pag)) {
                    if (itemStack != null) {
                        inventory.addItem(itemStack);
                    }
                }
                if (pag == 2) {
                    if (Config.CONTAINS_ADD(player.getName())) {
                        inventory.setItem(52, BtnAnterior());
                        inventory.setItem(53, BtnSalva());
                    } else {
                        inventory.setItem(53, BtnAnterior());
                    }
                }
            }
        }
    }
}