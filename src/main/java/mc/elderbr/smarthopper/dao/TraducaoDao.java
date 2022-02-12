package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TraducaoDao {


    private Traducao traducao;

    private Connection conexao;
    private PreparedStatement smt;
    private ResultSet rs;
    private String sql;

    private Item item;
    private Grupo grupo;

    public TraducaoDao() {
        createTable();
    }

    private void createTable() {
        sql = "CREATE TABLE IF NOT EXISTS traducao (" +
                "cdTraducao INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", dsTraducao NVARCHAR(30) NOT NULL" +
                ", cdLang INTEGER NO NULL" +
                ", cdItem INTEGER DEFAULT 0" +
                ", cdGrupo INTEGER DEFAULT 0)";
        try {
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela tradução", "createTable", getClass(), e);
        }
    }

    public int insert(Object traducao) {

        if (traducao instanceof Item || traducao instanceof Grupo) {
            try {
                if (traducao instanceof Item) {
                    item = (Item) traducao;
                    sql = "INSERT INTO traducao (dsTraducao, cdLang, cdItem) VALUES (?,?,?);";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, item.getDsTraducao());
                    smt.setInt(2, item.getCdLang());
                    smt.setInt(3, item.getCdItem());
                } else if(traducao instanceof Grupo){
                    grupo = (Grupo) traducao;
                    sql = "INSERT INTO traducao (dsTraducao, cdLang, cdGrupo) VALUES (?,?,?);";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, grupo.getDsTraducao());
                    smt.setInt(2, grupo.getCdLang());
                    smt.setInt(3, grupo.getCdGrupo());
                }else{
                    return 0;
                }
                return smt.executeUpdate();
            } catch (SQLException e) {
                Msg.ServidorErro("Erro ao adicionar tradução do item/grupo!!!", "insert(Object traducao)", getClass(), e);
            } finally {
                Conexao.desconect();
            }
        }
        return 0;
    }

    public Item selectItem(Object item) {
        this.item = null;
        if (item instanceof Item || item instanceof Integer || item instanceof String) {

            try {

                if (item instanceof Item) {
                    if (((Item) item).getCdItem() > 0) {
                        sql = "SELECT * FROM traducao t INNER JOIN item i ON i.cdItem = t.cdItem LEFT JOIN lang l ON t.cdLang = l.cdLang WHERE t.cdItem = ? AND l.cdlang = ?";
                        smt = Conexao.prepared(sql);
                        smt.setInt(1, ((Item) item).getCdItem());
                        smt.setInt(2, ((Item) item).getCdLang());
                    } else {
                        sql = "SELECT * FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.dsItem = ? COLLATE NOCASE";
                        smt = Conexao.prepared(sql);
                        smt.setString(1, ((Item) item).getDsItem());
                    }
                } else if (item instanceof Integer) {
                    sql = "SELECT * FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.cdItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, ((Item) item).getCdItem());
                } else if(item instanceof String){
                    sql = "SELECT * FROM item i " +
                            "INNER JOIN traducao t ON i.cdItem = t.cdItem " +
                            "INNER JOIN lang l ON t.cdLang = l.cdLang "+
                            "WHERE i.dsItem = ? OR t.dsTraducao = ? COLLATE NOCASE";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, (String) item);
                    smt.setString(2, (String) item);
                }else{
                    return null;
                }
                rs = smt.executeQuery();
                while (rs.next()) {
                    this.item = new Item();
                    this.item.setCdItem(rs.getInt("cdItem"));
                    this.item.setDsItem(rs.getString("dsItem"));
                    this.item.setCdLang(rs.getInt("cdLang"));
                    this.item.setDsLang(rs.getString("dsLang"));
                    this.item.setCdTraducao(rs.getInt("cdTraducao"));
                    this.item.setDsTraducao(rs.getString("dsTraducao"));
                }
                smt.close();
                rs.close();
            } catch (SQLException e) {
                Msg.ServidorErro("Erro ao buscar a tradução do item!!!", "selectItem(Object item)", getClass(), e);
            } finally {
                Conexao.desconect();
            }
        }
        return this.item;
    }

    public Grupo selectGrupo(Object traducao) {
        grupo = null;
        if (traducao instanceof Grupo || traducao instanceof Integer || traducao instanceof String) {
            try {
                if (traducao instanceof Grupo) {
                    if (((Grupo) traducao).getCdGrupo() > 0) {
                        sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.cdGrupo = ?";
                        smt = Conexao.prepared(sql);
                        smt.setInt(1, ((Grupo) traducao).getCdGrupo());
                    } else {
                        sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.dsGrupo = ?";
                        smt = Conexao.prepared(sql);
                        smt.setString(1, ((Grupo) traducao).getDsGrupo());
                    }
                }
                if (traducao instanceof Integer) {
                    sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.cdGrupo = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, (Integer) traducao);
                }
                if (traducao instanceof String) {
                    sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.dsGrupo = ? OR t.dsTraducao = ? COLLATE NOCASE";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, (String) traducao);
                    smt.setString(2, (String) traducao);
                }
                rs = smt.executeQuery();
                while (rs.next()) {
                    grupo = new Grupo();
                    grupo.setCdGrupo(rs.getInt("cdGrupo"));
                    grupo.setDsGrupo(rs.getString("dsGrupo"));
                    grupo.setDsTraducao(rs.getString("dsTraducao"));
                    if(grupo.getDsTraducao()==null){
                        grupo.setDsTraducao(grupo.getDsGrupo());
                    }
                }
                smt.close();
                rs.close();
            } catch (SQLException e) {
                Msg.ServidorErro("Erro ao buscar a tradução do grupo!!!", "selectGrupo(Object traducao)", getClass(), e);
            } finally {
                Conexao.desconect();
            }
        }
        return grupo;
    }

    public int update(Item item) {
        if(item == null){
            return 0;
        }
        sql = "UPDATE traducao SET dsTraducao = ?, cdLang = ? WHERE cdTraducao = ?;";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, item.getDsTraducao());
            smt.setInt(2, item.getCdLang());
            smt.setInt(3, item.getCdTraducao());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao atualizar a tradução do item!!!", "update(Item item)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public int update(Grupo grupo) {
        sql = "UPDATE traducao SET dsTraducao = ?, cdLang = ? WHERE cdTraducao = ?;";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsTraducao());
            smt.setInt(2, grupo.getCdLang());
            smt.setInt(3, grupo.getCdTraducao());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao atualizar a tradução do grupo!!!", "update(Grupo grupo)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }


}
