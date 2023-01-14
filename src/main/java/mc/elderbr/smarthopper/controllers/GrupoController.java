package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.LivroEncantado;
import mc.elderbr.smarthopper.model.Pocao;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static mc.elderbr.smarthopper.interfaces.VGlobal.*;

public class GrupoController {

    private int codigo;
    private String name;
    private String nameGrupo;
    private Grupo grupo;
    private List<Grupo> listGrupo;
    private Item item;

    public GrupoController() {
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public List<Grupo> getGrupo(@NotNull Object itemObj) throws GrupoException {
        listGrupo = new ArrayList<>();
        if (itemObj instanceof String nameItem) {
            if (nameItem.isEmpty()) {
                throw new GrupoException("$6Digite o nome do grupo ou ID!!!");
            }
            name = nameItem;
            nameGrupo = nameItem.toLowerCase().replaceAll("[#g]", "");
            try {
                codigo = Integer.parseInt(nameGrupo);
            } catch (NumberFormatException e) {
            }
        } else if (itemObj instanceof Integer id) {
            codigo = id;
        } else if (itemObj instanceof ItemStack itemStack) {

            if (itemStack.getType() == Material.AIR) {
                throw new GrupoException("$cSegure o item na mão ou digite o código do grupo!!!");
            }

            name = Item.ToName(itemStack);
            item = ITEM_MAP_NAME.get(name);

            if (name.contains("potion")) {
                Pocao pocao = new Pocao(itemStack);
                item = pocao.getItem();
            }

            if (name.contains("enchanted book")) {
                LivroEncantado livroEncantado = new LivroEncantado(itemStack);
                item = livroEncantado.getItem();
            }


            for (Grupo grup : GRUPO_MAP_NAME.values()) {
                if (grup.isContains(item)) {
                    listGrupo.add(grup);
                }
            }
            grupo = listGrupo.get(0);
            return listGrupo;
        }

        if (codigo > 0) {
            grupo = GRUPO_MAP_ID.get(codigo);
        } else {
            grupo = TRADUCAO_GRUPO.get(nameGrupo);
        }

        if (grupo == null) {
            throw new GrupoException("$cO grupo$6 " + name + "$c não é valido!!!");
        }
        grupo.setBlocked(name.contains("#"));
        listGrupo.add(grupo);
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
        if(args.length < 2){
            throw new GrupoException("Digite /addtraducao [código do grupo] [tradução]!!!");
        }

        // Pegando a tradução do grupo
        String traducao = convertTraducao(args);
        // A tradução precisa conter pelo menos 3 caracteres
        if (traducao.length() < 3) {
            throw new GrupoException("A tradução não pode ser menor que 3 caracteres!!!");
        }

        // Buscando o grupo pelo código
        codigo = 0;
        try{
            codigo = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            throw new GrupoException(String.format("O código %s não é valido!!!", args[0]));
        }
        grupo = GRUPO_MAP_ID.get(codigo);
        if (grupo == null) {
            throw new GrupoException(String.format("O código %s não está na lista de grupos!!!", args[0]));
        }

        grupo.addTranslation(player.getLocale(), traducao);
        return GrupoConfig.ADD_TRADUCAO(grupo);
    }

    private String convertTraducao(String[] args){
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < args.length;i++){
            sb.append(args[i].concat(" "));
        }
        return sb.toString().trim();
    }

    public boolean delete(Player player) throws GrupoException {

        if (!Config.CONTAINS_ADD(player)) {
            throw new GrupoException("Ops, você não é adm do Smart Hopper!!!");
        }

        if (grupo == null) {
            throw new GrupoException("Digite o nome ou ID do grupo!!!");
        }
        if (GrupoConfig.DELETE(grupo)) {
            GRUPO_MAP_ID.remove(grupo.getId());
            GRUPO_MAP_NAME.remove(grupo.getName());
            TRADUCAO_GRUPO.remove(grupo.getName().toLowerCase());
            TRADUCAO_GRUPO.remove(grupo.getName());
            return true;
        }
        return false;
    }
}