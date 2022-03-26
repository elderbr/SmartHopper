package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.Debugs;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrupoDao {

    private int position;
    private int cdGrupo;
    private Grupo grupo;
    private List<Grupo> listGrupo;
    private List<String> listNameGrupo;

    private Item item;
    private List<String> listNames;
    private List<Item> listItem;
    private int contato;

    private String sql;
    private PreparedStatement smt;
    private ResultSet rs;

    public GrupoDao() {


        try {
            sql = "CREATE TABLE IF NOT EXISTS grupo (" +
                    "cdGrupo INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", dsGrupo VARCHAR(20) NOT NULL UNIQUE);";
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela grupo!", "GrupoDao", getClass(), e);
        } finally {
            Conexao.desconect();
        }

        try {
            sql = "CREATE TABLE IF NOT EXISTS grupoItem (" +
                    "cdGItem INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", cdGrupo INTEGER NOT NULL" +
                    ", cdItem INTEGER NOT NULL);";
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela item do grupo!", "GrupoDao", getClass(), e);
        } finally {
            Conexao.desconect();
        }
    }

    public void createGrupo() {

        Debugs.escrever("Criando grupos padrão!!!");
        String name = null;
        // NOMES DOS GRUPOS
        listNames = new ArrayList<>();
        // PERCORRENDO A LISTA DE MATERIAL PADÃO DO MINECRAFT
        item = new Item();
        for (Item itens : item.getListMaterial()) {
            name = itens.getName();
            if (!listNames.contains(name)) {
                listNames.add(name);
            }
            String names[] = name.split("\\s");
            for (int i = 0; i < names.length; i++) {
                if (names[i].length() > 2 && !listNames.contains(names[i])) {
                    listNames.add(names[i]);
                }
                if ((i + 1) < names.length && names[i].length() > 2) {
                    name = names[i] + " " + names[i + 1];
                    if (!listNames.contains(names)) {
                        listNames.add(name);
                    }
                }
                if ((i + 2) < names.length && names[i].length() > 2) {
                    name = names[i] + " " + names[i + 1] + " " + names[i + 2];
                    if (!listNames.contains(names)) {
                        listNames.add(name);
                    }
                }
                if ((i + 3) < names.length && names[i].length() > 2) {
                    name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3];
                    if (!listNames.contains(names)) {
                        listNames.add(name);
                    }
                }
                if ((i + 4) < names.length && names[i].length() > 2) {
                    name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3] + " " + names[i + 4];
                    if (!listNames.contains(names)) {
                        listNames.add(name);
                    }
                }
                if ((i + 5) < names.length && names[i].length() > 2) {
                    name = names[i] + " " + names[i + 1] + " " + names[i + 2] + " " + names[i + 3] + " " + names[i + 4] + " " + names[i + 5];
                    if (!listNames.contains(names)) {
                        listNames.add(name);
                    }
                }
            }
        }// FIM DE PERCORRER A LISTA DE MATERIAIS

        // VERIFICA SE EXISTE MAIS DE UM ITEM COM O NOME DO GRUPO SE SIM O GRUPO É VALIDO
        contato = 0;
        listNameGrupo = new ArrayList<>();
        Collections.sort(listNames);
        for (String gp : listNames) {
            grupo = new Grupo();
            grupo.setDsGrupo(gp);
            for (Item item : item.getListMaterial()) {
                if (item.contains(grupo)) {
                    if (contato > 0 && !listNameGrupo.contains(gp)) {
                        listNameGrupo.add(gp);
                    }
                    contato++;
                }
            }
            contato = 0;
        }
        // ADICIONAR GRUPO E SEU ITEM NO BANCO DE DADOS
        if (listNameGrupo.size() != selectAll().size()) {
            Debugs.escrever("Adicionando novo grupo!!!");
            Msg.ServidorGold("....Criando grupos....");
            Collections.sort(listNameGrupo);
            listNameGrupo.forEach(gp -> {
                grupo = new Grupo();
                grupo.setDsGrupo(gp);
                if ((cdGrupo = insert(grupo)) > 0) {
                    grupo.setCdGrupo(cdGrupo);
                    Debugs.escrever("Adicionando grupo " + grupo.getDsGrupo());
                    for (Item item : VGlobal.LIST_ITEM) {
                        if (item.contains(grupo)) {
                            if (insertItem(grupo, item) > 0) {
                                Debugs.escrever("Grupo " + grupo.getDsGrupo() + " - Item >> " + item.getName());
                            }
                        }
                        // Grupo de redstones
                        if (grupo.getDsGrupo().equals("redstone")) {
                            if (item.getName().contains("redstone")
                                    || item.getName().equals("dispenser")
                                    || item.getName().equals("note block")
                                    || item.getName().contains("piston")
                                    || item.getName().equals("lever")
                                    || item.getName().contains("pressure plate")
                                    || item.getName().contains("button")
                                    || item.getName().contains("trapdoor")
                                    || item.getName().contains("tripwire")
                                    || item.getName().contains("chest")
                                    && !item.getName().contains("chestplate")
                                    || item.getName().contains("daylight")
                                    || item.getName().contains("hopper")
                                    || item.getName().contains("drooper")
                                    || item.getName().contains("observer")
                                    || item.getName().contains("iron door")
                                    || item.getName().contains("comparator")
                                    || item.getName().contains("repeater")) {
                                insertItem(grupo, item);
                            }
                        }
                        // Grupo de flores
                        if (grupo.getDsGrupo().equals("flower")) {
                            if (item.getName().equals("grass")
                                    || item.getName().equals("fern")
                                    || item.getName().equals("dead bush")
                                    || item.getName().contains("seagrass")
                                    || item.getName().equals("sea pickle")
                                    || item.getName().equals("dandelion")
                                    || item.getName().equals("poppy")
                                    || item.getName().equals("blue orchid")
                                    || item.getName().equals("allium")
                                    || item.getName().equals("azure bluet")
                                    || item.getName().contains("tulip")
                                    || item.getName().equals("oxeye daisy")
                                    || item.getName().equals("cornflower")
                                    || item.getName().contains("lily")
                                    || item.getName().contains("rose")
                                    || item.getName().equals("vine")
                                    || item.getName().equals("sunflower")
                                    || item.getName().equals("lilac")
                                    || item.getName().equals("peony")
                                    || item.getName().equals("tall grass")
                                    || item.getName().equals("large fern")
                            ) {
                                insertItem(grupo, item);
                            }
                        }
                    }
                }
            });
        }
    }

    public int insert(Grupo grupo) {
        sql = "INSERT INTO grupo (dsGrupo) VALUES (?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsGrupo());
            smt.executeUpdate();
            rs = smt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insert(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public int insertID(Grupo grupo) {
        sql = "INSERT INTO grupo (cdGrupo, dsGrupo) VALUES (?,?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setInt(1, grupo.getCdGrupo());
            smt.setString(2, grupo.getDsGrupo());
            smt.executeUpdate();
            rs = smt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insertID(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    private void insertName(String name) {
        grupo = new Grupo();
        grupo.setDsGrupo(name);
        if (insert(grupo) > 0) {
            Msg.ServidorGreen("grupo item >> " + name);
        }
    }

    public Grupo select(Grupo grupo) {
        if( (this.grupo = selectTraducao(grupo))!=null){
            return this.grupo;
        }
        if( (this.grupo = selectName(grupo))!=null){
            return this.grupo;
        }
        return selectCodigo(grupo);
    }

    public Grupo selectCodigo(Grupo gp) {
        try {
            sql = "SELECT g.cdGrupo, g.dsGrupo, t.cdTraducao, t.dsTraducao, l.cdLang, l.dsLang FROM grupo g " +
                    "LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE g.cdGrupo = ? AND l.dsLang = ?;";
            smt = Conexao.prepared(sql);
            smt.setInt(1, gp.getCdGrupo());
            smt.setString(2, gp.getDsLang());
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                grupo.setCdLang(rs.getInt("cdLang"));
                grupo.setDsLang(rs.getString("dsLang"));
                grupo.setCdTraducao(rs.getInt("cdTraducao"));
                grupo.setDsTraducao(rs.getString("dsTraducao"));
                return grupo;
            }

            smt.close();
            rs.close();

            sql = "SELECT * FROM grupo WHERE cdGrupo = ?;";
            smt = Conexao.prepared(sql);
            smt.setInt(1, gp.getCdGrupo());
            rs = smt.executeQuery();
            while(rs.next()){
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                return grupo;
            }

        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o grupo pela sua tradução!", "selectTraducao(Grupo gp)", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        return null;
    }

    public Grupo selectName(Grupo gp) {
        try {
            sql = "SELECT g.cdGrupo, g.dsGrupo, t.cdTraducao, t.dsTraducao, l.cdLang, l.dsLang FROM grupo g " +
                    "LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE lower(g.dsGrupo) = lower(?) AND l.dsLang = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, gp.getDsGrupo());
            smt.setString(2, gp.getDsLang());
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                grupo.setCdLang(rs.getInt("cdLang"));
                grupo.setDsLang(rs.getString("dsLang"));
                grupo.setCdTraducao(rs.getInt("cdTraducao"));
                grupo.setDsTraducao(rs.getString("dsTraducao"));
                return grupo;
            }

            smt.close();
            rs.close();

            sql = "SELECT * FROM grupo WHERE lower(dsGrupo) = lower(?);";
            smt = Conexao.prepared(sql);
            smt.setString(1, gp.getDsGrupo());
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                return grupo;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o grupo pela sua tradução!", "selectName(Grupo gp)", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        return null;
    }

    public Grupo selectTraducao(Grupo gp) {
        try {
            sql = "SELECT g.cdGrupo, g.dsGrupo, t.cdTraducao, t.dsTraducao, l.cdLang, l.dsLang FROM grupo g " +
                    "LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE lower(t.dsTraducao) = lower(?) AND l.dsLang = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, gp.getDsGrupo());
            smt.setString(2, gp.getDsLang());
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                grupo.setCdLang(rs.getInt("cdLang"));
                grupo.setDsLang(rs.getString("dsLang"));
                grupo.setCdTraducao(rs.getInt("cdTraducao"));
                grupo.setDsTraducao(rs.getString("dsTraducao"));
                return grupo;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o grupo pela sua tradução!", "selectTraducao(Grupo gp)", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        return null;
    }

    public List<Grupo> selectAll() {
        listGrupo = new ArrayList<>();
        sql = "SELECT * FROM grupo";
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                listGrupo.add(this.grupo);
                // ADICIONANDO NAS VARIAVEIS GLOBAIS
                VGlobal.LIST_GRUPO.add(grupo);
                VGlobal.GRUPO_ID_MAP.put(this.grupo.getCdGrupo(), this.grupo);
                VGlobal.GRUPO_NAME_MAP.put(this.grupo.getDsGrupo(), this.grupo);
                Debugs.escrever("Lendo o grupo " + grupo.getDsGrupo());

            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "selectAll()", getClass());
        }
        return listGrupo;
    }

    public boolean delete(Grupo grupo) {
        if (grupo == null && grupo.getCdGrupo() > 0) {
            return false;
        }
        sql = "DELETE FROM grupo WHERE cdGrupo = " + grupo.getCdGrupo() + ";";
        try {
            smt = Conexao.prepared(sql);
            int retorno = smt.executeUpdate();
            if (retorno > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public int update(Grupo grupo) {
        if (grupo == null) {
            return 0;
        }
        sql = "UPDATE TABLE grupo SET dsGrupo = ? WHERE cdGrupo = ?";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsGrupo());
            smt.setInt(2, grupo.getCdGrupo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e, "update(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    /* ITEM DO GRUPO */
    public int insertItem(Grupo grupo, Item item) {
        try {
            sql = "INSERT INTO grupoItem (cdGrupo, cdItem) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM grupoItem WHERE cdGrupo = ? AND cdItem = ?);";
            smt = Conexao.prepared(sql);
            smt.setInt(1, grupo.getCdGrupo());
            smt.setInt(2, item.getCodigo());
            smt.setInt(3, grupo.getCdGrupo());
            smt.setInt(4, item.getCodigo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar item do grupo!!!", "", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public List<Grupo> selectListGrupo(Item item) {
        listGrupo = new ArrayList<>();
        try {
            // PROCURA NA TABELA GRUPO DO ITEM O CÓDIGO DO ITEM
            sql = String.format("SELECT * FROM grupo g " +
                    "LEFT JOIN grupoItem gi ON g.cdGrupo = gi.cdGrupo " +
                    "LEFT JOIN item i ON i.cdItem = gi.cdItem " +
                    "LEFT JOIN traducao t ON t.cdGrupo = g.cdGrupo " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE i.cdItem = %d AND l.dsLang = \"%s\"", item.getCodigo(), item.getDsLang());
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            if (!rs.next()) {
                sql = String.format("SELECT * FROM grupo g " +
                        "LEFT JOIN grupoItem gi ON g.cdGrupo = gi.cdGrupo " +
                        "LEFT JOIN traducao t ON t.cdGrupo = g.cdGrupo " +
                        "LEFT JOIN item i ON i.cdItem = gi.cdItem " +
                        "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                        "WHERE gi.cdItem = %d;", item.getCodigo());
            }
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                grupo.setDsTraducao(rs.getString("dsTraducao"));
                grupo.setDsLang(rs.getString("dsLang"));
                listGrupo.add(grupo);
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao localizar grupo!!!", "selectListGrupo(Item item)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return listGrupo;
    }

    public List<Item> selectListItemGrupo(Grupo gp) {
        listItem = new ArrayList<>();
        try {
            sql = "SELECT gi.cdGitem, g.cdGrupo, g.dsGrupo, i.cdItem, i.dsItem FROM grupo g " +
                    "LEFT JOIN grupoItem gi ON gi.cdGrupo = g.cdGrupo " +
                    "LEFT JOIN item i ON i.cdItem = gi.cdItem " +
                    "WHERE g.cdGrupo = ?;";

            smt = Conexao.prepared(sql);
            smt.setInt(1, gp.getCdGrupo());
            rs = smt.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setCodigo(rs.getInt("cdItem"));
                item.setName(rs.getString("dsItem"));
                if(item.getCodigo()>0) {
                    listItem.add(item);
                }
            }
            return listItem;
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao localizar lista item do grupo!!!", "selectListItemGrupo(Grupo gp)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    /***
     * Deleta todos os itens do grupo
     * @param grupo
     * @return Long se for maior que zero foi deletado
     */
    public long deleteItem(Grupo grupo) {
        try {
            sql = "DELETE FROM grupoItem WHERE cdGrupo = ?";
            smt = Conexao.prepared(sql);
            smt.setInt(1, grupo.getCdGrupo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao deletar os itens do grupo!!!", "deleteItem", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }


}
