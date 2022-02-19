package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Lang;
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

import java.util.ArrayList;
import java.util.List;

public class InventarioEvent implements Listener {

    private String titulo;
    private Player player;
    private Lang lang;
    private ItemStack itemStack;
    private ItemStack itemNovo;

    // ITEM
    private Item item;

    // GRUPO
    private String nameGrupo;
    private Grupo grupo;
    private GrupoDao grupoDao = new GrupoDao();
    private List<String> lore;

    private Inventory inventory;

    private Adm adm;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {

        // Player
        player = (Player) event.getWhoClicked();// Pega o player que está manipulando o inventario
        lang = VGlobal.LANG_NAME_MAP.get(player.getLocale());
        adm = new Adm(player);

        inventory = player.getOpenInventory().getTopInventory();
        itemStack = event.getCurrentItem();

        titulo = event.getView().getTitle();// NOME DO INVENTARIO

        // VERIFICA SE O ITEM É DIFERENTE DE NULL, SE O INVENTARIO É CHEST E SE O PLAYER É ADM OU OPERADOR
        if (itemStack == null || itemStack.getType() == Material.AIR || inventory.getType() != InventoryType.CHEST) {
            return;
        }

        // SE O INVENTARIO RECEBER O NOME DE NOVO GRUPO OU GRUPO CANCELA A MOVIMENTAÇÃO DO ITEM
        if (titulo.contains(Msg.Color(VGlobal.GRUPO_NOVO_INVENTORY)) || titulo.contains(Msg.Color(VGlobal.GRUPO_INVENTORY))) {
            event.setCancelled(true);

            // SE O PLAYER É O ADM OU OPERADOR
            if (VGlobal.ADM_UUID.contains(adm.getDsUuid())) {
                // SE O ITEM CONTEM LORE
                lore = new ArrayList<>();
                if (itemStack != null && itemStack.getItemMeta().getLore() != null) {
                    lore = itemStack.getItemMeta().getLore();
                }

                // SE CLICADO COM O BOTÃO ESQUERDO ADICIONA ITEM
                if (event.isLeftClick() && !inventory.contains(itemStack)) {
                    event.getInventory().addItem(itemStack);
                }
                // SE CLICADO COM O BOTÃO DIREITO REMOVE ITEM
                if (event.isRightClick() && inventory.contains(itemStack) && !lore.contains(VGlobal.GRUPO_SALVA) && !lore.contains(VGlobal.GRUPO_UPDATE)) {
                    event.getInventory().removeItem(itemStack);
                }
                // SE CLICADO NA LÃ SALVAR ADICIONA NO BANCO O NOVO GRUPO
                if (event.isLeftClick() && lore.contains(VGlobal.GRUPO_SALVA)) {
                    grupo = new Grupo();
                    grupo.setDsGrupo(titulo.replace(VGlobal.GRUPO_NOVO_INVENTORY, ""));
                    // LANG
                    grupo.setCdLang(lang.getCdLang());
                    grupo.setDsLang(lang.getDsLang());

                    player.closeInventory();// FECHA INVENTARIO

                    int cdGrupo = grupoDao.insert(grupo);
                    if (cdGrupo > 0) {
                        // REMOVENDO O BOTÃO LÃ
                        grupo.setCdGrupo(cdGrupo);
                        inventory.remove(inventory.getItem(53));
                        for (ItemStack items : inventory.getStorageContents()) {
                            if (items == null) continue;
                            // ADICIONANDO O ITEM AO GRUPO
                            itemNovo = new ItemStack(items.getType(), 1);
                            item = VGlobal.ITEM_NAME_MAP.get(new Item(itemNovo).toString());
                            grupo.addItem(item);
                            // ADICIONANDO O ITEM DO GRUPO NO BANCO
                            grupoDao.insertItem(grupo, item);
                        }
                        Msg.PlayerGold(player, "Grupo salvo com sucesso!!!");
                    } else {
                        Msg.PlayerGold(player, "Erro ao criar novo grupo!!!");
                    }
                }
                if (event.isLeftClick() && lore.contains(VGlobal.GRUPO_UPDATE)) {
                    nameGrupo = titulo.replaceAll(VGlobal.GRUPO_INVENTORY, "");
                    grupo = new Grupo();
                    grupo.setDsGrupo(nameGrupo);

                    grupo = grupoDao.select(grupo);
                    if (grupo != null) {

                        inventory.removeItem(inventory.getItem(53));

                        grupoDao.deleteItem(grupo);
                        for (ItemStack items : inventory.getStorageContents()) {
                            if (items == null) continue;
                            itemNovo = new ItemStack(items.getType(), 1);
                            grupoDao.insertItem(grupo, VGlobal.ITEM_NAME_MAP.get(Utils.toItem(itemNovo)));
                        }
                    }
                    player.closeInventory();
                    Msg.PlayerGold(player, "Grupo atualizado com sucesso!!!");

                }

            }

        }


    }
}
