package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.file.GrupoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.GrupoCreate;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class GrupoController implements VGlobal {

    private int id;
    private String name;
    private Grupo grupo;
    private GrupoDao grupoDao = new GrupoDao();
    private List<Grupo> listGrupo;
    private Item item;
    private ItemController itemCtrl = new ItemController();

    public GrupoController() {
    }

    public boolean save(Grupo grupo) throws GrupoException {
        // O grupo não pode nulo e precisa conter o nome
        if (grupo == null || grupo.getName().isBlank()) {
            throw new GrupoException("Digite o nome do grupo!!!");
        }
        // Verificar se existe item adicionado no grupo
        if (grupo.getListItem().isEmpty()) {
            throw new GrupoException("Adicione item no grupo para poder salvar!!!");
        }
        grupo.setId(ID());
        return grupoDao.save(grupo);
    }

    public Grupo findByName(@NotNull String name) throws GrupoException {
        // Buscando o grupo pelo o código
        try {
            int codigo = Integer.parseInt(name.replaceAll("[^0-9]", ""));
            if (codigo < 1) {
                throw new GrupoException("O código do grupo é invalido!!!");
            }
            grupo = GRUPO_MAP_ID.get(codigo);
        } catch (NumberFormatException e) {
            grupo = findNameTranslation(name);
        }
        return grupo;
    }

    public List<Grupo> findByItemStack(ItemStack itemStack) throws GrupoException {
        listGrupo = new ArrayList<>();
        // O item não pode nulo ou igual ao ar
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            throw new GrupoException("Segure o item na mão ou digite o nome ou o código do grupo!!!");
        }
        name = Item.ToName(itemStack);// Pegando o nome do item
        try {
            item = itemCtrl.findByItemStack(itemStack);// Busca o item na memoria global
        } catch (ItemException e) {
            throw new GrupoException(String.format("Não existe grupo para o item %s!!!", name));
        }
        listGrupo = item.getListGrupo();// Lista de grupo no item
        if (listGrupo.isEmpty()) {
            throw new GrupoException(String.format("Não existe grupo para o item %s!!!", name));
        }
        return listGrupo;
    }

    public Grupo findNameTranslation(String name) {
        grupo = GRUPO_MAP_NAME.get(name);
        if (grupo == null) {
            for (Grupo grup : GRUPO_MAP_NAME.values()) {
                for (String lang : grup.getTranslation().values()) {
                    if (lang.equalsIgnoreCase(name)) {
                        return grup;
                    }
                }
            }
        }
        return grupo;
    }

    public boolean update(Grupo grupo) throws GrupoException {
        if (grupo == null || grupo.getId() < 1 || grupo.getName() == null) {
            throw new GrupoException("Grupo invalido!!!");
        }
        this.grupo = GRUPO_MAP_NAME.get(grupo.getName().toLowerCase());
        if (this.grupo == null) {
            throw new GrupoException(String.format("O grupo %s não existe!!!", grupo.getName()));
        }
        return grupoDao.update(grupo);
    }

    public boolean addTraducao(@NotNull Player player, @NotNull String[] args) throws GrupoException {
        // Verifica se o jogador é o administrador do SmartHopper
        if (player.isOp() && !Config.CONTAINS_ADD(player)) {
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
        return grupoDao.update(grupo);
    }

    private String convertTraducao(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i].concat(" "));
        }
        return sb.toString().trim();
    }

    public boolean delete(Player player, Grupo grupo) throws GrupoException {

        if (!player.isOp() && !Config.CONTAINS_ADD(player)) {
            throw new GrupoException("Ops, você não é adm do Smart Hopper!!!");
        }

        if (grupo == null || grupo.getId() < 1) {
            throw new GrupoException("Digite o nome ou ID do grupo!!!");
        }
        return grupoDao.delete(grupo);
    }


    public List<String> findNameContains(String name) {
        List<String> list = new ArrayList<>();
        for (String grup : GRUPO_NAME_LIST) {
            if (grup.toLowerCase().contains(name.toLowerCase()) || grup.equalsIgnoreCase(name)) {
                if (!list.contains(grup)) {
                    list.add(grup);
                }
            }
        }
        return list;
    }

    public static void findAll() {
        clear();
        GrupoDao dao = new GrupoDao();
        GrupoCreate.NewNome();
        dao.findAll();
        if(GRUPO_MAP_NAME.size()<1){
            GrupoCreate.NEW();
        }else {
            ItemController itemCtrl = new ItemController();
            // Percorrendo a lista do grupo e adicionando o grupo no item
            for (Grupo grupo : GRUPO_MAP_NAME.values()) {
                try {
                    for (String itemName : grupo.getListNameItem()) {
                        Item item = itemCtrl.findByName(itemName);
                        if (item == null) continue;
                        item.addListGrupo(grupo);
                        ITEM_MAP_ID.put(item.getId(), item);
                        ITEM_MAP_NAME.put(item.getName().toLowerCase(), item);
                    }
                } catch (ItemException e) {
                    Msg.ServidorErro(e, "findAll()", GrupoController.class);
                }
            }
        }
    }

    public static void clear() {
        List<String> list = new ArrayList<>();
        String txt = null;
        if (GRUPO_FILE.exists()) {
            try (BufferedReader reader = Files.newBufferedReader(GRUPO_FILE.toPath(), StandardCharsets.UTF_8)) {
                while ((txt = reader.readLine()) != null) {
                    list.add(txt.replaceAll("grupo_", "").replaceAll("[\']", ""));
                }
            } catch (IOException e) {
                Msg.ServidorErro(e, "clean()", GrupoController.class);
            }
            if (list.size() > 0) {
                try (BufferedWriter writer = Files.newBufferedWriter(GRUPO_FILE.toPath(), StandardCharsets.UTF_8)) {
                    for (String arg : list) {
                        writer.write(arg);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    Msg.ServidorErro(e, "clean()", GrupoController.class);
                }
            }
        }
    }

    private int ID() {
        int id = 0;
        for (Grupo grupo : GRUPO_MAP_NAME.values()) {
            if (id < grupo.getId()) {
                id = grupo.getId();
            }
        }
        id++;
        return id;
    }

    public Grupo getGrupo(){
        return grupo;
    }

    public static void CREATE() {
        GrupoDao dao = new GrupoDao();
        GrupoCreate.NewNome();
        if (dao.findByName("acacia") == null) {
            GrupoCreate.NEW();
        }
    }


}