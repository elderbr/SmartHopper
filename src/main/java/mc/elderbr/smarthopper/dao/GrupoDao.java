package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrupoDao {

    private Grupo grupo;
    private List<Grupo> listGrupo;

    private List<String> listNames;
    private List<String> listItemName;
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

        createGrupo();
    }

    public void createGrupo() {

        // NOMES DOS GRUPOS
        listNames = new ArrayList<>();
        // PERCORRENDO A LISTA DE MATERIAL PADÃO DO MINECRAFT
        for (Material m : VGlobal.LIST_MATERIAL) {
            // CONVERTENDO O NOME DO MATERIAL REMOVENDO O ANDERLAINE
            // ARRAY DO NOME DO MATERIAL SEPARANDO PELO O ESPAÇO
            String[] itens = Utils.ToMaterial(m).split(" ");
            String name = null;

            // ADICIONA NA LISTA DE NOME SE O ITEM ESTIVER MAIS DE UM NOME E
            // NÃO ESTIVER NA LISTA NOME ADICIONA
            switch (itens.length) {
                case 2:
                    name = itens[0];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[1];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    break;
                case 3:
                    name = itens[0] + " " + itens[1];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[1] + " " + itens[2];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    break;
                case 4:
                    name = itens[0] + " " + itens[1];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[1] + " " + itens[2];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[2] + " " + itens[3];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    break;
                case 5:
                    name = itens[0] + " " + itens[1];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[1] + " " + itens[2];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[2] + " " + itens[3];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[0] + " " + itens[1] + " " + itens[2];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    name = itens[1] + " " + itens[2] + " " + itens[3];
                    if (!listNames.contains(name)) {
                        listNames.add(name);
                    }
                    break;
            }
        }
        // NOME DO GRUPO É VALIDO SE EXISTIR MAIS DE UM ITEM COM O NOME
        contato = 0;
        listItemName = new ArrayList<>();
        for (String names : listNames) {
            for (Material m : VGlobal.LIST_MATERIAL) {
                if (m.isItem()) {
                    if (Utils.ToMaterial(m).contains(names)) {
                        if (contato > 1 && !listItemName.contains(names)) {
                            listItemName.add(names);
                            break;
                        }
                        contato++;
                    }
                }
            }
            contato = 0;
        }

        // LISTA VALIDA DO NOME DO GRUPO
        // VERIFICA SE A LISTA DE ITEM É MAIOR QUE LISTA DO BANCO
        if (listItemName.size() > selectAll().size()) {
            listItemName.forEach(names -> {
                grupo = new Grupo();
                grupo.setDsGrupo(names);
                Msg.ServidorGreen("Criando o grupo " + grupo.getDsGrupo());
                insert(grupo);
            });
        }
    }

    public int insert(Grupo grupo) {
        sql = "INSERT INTO grupo (dsGrupo) VALUES (?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsGrupo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insert(Grupo grupo)", getClass());
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
        if (grupo == null) {
            return null;
        }
        try {
            // BUSCA PELO CÓDIGO DO GRUPO E O TIPO DE LINGUAGEM
            if (grupo.getCdGrupo() > 0) {
                sql = "SELECT * FROM grupo g " +
                        "LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo " +
                        "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                        "WHERE g.cdGrupo = ? AND l.dsLang = ?;";
                smt = Conexao.prepared(sql);
                smt.setInt(1, grupo.getCdGrupo());
                smt.setString(2, grupo.getDsLang());
            } else {
                // BUSCA PELO NOME DO GRUPO OU TRADUÇÃO
                sql = "SELECT * FROM grupo g " +
                        "LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo " +
                        "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                        "WHERE g.dsGrupo = ? OR t.dsTraducao = ? AND l.dsLang = ? COLLATE NOCASE;";
                smt = Conexao.prepared(sql);
                smt.setString(1, grupo.getDsGrupo());
                smt.setString(2, grupo.getDsTraducao());
                smt.setString(3, grupo.getDsLang());
            }
            rs = smt.executeQuery();
            while (rs.next()) {
                this.grupo = new Grupo();
                this.grupo.setCdGrupo(rs.getInt("cdGrupo"));
                this.grupo.setDsGrupo(rs.getString("dsGrupo"));
                this.grupo.setCdLang(rs.getInt("cdLang"));
                this.grupo.setDsLang(rs.getString("dsLang"));
                this.grupo.setCdTraducao(rs.getInt("cdTraducao"));
                this.grupo.setDsTraducao(rs.getString("dsTraducao"));
                // SE O LANG NÃO FOR LOCALIZADO COLOCA COMO O NOME PADRÃO DO GRUPO
                if (this.grupo.getDsTraducao() == null) {
                    this.grupo.setDsTraducao(this.grupo.getDsGrupo());
                }
            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "select(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        // SELECT SIMPLES NO GRUPO BUSCANDO PELO O CÓDIGO OU NOME DO GRUPO OU SUA TRADUÇÃO
        try {
            sql = "SELECT * FROM item i " +
                    "LEFT JOIN traducao t ON t.cdItem = i.cdItem " +
                    "WHERE i.cdItem = ? OR i.dsItem = ? OR t.dsTraducao = ? COLLATE NOCASE";
            smt = Conexao.prepared(sql);
            smt.setInt(1, grupo.getCdGrupo());
            smt.setString(2, grupo.getDsGrupo());
            smt.setString(3, grupo.getDsGrupo());
            rs = smt.executeQuery();
            while (rs.next()) {
                this.grupo = new Grupo();
                this.grupo.setCdGrupo(rs.getInt("cdGrupo"));
                this.grupo.setDsGrupo(rs.getString("dsGrupo"));
                this.grupo.setDsTraducao(rs.getString("dsGrupo"));
                return this.grupo;
            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "select(Grupo grupo)", getClass());
        } finally {
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
                this.grupo = new Grupo();
                this.grupo.setCdGrupo(rs.getInt("cdGrupo"));
                this.grupo.setDsGrupo(rs.getString("dsGrupo"));
                listGrupo.add(this.grupo);
                VGlobal.LIST_GRUPO.add(grupo);
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
            sql = "INSERT INTO grupoItem (cdGrupo, cdItem) VALUES (?,?);";
            smt = Conexao.prepared(sql);
            smt.setInt(1, grupo.getCdGrupo());
            smt.setInt(2, item.getCdItem());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar item do grupo!!!", "", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

}
