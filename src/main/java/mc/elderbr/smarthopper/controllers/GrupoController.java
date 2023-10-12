package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.LivroEncantado;
import mc.elderbr.smarthopper.model.Pocao;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrupoController implements VGlobal {

    private int id;
    private String name;
    private Grupo grupo;
    private List<Grupo> listGrupo;
    private Item item;

    public GrupoController() {
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public List<Grupo> getGrupo(ItemStack itemStack) throws GrupoException {
        listGrupo = new ArrayList<>();
        if (itemStack.getType() == Material.AIR) {
            throw new GrupoException("$cSegure o item na mão ou digite o nome ou o código do grupo!!!");
        }

        name = Item.ToName(itemStack);

        if (name.contains("potion")) {
            Pocao pocao = new Pocao(itemStack);
            item = pocao.getItem();
        } else if (name.contains("enchanted book")) {
            LivroEncantado livroEncantado = new LivroEncantado(itemStack);
            item = livroEncantado.getItem();
        } else {
            item = ITEM_MAP_NAME.get(name);
        }
        listGrupo = item.getListGrupo();
        return listGrupo;
    }

    public List<Grupo> getListGrupo() {
        return listGrupo;
    }

    public boolean addTraducao(@NotNull Player player, @NotNull String[] args) throws GrupoException {
        // Verifica se o jogador é o administrador do SmartHopper
        if (!Config.CONTAINS_ADD(player)) {
            throw new GrupoException("Ops, você não é adm do Smart Hopper!!!");
        }

        // Verifica se existe o ID do grupo e a tradução
        if (args.length < 2) {
            throw new GrupoException("Digite /addtraducao [código do grupo] [tradução]!!!");
        }

        // Pegando a tradução do grupo
        String traducao = convertTraducao(args);
        // A tradução precisa conter pelo menos 3 caracteres
        if (traducao.length() < 3) {
            throw new GrupoException("A tradução não pode ser menor que 3 caracteres!!!");
        }

        // Buscando o grupo pelo código
        id = 0;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new GrupoException(String.format("O código %s não é valido!!!", args[0]));
        }
        grupo = GRUPO_MAP_ID.get(id);
        if (grupo == null) {
            throw new GrupoException(String.format("O código %s não está na lista de grupos!!!", args[0]));
        }

        grupo.addTranslation(player.getLocale(), traducao);
        return GrupoConfig.ADD_TRADUCAO(grupo);
    }

    private String convertTraducao(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i].concat(" "));
        }
        return sb.toString().trim();
    }

    public boolean delete(Player player) throws GrupoException {

        if (!player.isOp() && !Config.CONTAINS_ADD(player)) {
            throw new GrupoException("Ops, você não é adm do Smart Hopper!!!");
        }

        if (grupo == null) {
            throw new GrupoException("Digite o nome ou ID do grupo!!!");
        }
        if (GrupoConfig.DELETE(grupo)) {
            TRADUCAO_GRUPO.remove(grupo.getName().toLowerCase());
            TRADUCAO_GRUPO.remove(grupo.getName());
            return true;
        }
        return false;
    }

    public Grupo findName(@NotNull String name) throws GrupoException {
        // Buscando o grupo pelo o código
        try {
            int codigo = Integer.parseInt(name.replaceAll("[^0-9]", ""));
            if (codigo < 1) {
                throw new GrupoException("O código do grupo é invalido!!!");
            }
            return grupo = GRUPO_MAP_ID.get(codigo);
        } catch (NumberFormatException e) {
        }

        // Buscando o grupo pelo o nome
        for(String nameGrup : GRUPO_NAME_LIST){
            if(nameGrup.equalsIgnoreCase(name)){
                return TRADUCAO_GRUPO.get(nameGrup);
            }
        }
        grupo = null;
        return grupo;
    }

    public Grupo findId(@NotNull int id) throws GrupoException {
        if (id < 1) {
            throw new GrupoException("$6O código do grupo não é valido!!!");
        }
        return GRUPO_MAP_ID.get(id);
    }

    public Grupo findItem(@NotNull Item item) throws GrupoException {
        return findName(item.getName());
    }

    public Grupo findItemStack(@NotNull ItemStack itemStack) throws GrupoException {
        return findName(new Item(itemStack).getName());
    }

    public List<String> findAll() {
        return GRUPO_NAME_LIST;
    }

    public List<String> findNameContains(String name) {
        List<String> list = new ArrayList<>();
        for (String grup : GRUPO_NAME_LIST) {
            if (grup.contains(name) || grup.equalsIgnoreCase(name)) {
                if (!list.contains(grup)) {
                    list.add(grup);
                }
            }
        }
        return list;
    }
}