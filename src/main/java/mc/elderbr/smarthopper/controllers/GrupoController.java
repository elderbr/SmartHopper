package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.factories.GrupFactory;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.interfaces.msg.GrupMsg;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.GrupoCreate;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrupoController implements GrupMsg, VGlobal {

    private static GrupoDao grupoDao = GrupoDao.getInstance();
    private List<Grupo> listGrupo;
    private ItemController itemCtrl = new ItemController();

    public GrupoController() {
    }

    public boolean save(Grupo grupo) throws GrupoException {
        // O grupo não pode nulo e precisa conter o nome
        if (grupo == null || grupo.getName().isBlank()) {
            throw new GrupoException(GRUP_NAME_REQUIRED);
        }
        // Verificar se existe item adicionado no grupo
        if (grupo.getItems().isEmpty()) {
            throw new GrupoException(GRUP_ITEM_REQUIRED);
        }
        grupo.setId(ID());
        return grupoDao.save(grupo);
    }

    public Grupo findById(Integer id) {
        if (id < 1) {
            throw new GrupoException(GRUP_INVALID);
        }
        Grupo grupo = grupoDao.findById(id);
        if (grupo == null) {
            throw new GrupoException(GRUP_INVALID);
        }
        return grupo;
    }

    public Grupo findByName(@NotNull String name) throws GrupoException {
        if (name.isBlank()) {
            throw new GrupoException(GRUP_NAME_REQUIRED);
        }
        Grupo grupo = grupoDao.findByName(name);
        if (grupo == null) {
            throw new GrupoException(GRUP_NOT_EXIST);
        }
        return grupo;
    }

    public Grupo findByIdOrName(String value) {
        Grupo grup;
        String grupName = value.toLowerCase();
        try {
            int code = Integer.parseInt(grupName.replaceAll("[^0-9]", ""));
            grup = grupoDao.findById(code);
        } catch (NumberFormatException e) {
            grup = grupoDao.findByName(grupName);
        }
        if (grup == null) {
            throw new GrupoException(String.format(GRUP_NOT_EXIST, value));
        }
        return grup;
    }

    public Grupo findByIdOrName(String[] args) {
        Grupo grup;
        String grupName = Utils.NAME_ARRAY(args);
        try {
            int code = Integer.parseInt(grupName.replaceAll("[^0-9]", ""));
            grup = grupoDao.findById(code);
        } catch (NumberFormatException e) {
            grup = grupoDao.findByName(grupName);
        }
        if (grup == null) {
            throw new GrupoException(String.format(GRUP_NOT_EXIST, grupName));
        }
        return grup;
    }

    public List<Grupo> findByItemStack(ItemStack itemStack) throws GrupoException {
        Item item = itemCtrl.findByItemStack(itemStack);
        List<Grupo> grups = new ArrayList<>();
        for(Integer code : item.getGrups()){
            grups.add(grupoDao.findById(code));
        }
        return grups;
    }

    public Grupo findNameTranslation(String name) {
        Grupo grupo = grupoDao.findByName(name);
        if (grupo == null) {
            for (Grupo grup : GRUPO_MAP_NAME.values()) {
                for (String lang : grup.getTranslations().values()) {
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
            throw new GrupoException(GRUP_INVALID);
        }
        grupo = grupoDao.findByName(grupo.getName());
        if (grupo == null) {
            throw new GrupoException(String.format(GRUP_NOT_EXIST, grupo.getName()));
        }
        return grupoDao.update(grupo);
    }

    public boolean addTraducao(@NotNull Player player, @NotNull String[] args) throws GrupoException {
        // Verifica se o jogador é o administrador do SmartHopper
        if (!player.isOp() && !AdmController.ContainsAdm(player)) {
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
        int id = 0;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new GrupoException(String.format("O código %s não é valido!!!", args[0]));
        }
        Grupo grupo = grupoDao.findById(id);
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

        if (!player.isOp() && !AdmController.ContainsAdm(player)) {
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
        grupoDao.findAll();
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
        int id = Collections.max(GRUPO_MAP_ID.keySet());
        return id + 1;
    }

    public static void CREATE() {
        GrupoDao grupDao = GrupoDao.getInstance();
        grupDao.findAll();// Busca todos os grupos e adiciona na váriavel global
        ItemDao itemDao = ItemDao.getInstance();
        if(GRUPO_MAP_ID.isEmpty()) {
            int id = (GRUPO_MAP_ID.isEmpty() ? 1 : Collections.max(GRUPO_MAP_ID.keySet()) + 1);
            for (String nameGrup : GrupoCreate.NEW()) {
                Grupo grup = grupDao.findByName(nameGrup);// Busca o grupo na variavel global
                if (grup == null) {// Criando novo grupo
                    grup = new Grupo();
                    grup.setId(id);
                    grup.setName(nameGrup);
                    id++;
                }
                for (String nameItem : ITEM_MAP_NAME.keySet()) {
                    if (GrupoCreate.containsItem(nameGrup, nameItem)) {
                        // Adiciona o item ao grupo se não existir na lista
                        if (grup.getItemsNames().contains(nameItem)) continue;
                        grup.addItems(itemDao.findByName(nameItem));
                    }
                }
                grupDao.save(grup);
            }

            // Percorrendo os grupos personalizados
            for (String name : GrupFactory.nameMethods()) {
                try {
                    // Obtém o método da classe GrupFactory pelo nome
                    Method method = GrupFactory.class.getMethod(name);

                    // Verifica se o tipo de retorno do método é Grupo
                    if (method.getReturnType().equals(Grupo.class)) {
                        // Invoca o método para obter o objeto Grupo retornado
                        Grupo grup = (Grupo) method.invoke(null); // null porque o método é estático
                        grupDao.update(grup);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}