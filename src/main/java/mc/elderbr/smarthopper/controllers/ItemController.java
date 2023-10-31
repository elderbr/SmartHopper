package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.ItemCreate;
import mc.elderbr.smarthopper.model.LivroEncantado;
import mc.elderbr.smarthopper.model.Pocao;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class ItemController {

    private int id;
    private String name;
    private Item item;
    private ItemDao itemDao = new ItemDao();
    private Pocao pocao;
    private LivroEncantado livro;

    public ItemController() {
    }

    public Item save(Item item) throws ItemException {

        if (item == null) {
            throw new ItemException("Item invalido!!!");
        }
        if (item.getName().isBlank()) {
            throw new ItemException("Digite o nome do item!!!");
        }
        if (ITEM_MAP_NAME.get(item.getName().toLowerCase()) != null) {
            throw new ItemException("O item já existe!!!");
        }
        item.setId(ID());
        itemDao.save(item);

        return ITEM_MAP_NAME.get(item.getName().toLowerCase());
    }

    public Item save(ItemStack itemStack) throws ItemException {
        if (itemStack == null || itemStack.getType().isAir() || !itemStack.getType().isItem()) {
            throw new ItemException("Item invalido!!!");
        }
        Item item = new Item(itemStack);
        if (ITEM_MAP_NAME.get(item.getName().toLowerCase()) != null) {
            throw new ItemException("O item já existe!!!");
        }
        item.setId(ID());
        itemDao.save(item);
        return item;
    }

    public Item findByID(int id) throws ItemException {
        if (id < 1) {
            throw new ItemException("ID do invalido!!!");
        }
        Item item = ITEM_MAP_ID.get(id);
        if (item == null) {
            throw new ItemException("O item não existe!!!");
        }
        return item;
    }

    public Item findByName(String name) throws ItemException {
        if (name.isBlank()) {
            throw new ItemException("Nome do item invalido!!!");
        }
        Item item = ITEM_MAP_NAME.get(name);
        if (item == null) {
            throw new ItemException("O item não existe!!!");
        }
        return item;
    }

    public Item findByItem(Item item) throws ItemException {
        if (item == null || item.getName().isBlank()) {
            throw new ItemException("Item invalido!!!");
        }
        Item newItem = ITEM_MAP_NAME.get(item.getName().toLowerCase());
        if (newItem == null) {
            throw new ItemException("O item não existe!!!");
        }
        return item;
    }

    public Item findByItemStack(ItemStack itemStack) throws ItemException {
        if (itemStack == null || itemStack.getType().isAir()) {
            throw new ItemException("Item invalido!!!");
        }
        switch (itemStack.getType()){
            case ENCHANTED_BOOK:
                return LivroEncantado.getItem(itemStack);
            case POTION:
            case SPLASH_POTION:
            case LINGERING_POTION:
                return Pocao.getItem(itemStack);
            default:
                item = findByName(Item.ToName(itemStack));
        }
        if (item == null) {
            throw new ItemException(String.format("O item %s não está na lista!!!", Item.ToName(itemStack)));
        }
        return item;
    }

    public static void findAll() {
        clean();// Remove todos os texto que contenha "item_"
        ItemDao dao = new ItemDao();
        dao.findAll();// Carrega todos os itens na variavel global
        for (String name : ItemCreate.Create()) {
            try {
                if (ITEM_MAP_NAME.get(name) == null) {
                    Item item = new Item();
                    item.setId(ID());
                    item.setName(name);
                    dao.save(item);
                    Msg.ServidorGreen("$2Criando o item $e"+ name);
                }
            } catch (ItemException e) {
                Msg.ServidorErro(e, "findAll()", ItemController.class);
            }
        }
    }

    public ItemStack parseItemStack(Item item) throws ItemException {
        if(item == null){
            throw new ItemException("Segure o item na mão ou digite o nome o ID do item!!!");
        }
        if(item.getName().contains("enchanted book")){
            return ENCHANTEMENT_BOOK_MAP.get(item.getName());
        }
        if(item.getName().contains("potion")){
            return POTION_MAP.get(item.getName());
        }
        ItemStack itemStack = new ItemStack(item.parseItemStack());
        if(itemStack == null) {
            throw new ItemException("Segure o item na mão ou digite o nome o ID do item!!!");
        }
        return itemStack;
    }

    public Item update(Item item) throws ItemException {
        String name = item.getName().toLowerCase();
        try {
            if (item == null) {
                throw new ItemException("Item invalido!!!");
            }
            if (item.getName().isBlank()) {
                throw new ItemException("Digite o nome do item!!!");
            }
            if (ITEM_MAP_NAME.get(name) == null) {
                throw new ItemException("O item não existe!!!");
            }
            item.setId(ITEM_MAP_NAME.get(name).getId());
            itemDao.update(item);
        } catch (ItemException e) {
            throw new ItemException("Erro ao atualizar o item!!!");
        }
        return ITEM_MAP_NAME.get(name);
    }

    public void delete(Item item) throws ItemException {
        if (item == null || item.getName().isBlank()) {
            throw new ItemException("Item invalido!!!");
        }
        Item newItem = ITEM_MAP_NAME.get(item.getName().toLowerCase());
        if (newItem == null) {
            throw new ItemException("Item invalido!!!");
        }
        itemDao.delete(item);
    }

    public static void Create() {
        ItemDao dao = new ItemDao();
        ItemCreate.Create();
        int id = 1;
        for (String name : ITEM_NAME_LIST_DEFAULT) {
            Msg.ServidorGreen("item: " + name, ItemController.class);
            try {
                Item item = new Item();
                item.setId(id);
                item.setName(name);
                dao.save(item);
                id++;
            } catch (ItemException e) {
                Msg.ServidorErro(e, "Create()", ItemController.class);
            }
        }
    }

    public Item getItem() {
        return item;
    }

    public Item getItem(@NotNull Object obj) throws ItemException {

        if (obj instanceof ItemStack itemStack) {

            if (itemStack.getType().isAir() || itemStack.getType() == Material.AIR) {
                throw new ItemException("Digite o nome ou ID do item, ou segura um item na mão!!");
            }
            ItemMeta meta = itemStack.getItemMeta();
            switch (itemStack.getType()) {
                case ENCHANTED_BOOK:
                    return LivroEncantado.getItem(itemStack);
                case POTION:
                case SPLASH_POTION:
                case LINGERING_POTION:
                    return Pocao.getItem(itemStack);
                default:
                    name = Item.ToName(itemStack);
            }
            item = ITEM_MAP_NAME.get(name);
        } else if (obj instanceof String itemName) {
            name = itemName.toLowerCase();

            if (name.isBlank()) {
                throw new ItemException("Digite o nome do item ou ID!!!");
            }
            try {
                id = Integer.parseInt(name.replaceAll("[^0-9]", ""));
                item = ITEM_MAP_ID.get(id);
            } catch (NumberFormatException e) {
                item = ITEM_MAP_NAME.get(name.toLowerCase());
            }
            if (item == null) {
                item = findByTraducao(name);
            }
        }
        if (item == null) {
            throw new ItemException("O item " + obj + " não existe!!!");
        }
        return item;
    }

    public static ItemStack ParseItemStack(@NotNull Item item) {
        ItemStack itemStack = null;
        String name = item.getName();
        if (name.contains("potion")) {
            String potion = null;
            if (name.contains("lingering")) {
                potion = name.replaceAll("lingering potion", "").trim();
                itemStack = new ItemStack(Material.LINGERING_POTION);
            } else if (name.contains("splash")) {
                potion = name.replaceAll("splash potion", "").trim();
                itemStack = new ItemStack(Material.SPLASH_POTION);
            } else {
                potion = name.replaceAll("potion", "").trim();
                itemStack = new ItemStack(Material.POTION);
            }

            if (!potion.isEmpty()) {
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.valueOf(potion.replaceAll("\\s", "_").toUpperCase())));
                itemStack.setItemMeta(meta);
            }
            return itemStack;
        }
        if (name.contains("enchanted book")) {
            itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            //
            String encantamento = item.getName().replaceAll("enchanted book", "").trim();
            NamespacedKey key = null;
            for (Enchantment enchantment : Enchantment.values()) {
                String nameEnch = enchantment.getKey().getKey().replaceAll("_", " ").toLowerCase();
                if (encantamento.equalsIgnoreCase(nameEnch)) {
                    key = enchantment.getKey();
                }
            }
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            meta.addStoredEnchant(Enchantment.getByKey(key), 1, true);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        return new ItemStack(Material.valueOf(name.replaceAll("\\s", "_").toUpperCase()));
    }

    public boolean addTranslation(@NotNull Player player, @NotNull String[] args) throws ItemException {

        // Verifica se existe o código do item e a tradução
        if (args.length < 2) {
            throw new ItemException("Digite /traducaoitem <código do item> <tradução>!!!");
        }

        // Verifica se o jogar é administrador do SmartHopper
        if (!player.isOp() && !AdmController.ContainsAdm(player)) {
            throw new ItemException("Você não tem permissão para adicionar a tradução ao item!!!");
        }

        // Pegando a tradução
        String translation = convertTranslation(args);
        // A tradução precisa conter mais do 2 letras
        if (translation.length() < 3) {
            throw new ItemException("O tradução precisa conter mais do que 2 letras!!!");
        }

        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new ItemException("O código " + args[0] + " não é valido!!!");
        }
        item = ITEM_MAP_ID.get(id);
        if (item == null) {
            throw new ItemException("O código " + args[0] + " não está na lista de item!!!");
        }
        item.addTranslation(player.getLocale(), translation);
        return itemDao.update(item);
    }

    private String convertTranslation(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i].concat(" "));
        }
        return sb.toString().trim();
    }

    // Pega o último ID adicionando
    private static int ID() {
        int id = 0;
        for (Item item : ITEM_MAP_NAME.values()) {
            if (id < item.getId()) {
                id = item.getId();
            }
        }
        id++;
        return id;
    }

    private Item findByTraducao(String name) {
        for (Item items : ITEM_MAP_NAME.values()) {
            for (Map.Entry<String, String> langs : items.getTranslation().entrySet()) {
                if (name.equalsIgnoreCase(langs.getValue())) {
                    return items;
                }
            }
        }
        return null;
    }

    private static void clean() {
        List<String> list = new ArrayList<>();
        String txt = null;
        if (ITEM_FILE.exists()) {
            try (BufferedReader reader = Files.newBufferedReader(ITEM_FILE.toPath(), StandardCharsets.UTF_8)) {
                while ((txt = reader.readLine()) != null) {
                    list.add(txt.replaceAll("item_", "").replaceAll("[\']", ""));
                }
            } catch (IOException e) {
                Msg.ServidorErro(e, "clean()", ItemController.class);
            }
            if (list.size() > 0) {
                try (BufferedWriter writer = Files.newBufferedWriter(ITEM_FILE.toPath(), StandardCharsets.UTF_8)) {
                    for (String arg : list) {
                        writer.write(arg);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    Msg.ServidorErro(e, "clean()", ItemController.class);
                }
            }
        }
    }
}
