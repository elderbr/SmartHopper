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

    public Grupo getGrupo(int id) throws GrupoException {
        if (id < 1) {
            throw new GrupoException("$6O código do grupo não é valido!!!");
        }
        return GRUPO_MAP_ID.get(id);
    }

    public Grupo getGrupo(@NotNull String name) throws GrupoException {
        // SE A STRING ESTIVER VAZIA
        if (name.isEmpty()) {
            throw new GrupoException("$6Digite o nome do grupo ou ID!!!");
        }

        // Tenta converte o nome em Inteiro se consegui busca o número do ID no banco dos grupos
        try {
            id = Integer.parseInt(name.toLowerCase().replaceAll("[^0-9]",""));
            return GRUPO_MAP_ID.get(id);// Buscando o número do ID no banco dos grupos
        }catch (NumberFormatException e){}

        // Verifica se contém o item pelo o nome buscando o nome e a tradução
        if(!findName(name)){
            throw new GrupoException("O grupo pesquisado não existe!");
        }
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

    public boolean isContains(String name){
        for(Map.Entry<String, Grupo> grupName : TRADUCAO_GRUPO.entrySet()){
            if(name.toLowerCase().equalsIgnoreCase(grupName.getKey().toLowerCase())){
                grupo = grupName.getValue();
                return true;
            }
        }
        return false;
    }

    public boolean findName(String name){
        for(Map.Entry<String, Grupo> grup : GRUPO_MAP_NAME.entrySet()){
            grupo = grup.getValue();
            if(name.toLowerCase().equalsIgnoreCase(grup.getKey())){
                return true;
            }
            if(grup.getValue().getTranslation().size()>0) {
                if (grupo.getTranslation().containsValue(name.toLowerCase())) {
                    return true;
                }
            }
        }
        grupo = null;
        return false;
    }
}