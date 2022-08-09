package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GrupoComando implements CommandExecutor {

    private Player player;
    private String cmd;

    public static Grupo GRUPO;
    private List<Grupo> listGrupo;
    private ItemStack itemStack;
    private InventoryCustom inventory;
    private ItemStack itemSalve;
    private ItemMeta meta;
    private List<String> lore;


    private InventoryCustom inventoryCustom;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            player = (Player) sender;
            itemStack = player.getInventory().getItemInMainHand();
            cmd = Utils.NAME_ARRAY(args);

            if (command.getName().equalsIgnoreCase("grupo")) {

                GRUPO = null;
                if (cmd.length() > 0) {
                    try {
                        GRUPO = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        GRUPO = VGlobal.TRADUCAO_GRUPO_LIST.get(cmd);
                    }
                    if (GRUPO != null) {
                        GRUPO.setDsLang(player);

                        if (!GRUPO.getListItem().isEmpty()) {
                            inventoryCustom = new InventoryCustom();
                            inventoryCustom.create(GRUPO.toTraducao().concat(Msg.Color(" $lID:$r" + GRUPO.getCdGrupo())));
                            for (String itemName : GRUPO.getListItem()) {
                                Item item = new Item(itemName);
                                inventoryCustom.addItem(item.getItemStack());
                            }
                            // SE FOR ADM OU OPERADOR ADICIONA O BOTÃO PARA SALVAR OU ALTERAR
                            if (VGlobal.ADM_LIST.contains(player.getName())) {
                                // CRIANDO O BOTÃO PARA SALVAR
                                itemSalve = new ItemStack(Material.LIME_WOOL);
                                meta = itemSalve.getItemMeta();
                                meta.setDisplayName(Msg.Color("$aAtualizar"));
                                lore = new ArrayList<>();
                                lore.add(Msg.Color("$3Salvar"));
                                meta.setLore(lore);
                                itemSalve.setItemMeta(meta);
                                inventoryCustom.getInventory().setItem(53, itemSalve);
                            }
                            player.openInventory(inventoryCustom.getInventory());
                        }
                        player.sendMessage(Msg.Color("$2Grupo: $6" + GRUPO.toTraducao() + " $e$lID: " + GRUPO.getCdGrupo()));
                    } else {
                        player.sendMessage(Msg.Color("$2O grupo $e" + cmd + " $6não existe!"));
                    }
                } else {

                    if (itemStack.getType() == Material.AIR) {
                        Msg.PlayerRed(player, "Segure um item na mão ou escreva o nome do grupo ou seu ID!!!");
                        return false;
                    }

                    Item item = VGlobal.ITEM_MAP_NAME.get(new Item(itemStack).getDsItem());
                    item.setDsLang(player);

                    if (listGrupo.isEmpty()) {
                        player.sendMessage(Msg.Color("$2Não existe grupo para o item $e" + item.toTraducao() + "!"));
                        return false;
                    }

                    if (listGrupo.size() == 1) {
                        GRUPO = VGlobal.GRUPO_MAP_NAME.get(listGrupo.get(0).getDsGrupo());
                        GRUPO.setDsLang(player);
                        inventoryCustom = new InventoryCustom();
                        inventoryCustom.create(GRUPO.toTraducao().concat(Msg.Color(" $lID:$r" + GRUPO.getCdGrupo())));
                        for (String itemName : GRUPO.getListItem()) {
                            Item items = new Item(itemName);
                            inventoryCustom.addItem(items.getItemStack());
                        }

                        // SE FOR ADM OU OPERADOR ADICIONA O BOTÃO PARA SALVAR OU ALTERAR
                        if (VGlobal.ADM_LIST.contains(player.getName())) {
                            // CRIANDO O BOTÃO PARA SALVAR
                            itemSalve = new ItemStack(Material.LIME_WOOL);
                            meta = itemSalve.getItemMeta();
                            meta.setDisplayName(Msg.Color("$aAtualizar"));
                            lore = new ArrayList<>();
                            lore.add(Msg.Color("$3Salvar"));
                            meta.setLore(lore);
                            itemSalve.setItemMeta(meta);
                            inventoryCustom.getInventory().setItem(53, itemSalve);
                        }
                        player.openInventory(inventoryCustom.getInventory());
                        player.sendMessage(Msg.Color("$2Grupo: $6" + GRUPO.toTraducao() + " $e$lID: " + GRUPO.getCdGrupo()));
                        return false;
                    }

                    player.sendMessage("=====================================================");
                    for (Grupo grupo : listGrupo) {
                        grupo.setDsLang(player);
                        player.sendMessage(Msg.Color("$2Grupo: $6" + grupo.toTraducao() + " $e$lID: " + grupo.getCdGrupo()));
                    }
                    player.sendMessage("=====================================================");
                }

            }

            /************  ADICIONA NOVO GRUPO     ************/
            if (command.getName().contentEquals("addgrupo")) {

                if (VGlobal.ADM_LIST.contains(player.getName())) {
                    if (cmd.length() > 4) {

                        GRUPO = new Grupo();
                        GRUPO.setDsGrupo(cmd);
                        GRUPO.setDsTraducao(cmd);

                        inventory = new InventoryCustom();
                        inventory.createNewGrupo(cmd);

                        // CRIANDO O BOTÃO PARA SALVAR
                        itemSalve = new ItemStack(Material.LIME_WOOL);
                        meta = itemSalve.getItemMeta();
                        meta.setDisplayName(Msg.Color("$aSalva"));
                        lore = new ArrayList<>();
                        lore.add(Msg.Color("$3Salva novo grupo"));
                        meta.setLore(lore);
                        itemSalve.setItemMeta(meta);

                        inventory.getInventory().setItem(53, itemSalve);

                        player.openInventory(inventory.getInventory());
                    } else {
                        player.sendMessage(Msg.Color("$e$lO nome do grupo precisa ter 5 ou mais letras!!!"));
                    }
                } else {
                    player.sendMessage(Msg.Color("$6Você não tem permissão!!!"));
                }
            }

            /************  REMOVER GRUPO     ************/
            if (command.getName().contentEquals("removegrupo")) {
                if (VGlobal.ADM_LIST.contains(player.getName())) {
                    if (cmd.length() > 0) {
                        try {
                            GRUPO = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                        } catch (NumberFormatException e) {
                            GRUPO = VGlobal.GRUPO_MAP_NAME.get(cmd);
                        }
                        if (GRUPO != null) {


                                // REMOVENDO O GRUPO DAS VARIAVEIS GLOBAL
                                VGlobal.GRUPO_MAP_NAME.get(GRUPO.getDsGrupo());
                                VGlobal.GRUPO_MAP_ID.get(GRUPO.getCdGrupo());
                                VGlobal.GRUPO_MAP.get(GRUPO.getDsGrupo());
                                VGlobal.GRUPO_NAME_LIST.remove(GRUPO.getDsGrupo());

                                // TRADUÇÃO
                                VGlobal.TRADUCAO_GRUPO_LIST.remove(GRUPO.getDsGrupo());

                                Msg.PlayerTodos(Msg.Color("$6O jogador " + player.getName() + " deletou o grupo $a$l" + GRUPO.getDsGrupo() + "!"));

                        }else{
                            Msg.PlayerRed(player, "O grupo "+ cmd +" não existe!!!");
                        }
                        return false;
                    } else {
                        player.sendMessage("Escreva o nome do grupo que deseja apagar!!!\n/removegrupo <grupo>");
                        return false;
                    }
                }
            }

        }
        return false;
    }
}