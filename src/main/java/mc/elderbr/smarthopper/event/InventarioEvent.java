package mc.elderbr.smarthopper.event;

import mc.elderbr.smarthopper.controllers.AdmController;
import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.SmartHopper;
import mc.elderbr.smarthopper.factories.InventoryFactory;
import mc.elderbr.smarthopper.interfaces.Botao;
import mc.elderbr.smarthopper.interfaces.IItem;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.InventoryCustom;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventarioEvent implements Listener, Botao, VGlobal {

    private Player player;
    private String titleHopper = "";

    private String titleInventory;
    private InventoryCustom inventoryCustom;
    private Inventory inventory;
    private InventoryClickEvent event;
    private InventoryFactory inventoryFactory = InventoryFactory.getInstance();

    private ItemStack itemClicked;
    private ItemStack itemHopper;
    private List<ItemStack> listItemStick = new ArrayList<>();

    private List<IItem> listItem = new ArrayList<>();

    private Grupo grupo;
    private GrupoController grupoCtrl = new GrupoController();

    private SmartHopper smartHopper;

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
        player = (Player) event.getWhoClicked();
        this.event = event;
        inventory = event.getView().getTopInventory();
        titleInventory = event.getView().getTitle();

        try {
            if (Objects.isNull(event.getCurrentItem()) || event.getCurrentItem().getType() == Material.AIR) return;
            itemClicked = event.getCurrentItem();// Pega o item clicado

            // Verificar se o inventário aberto é o do Smart Hopper
            if (containsTitle(titleInventory)) {
                event.setCancelled(true);// Cancela o movimento do item

                // Verifica se o inventário é do tipo funil do Smart Hopper
                if (titleInventory.equals(NAME_RECIPE)) {

                    // Cancela o movimento do item se o botão for igual ao botão de bloqueio
                    if (equalButton(itemClicked) && itemClicked.equals(BtnBlocked())) return;

                    // Verifica se o item clicado é o botão de salvar
                    if (itemClicked.equals(BtnSalva())) {

                        // Pegando dos itens do inventário
                        ItemMeta meta = itemHopper.getItemMeta();
                        // Verifica se lista é vazia
                        if (listItem.isEmpty()) {
                            titleHopper = TITLE_RECIPE;
                        } else {
                            titleHopper = listItem.stream().map(IItem::getIdCodeConfig).collect(Collectors.joining(";"));
                        }
                        meta.setDisplayName(titleHopper);// Alterando o nome da configuração do funil
                        itemHopper.setItemMeta(meta);
                        player.closeInventory();// Fecha o inventário
                        Msg.PlayerGold(player, "Funil configurado com sucesso!");
                        return;
                    }
                    smartHopper = new SmartHopper(itemClicked);
                    IItem item = smartHopper.getTypes().get(0);// Busca no banco de dados o item clicado
                    // Verifica se o item já existe no inventário
                    // Verifica se o item clicado já foi adicionado no inventory
                    // Se o item já existe no inventário, remove o item do inventário
                    boolean contains = false;
                    for (ItemStack itemStack : inventory.getContents()) {
                        if (Objects.isNull(itemStack) || itemStack.getType() == Material.AIR) continue;

                        smartHopper = new SmartHopper(itemStack);
                        if (smartHopper.getTypes().isEmpty()) continue;
                        IItem newItemIv = smartHopper.getTypes().get(0);
                        if (newItemIv.equals(item)) {
                            inventory.removeItem(itemStack);
                            listItem.remove(item);
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        // Adiciona o item no inventário
                        inventory.addItem(item.getItemStackWithMeta());
                        listItem.add(item);
                    }
                    titleHopper = listItem.stream().map(IItem::getIdCodeConfig).collect(Collectors.joining(";"));

                    // Verifica se o titulo do funil é maior que 50 caracteres
                    if (titleHopper.length() > 50) {
                        Msg.PlayerRed(player, "Ops, a configuração para o funil é muito grande, remove algum item!");
                    }
                    return;
                }
                inventoryCustom = new InventoryCustom(event);
                inventoryCustom.btnNavegation(event);// Evento que navega entre os itens do grupo
                grupo = inventoryCustom.getGrupo();
                if (Objects.nonNull(grupo)) {
                    // Evento que navega entre os itens do grupo
                    if (inventoryCustom.btnNavegation(event)) {
                        return;
                    }

                    // Verifica se o player é Adm do servidor ou do Smart Hopper
                    if (!player.isOp() && !AdmController.ContainsAdm(player)) return;
                    add();// Adicionando item ao grupo
                    remove();// Removendo item do grupo
                    save();// Salvando o grupo
                }
            }
        } catch (Exception e) {
            Msg.PlayerRed(player, e.getMessage());
            Msg.ServidorErro(e, "Erro ao clicar no inventário", getClass());
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        // verifica se o click foi com o botão direito
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            this.player = event.getPlayer();
            this.inventory = player.getInventory();

            ItemStack itemAir = player.getItemOnCursor();// Pegando item que o jogador está segurando
            this.itemHopper = player.getInventory().getItemInMainHand();// Pegando item que o jogador está clicando

            // Se o jogador não estiver segurando nada ou o item for ar, ou o item que ele está clicando for ar, retorna
            if (Objects.isNull(itemHopper) || itemHopper.getType().isAir() || !itemAir.getType().isAir()) return;

            // Verifica se o inventário é do tipo funil
            if (itemHopper.getType() == Material.HOPPER) {
                // Verifica se o jogador está segurando um item contém lore
                if (Objects.isNull(itemHopper.getItemMeta())
                        || Objects.isNull(itemHopper.getItemMeta().getLore())
                        || itemHopper.getItemMeta().getLore().isEmpty()) {
                    return;
                }
                // Verifica se o lore contém o nome da receita
                List<String> lore = itemHopper.getItemMeta().getLore();
                if (lore.contains(NAME_RECIPE)) {
                    listItem = new ArrayList<>();
                    listItemStick = new ArrayList<>();
                    ItemMeta meta = itemHopper.getItemMeta();
                    String name = meta.getDisplayName();

                    inventory = inventoryFactory.InventoryConfigurationHopper();
                    smartHopper = new SmartHopper(name);
                    for (IItem sm : smartHopper.getTypes()) {
                        if (sm instanceof Item item) {
                            listItem.add(item);
                            inventory.addItem(item.getItemStackWithMeta());
                        }
                        if (sm instanceof Grupo grupo) {
                            listItem.add(grupo);
                            inventory.addItem(grupo.getItemStackWithMeta());
                        }
                    }
                    player.openInventory(inventory);
                }
            }
        }
    }

    private void add() {
        // Se o clique for com o botão direito do mouse
        if (event.isLeftClick()) {
            // Se o botão for igual ao botões personalizados
            if (equalButton(itemClicked)) {
                return;
            }
            ItemStack newItem = new ItemStack(itemClicked.getType());
            newItem.setAmount(1);

            // Adiciona item no grupo
            if (!grupo.containsItem(newItem)) {
                grupo.addItems(newItem);
                listItemStick.add(newItem);
                // Verifica se o item está na lista do inventário
                inventory.addItem(newItem);// Adicionando o item no inventário aberto
            }
        }
    }

    private void remove() {
        // Se for clicado com o botão direito do mouse no item
        if (event.isRightClick()) {

            if (equalButton(itemClicked)) return;

            // Verificando se o item está na lista do inventário
            ItemStack newItem = new ItemStack(itemClicked.getType());
            if (inventory.contains(newItem)) {
                inventory.removeItem(newItem);
            }
            if (grupo.containsItem(itemClicked)) {
                grupo.removeItems(itemClicked);
            }
        }
    }

    private void save() {
        try {
            // Verificar se foi clicado com o botão esquerdo do mouse no botão salvar(save)
            if (event.isLeftClick() && itemClicked.equals(BtnSalva())) {
                String msg = "Ops, algo deu errado!!!";
                // Verifica se o jogador é ADM
                if (!player.isOp() && !AdmController.ContainsAdm(player)) return;

                if (grupo.getId() < 1) {
                    grupo.addTranslation(player, grupo.getName());
                    grupo.toTranslation(player);
                    for (ItemStack itemStack : listItemStick) {
                        grupo.addItems(itemStack);
                    }
                    if (grupoCtrl.save(grupo)) {
                        msg = String.format("$eO jogador $c%s $eadicionou um novo grupo $c%s$e!!!", player.getName(), grupo.getName());
                    }
                } else {
                    grupoCtrl.update(grupo);// Atualizando o grupo
                    msg = String.format("$eO jogador $c%s $ealterou o grupo $c%s$e!!!", player.getName(), grupo.getName());
                }
                player.closeInventory();// Fechando o inventário do jogador

                // Envia mensagem para todos os jogadores online
                Msg.PlayerTodos(msg);
                grupo = null;
                listItemStick.clear();
            }
        } catch (Exception e) {
            Msg.PlayerRed(player, e.getMessage());
        }
    }
}