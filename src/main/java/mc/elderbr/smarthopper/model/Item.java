package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.abstracts.Funil;
import mc.elderbr.smarthopper.exceptions.ItemException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class Item extends Funil {

    private int id = 0;
    private String name;
    private boolean blocked = false;
    private List<Grupo> listGrupo = new ArrayList<>();


    public Item() {
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(ItemStack itemStack) {
        name = toName(itemStack);
    }

    @Override
    public Funil setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Funil setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Funil setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    public Item addListGrupo(Grupo grupo) {
        listGrupo.add(grupo);
        return this;
    }

    public Item removeListGrupo(Grupo grupo) {
        listGrupo.remove(grupo);
        return this;
    }

    public List<Grupo> getListGrupo() {
        return listGrupo;
    }

    /*************************************************************
     *
     *              Metodo que criar os itens
     *
     **************************************************************/
    public static void CreateItem() {
        for (Material m : Material.values()) {
            ItemStack itemStack = new ItemStack(m);
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType().isItem()) {
                Item item = new Item(itemStack);

                // Se item não pode está na lista
                if(item.getName().equalsIgnoreCase("enchanted book")) continue;

                if (!ITEM_NAME_LIST.contains(item.getName())) {
                    ITEM_NAME_LIST.add(item.getName());
                }
            }
        }

        Pocao.Create();// POÇÕES E SEU EFEITOS
        LivroEncantado.Create();// LIVRO ENCANTADOS

        Collections.sort(ITEM_NAME_LIST);// Organizando em ordem alfabetica

        // Criando itens com código e nome e adicionando na lista global
        int cod = 1;
        for (String name : ITEM_NAME_LIST) {
            Item item = new Item(cod, name);
            SET(item);// Atualiza ou adiciona o item no atributo global
            cod++;
        }
    }

    public String toName(@NotNull ItemStack itemStack) {
        return itemStack.getType().getKey().getKey().toLowerCase().replaceAll("_", " ");
    }

    public static String ToName(@NotNull ItemStack itemStack) {
        return itemStack.getType().getKey().getKey().toLowerCase().replaceAll("_", " ");
    }

    /***
     * Adiciona ou atualiza os atributos globais do item
     * @param item
     */
    public static void SET(@NotNull Item item) {
        ITEM_MAP_ID.put(item.getId(), item);
        ITEM_MAP_NAME.put(item.getName(), item);
        if (!ITEM_LIST.contains(item)) {
            ITEM_LIST.add(item);
        }
        if (!ITEM_NAME_LIST.contains(item.getName())) {
            ITEM_NAME_LIST.add(item.getName());
        }
    }

    /***
     * Busca o item no atributo global pelo o nome
     * @param name do item que seja pesquisar
     * @return Item
     * @throws ItemException
     */
    public static Item GET(@NotNull String name) throws ItemException {
        if (ITEM_MAP_NAME.get(name) == null) {
            throw new ItemException("O item não está na lista de item!!!");
        }
        return ITEM_MAP_NAME.get(name);
    }

    /**
     * Busca o item no atributo global buscando pelo ID
     *
     * @param id número do item
     * @return Item
     * @throws ItemException
     */
    public static Item GET(@NotNull Integer id) throws ItemException {
        if (ITEM_MAP_ID.get(id) == null) {
            throw new ItemException("O item não está na lista de item!!!");
        }
        return ITEM_MAP_ID.get(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public String toInfor() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blocked=" + blocked +
                ", listGrupo=" + listGrupo +
                '}';
    }
}