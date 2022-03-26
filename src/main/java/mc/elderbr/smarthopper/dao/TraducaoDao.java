package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.Traducao;
import mc.elderbr.smarthopper.model.*;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TraducaoDao {


    private Traducao traducao;

    private Connection conexao;
    private PreparedStatement smt;
    private ResultSet rs;
    private String sql;

    private Item item;
    private Grupo grupo;

    private static String CODIGO = "cd";
    private static String NAME = "nome";
    private Map<String, String> parameters = new HashMap<>();

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

        try {
            if (traducao instanceof Item) {
                item = (Item) traducao;
                sql = "INSERT INTO traducao (dsTraducao, cdLang, cdItem) VALUES (?,?,?);";
                smt = Conexao.prepared(sql);
                smt.setString(1, item.getDsTraducao());
                smt.setInt(2, item.getCdLang());
                smt.setInt(3, item.getCodigo());
            } else if (traducao instanceof Grupo) {
                grupo = (Grupo) traducao;
                sql = "INSERT INTO traducao (dsTraducao, cdLang, cdGrupo) VALUES (?,?,?);";
                smt = Conexao.prepared(sql);
                smt.setString(1, grupo.getDsTraducao());
                smt.setInt(2, grupo.getCdLang());
                smt.setInt(3, grupo.getCdGrupo());
            } else {
                return 0;
            }
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar tradução do item/grupo!!!", "insert(Object traducao)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public Item selectItem(Object item) {
        this.item = null;
        if (item instanceof Item || item instanceof Integer || item instanceof String) {

            try {

                if (item instanceof Item) {
                    if (((Item) item).getCodigo() > 0) {
                        sql = "SELECT * FROM traducao t INNER JOIN item i ON i.cdItem = t.cdItem LEFT JOIN lang l ON t.cdLang = l.cdLang WHERE t.cdItem = ? AND l.cdlang = ?";
                        smt = Conexao.prepared(sql);
                        smt.setInt(1, ((Item) item).getCodigo());
                        smt.setInt(2, ((Item) item).getCdLang());
                    } else {
                        sql = "SELECT * FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.dsItem = ? COLLATE NOCASE";
                        smt = Conexao.prepared(sql);
                        smt.setString(1, ((Item) item).getName());
                    }
                } else if (item instanceof Integer) {
                    sql = "SELECT * FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.cdItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, ((Item) item).getCodigo());
                } else if (item instanceof String) {
                    sql = "SELECT * FROM item i " +
                            "INNER JOIN traducao t ON i.cdItem = t.cdItem " +
                            "INNER JOIN lang l ON t.cdLang = l.cdLang " +
                            "WHERE i.dsItem = ? OR t.dsTraducao = ? COLLATE NOCASE";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, (String) item);
                    smt.setString(2, (String) item);
                } else {
                    return null;
                }
                rs = smt.executeQuery();
                while (rs.next()) {
                    this.item = new Item();
                    this.item.setCodigo(rs.getInt("cdItem"));
                    this.item.setName(rs.getString("dsItem"));
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

    public Traducao selectGrupo(Grupo traducao) {
        try {
            grupo = (Grupo) traducao;

            addParameters();

            if (grupo.getCdGrupo() > 0) {
                sql = "SELECT * FROM grupo g LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo LEFT JOIN lang l ON l.cdLang = t.cdLang WHERE " + parameters.get(CODIGO);
            } else {
                sql = "SELECT * FROM grupo g LEFT JOIN traducao t ON g.cdGrupo = t.cdGrupo LEFT JOIN lang l ON l.cdLang = t.cdLang WHERE " + parameters.get(NAME);
            }
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                // LANGUAGEM
                grupo.setCdLang(rs.getInt("cdLang"));
                grupo.setDsLang(rs.getString("dsLang"));
                // Tradução
                grupo.setCdTraducao(rs.getInt("cdTraducao"));
                grupo.setDsTraducao(rs.getString("dsTraducao"));
                //this.traducao = new TraducaoDao();
                this.traducao.setCdTraducao(rs.getInt("cdTraducao"));
                this.traducao.setDsTraducao(rs.getString("dsTraducao"));
                return this.traducao;
            }
            smt.close();
            rs.close();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar a tradução do grupo!!!", "selectGrupo(Object traducao)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    public int update(Item item) {
        if (item == null) {
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
        sql = "UPDATE traducao SET dsTraducao = ? WHERE cdTraducao = ?;";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsTraducao());
            smt.setInt(2, grupo.getCdTraducao());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao atualizar a tradução do grupo!!!", "update(Grupo grupo)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }


    public long delete(Grupo grupo) {
        try {
            smt = Conexao.prepared(String.format("DELETE FROM traducao WHERE cdTraducao = %d", grupo.getCdTraducao()));
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao tentar apagar a tradução do grupo!!!", "", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    private void addParameters() {
        parameters.put(CODIGO, "g.cdGrupo = " + grupo.getCdGrupo() + " AND l.cdLang = " + grupo.getCdLang());
        parameters.put(NAME, "g.dsGrupo = \"" + grupo.getDsGrupo() + "\"" + " AND l.cdLang = " + grupo.getCdLang());
    }
}
