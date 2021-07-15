package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.SmartHopper;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClickHopper implements Listener {

    private Player player;
    private ItemStack itemStack;
    private Block block;
    private SmartHopper smartHopper;
    private InventoryCustom inventory;
    private Grupo grupo;
    private String nameGrupo;
    private ItemStack itemSalve;
    private ItemMeta meta;
    private List<String> lore;
    private InventoryCustom inventoryCustom;

    @EventHandler
    public void clickHopper(PlayerInteractEvent event) {

        player = event.getPlayer();
        itemStack = player.getInventory().getItemInMainHand();

        // RETORN SE O BLOCO CLICADO FOR DIFERENTE DO HOPPER OU SE
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.HOPPER) {
            return;
        }

        block = event.getClickedBlock();
        smartHopper = new SmartHopper(block);

        // SE FOR CLICADO NO HOPPER COM GRAVETO NA MÃO
        if (itemStack.getType() == Material.STICK && event.getAction() == Action.LEFT_CLICK_BLOCK) {

            // VERIFICA SE EXISTE MAIS DE UM ITEM OU GRUPO SETADO NO HOPPER
            if (smartHopper.getName().contains(";")) {
                for (String hopperName : smartHopper.getName().split(";")) {
                    if (Utils.getHopperType(hopperName) instanceof Item) {
                        if (hopperName.contains("#")) {
                            Msg.ItemNegar(player, (Item) Utils.getHopperType(hopperName));
                        }else {
                            Msg.Item(player, (Item) Utils.getHopperType(hopperName));
                        }
                    }
                    if (Utils.getHopperType(hopperName) instanceof Grupo) {
                        if (hopperName.contains("#")) {
                            Msg.GrupoNegar(player, (Grupo) Utils.getHopperType(hopperName));
                        } else {
                            Msg.Grupo(player, (Grupo) Utils.getHopperType(hopperName));
                        }
                    }
                }
            }

            // SE O HOPPER FOR DO TIPO ITEM
            if (smartHopper.getType() instanceof Item) {
                if (smartHopper.getName().contains("#")) {
                    Msg.ItemNegar(player, smartHopper.getItem());
                }else {
                    Msg.Item(player, smartHopper.getItem());
                }
                return;
            }

            // SE O HOPPER FOR DO TIPO GRUPO
            if (smartHopper.getType() instanceof Grupo) {

                grupo = smartHopper.getGrupo();// PEGA O ITEM

                // CRIANDO O INVENTARIO DO GRUPO
                inventory = new InventoryCustom();
                inventory.create(grupo.getTraducao(player.getLocale()).concat(" §e§lID:"+grupo.getID()));// NO DO INVENTARIO
                for (Item items : grupo.getItemList()) {// ADICIONANDO OS ITENS NO INVENTARIO
                    inventory.addItem(items.getType());
                }

                // VERIFICA SE O JOGAR É OPERADOR
                if (Config.ADM_LIST.contains(player.getName()) || Config.OPERADOR_LIST.contains(player.getName())) {
                    // CRIANDO O BOTÃO PARA SALVAR
                    itemSalve = new ItemStack(Material.LIME_WOOL);
                    meta = itemSalve.getItemMeta();
                    meta.setDisplayName("§aSalva");
                    lore = new ArrayList<>();
                    lore.add("§3Salvar e atualiza o grupo");
                    meta.setLore(lore);
                    itemSalve.setItemMeta(meta);
                    inventory.getInventory().setItem(53, itemSalve);
                }
                player.openInventory(inventory.getInventory());
                if (smartHopper.getName().contains("#")) {
                    Msg.GrupoNegar(player, grupo);
                } else {
                    Msg.Grupo(player, grupo);
                }
                return;
            }
            // SE O HOPPER NÃO ESTIVER CONFIGURADO
            if (smartHopper.getName().equals("HOPPER")) {
                player.sendMessage("§6O hopper §4não §6configurado!!!");
            }
        }
    }
}
