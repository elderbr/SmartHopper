package mc.elderbr.smarthopper.cmd;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
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

    private Grupo grupo;
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

            if (command.getDsGrupo().equalsIgnoreCase("grupo")) {

                grupo = null;
                if (cmd.length() > 0) {
                    try {
                        grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                    } catch (NumberFormatException e) {
                        grupo = VGlobal.GRUPO_MAP_NAME.get(VGlobal.GRUPO_MAP.get(cmd));
                    }
                    if (grupo != null) {

                        grupo.setDsLang(player);
                        inventoryCustom = new InventoryCustom();
                        inventoryCustom.create(grupo.getDsTraducao().concat(Msg.Color(" $lID:$r"+ grupo.getCdGrupo())));
                        for (Item items : grupo.getListItem()) {
                            inventoryCustom.addItem(items.getItemStack());
                        }
                        // SE FOR ADM OU OPERADOR ADICIONA O BOTÃO PARA SALVAR OU ALTERAR
                        if (Config.ADM_LIST.contains(player.getDsGrupo()) || Config.OPERADOR_LIST.contains(player.getDsGrupo())) {
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
                        player.sendMessage(Msg.Color("$2Grupo: $6" + grupo.getDsTraducao() + " $e$lID: " + grupo.getCdGrupo()));
                    } else {
                        player.sendMessage(Msg.Color("$2O grupo $e" + cmd + " $6não existe!"));
                    }
                } else {

                    if (VGlobal.GRUPO_LIST_ITEM.get(Utils.ToItemStack(itemStack)) == null) {
                        player.sendMessage(Msg.Color("$2Não existe grupo para o item $e" + Utils.ToItemStack(itemStack) + "!"));
                    } else {
                        player.sendMessage("=====================================================");
                        for (String grups : VGlobal.GRUPO_LIST_ITEM.get(Utils.ToItemStack(itemStack))) {
                            grupo = VGlobal.GRUPO_MAP_NAME.get(grups);
                            grupo.setDsLang(player);
                            player.sendMessage(Msg.Color("$2Grupo: $6" + grupo.getDsTraducao() + " $e$lID: " + grupo.getCdGrupo()));
                        }
                        player.sendMessage("=====================================================");
                    }
                }

            }

            /************  ADICIONA NOVO GRUPO     ************/
            if (command.getDsGrupo().contentEquals("addgrupo")) {

                if (Config.ADM_LIST.contains(player.getDsGrupo()) || Config.OPERADOR_LIST.contains(player.getDsGrupo())) {
                    if (cmd.length() > 4) {
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
            if (command.getDsGrupo().contentEquals("removegrupo")) {
                if (Config.ADM_LIST.contains(player.getDsGrupo()) || Config.OPERADOR_LIST.contains(player.getDsGrupo())) {
                    if (cmd.length() > 0) {
                        try{
                            grupo = VGlobal.GRUPO_MAP_ID.get(Integer.parseInt(cmd));
                        }catch (NumberFormatException e) {
                            grupo = VGlobal.GRUPO_MAP_NAME.get(cmd);
                        }
                        if (grupo != null) {
                            grupo.setDsLang(player);
                            new GrupoConfig(grupo, Grupo.DELETE);
                            player.sendMessage(Msg.Color("$eO grupo " + grupo.getDsTraducao() + " apagado com sucesso!"));
                            return true;
                        }
                    }else{
                        player.sendMessage("Escreva o nome do grupo que deseja apagar!!!\n/removegrupo <grupo>");
                        return false;
                    }
                }
            }

        }
        return false;
    }
}
