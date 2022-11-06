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
        }

        if (itemObj instanceof Integer id) {
            codigo = id;
        }

        if (itemObj instanceof ItemStack itemStack) {

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


            for (Grupo grup : GRUPO_LIST) {
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
        grupo.setBloqueado(name.contains("#"));
        listGrupo.add(grupo);
        return listGrupo;
    }

    public List<Grupo> getListGrupo() {
        return listGrupo;
    }

    public boolean delete(Player player) throws GrupoException {

        if (!Config.CONTAINS_ADD(player)) {
            throw new GrupoException("Ops, você não é adm do Smart Hopper!!!");
        }

        if (grupo == null) {
            throw new GrupoException("Digite o nome ou ID do grupo!!!");
        }
        if (GrupoConfig.DELETE(grupo)) {
            GRUPO_LIST.remove(grupo);
            GRUPO_MAP_ID.remove(grupo.getCodigo());
            GRUPO_MAP_NAME.remove(grupo.getName());
            TRADUCAO_GRUPO.remove(grupo.getName().toLowerCase());
            TRADUCAO_GRUPO.remove(grupo.getName());
            return true;
        }
        return false;
    }
}
