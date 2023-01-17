package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.ItemConfig;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.LivroEncantado;
import mc.elderbr.smarthopper.model.Pocao;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class ItemController {

    private int id;
    private String name;
    private Item item;
    private Pocao pocao;
    private LivroEncantado livro;

    public ItemController() {
    }

    public Item getItem() {
        return item;
    }

    public Item getItem(@NotNull Object obj) throws ItemException {
        id = 0;
        name = null;
        if (obj instanceof Integer number) {
            id = number;
        } else if (obj instanceof String nameItem) {
            try {
                id = Integer.parseInt(nameItem);
            } catch (NumberFormatException e) {
                name = nameItem;
            }
        } else if (obj instanceof ItemStack itemStack) {
            switch (itemStack.getType()) {
                case POTION:
                case SPLASH_POTION:
                case LINGERING_POTION:
                    pocao = new Pocao(itemStack);
                    name = pocao.getPotion();
                    break;
                case ENCHANTED_BOOK:
                    livro = new LivroEncantado(itemStack);
                    name = livro.getBook();
                    break;
                default:
                    name = Item.ToName(itemStack);
            }

        }

        if (id > 0) {
            item = ITEM_MAP_ID.get(id);
        } else {
            item = ITEM_MAP_NAME.get(name);
            if (item == null) {
                item = TRADUCAO_ITEM.get(name);
            }
        }
        if (item == null) {
            throw new ItemException("Não foi possivél encontrar o item, digite o nome ou ID do item, ou segure um item na mão!!!");
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
        return new ItemStack(Material.valueOf(name.replaceAll("\\s","_").toUpperCase()));
    }

    public boolean addTranslation(@NotNull Player player, @NotNull String[] args) throws ItemException {

        // Verifica se o jogar é administrador do SmartHopper
        if (!Config.CONTAINS_ADD(player)) {
            throw new ItemException("Você não tem permissão para adicionar a tradução ao item!!!");
        }

        // Verifica se existe o código do item e a tradução
        if (args.length < 2) {
            throw new ItemException("Digite /traducaoitem <código do item> <tradução>!!!");
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
        TRADUCAO_ITEM.put(translation, item);
        return ItemConfig.ADD_TRADUCAO(item);
    }

    private String convertTranslation(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i].concat(" "));
        }
        return sb.toString().trim();
    }
}
