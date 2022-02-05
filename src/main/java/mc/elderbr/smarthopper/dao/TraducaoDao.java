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
                ", dsTraducao VARCHAR(30) NOT NULL" +
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
                    sql = "INSERT INTO traducao (dsTraducao, cdLang, cdItem) VALUES (?,?,?);";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, ((Item) traducao).getDsTraducao());
                } else {
                    sql = "INSERT INTO traducao (dsTraducao, cdLang, cdGrupo) VALUES (?,?,?);";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, ((Grupo) traducao).getDsTraducao());
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
                        sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.cdItem = ?";
                        smt = Conexao.prepared(sql);
                        smt.setInt(1, ((Item) item).getCdItem());
                    } else {
                        sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.dsItem = ?";
                        smt = Conexao.prepared(sql);
                        smt.setString(1, ((Item) item).getDsItem());
                    }
                } else if (item instanceof Integer) {
                    sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.cdItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, ((Item) item).getCdItem());
                } else {
                    sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.dsItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, (String) item);
                }
                rs = smt.executeQuery();
                if (rs.isAfterLast()) {
                    while (rs.next()) {
                        this.item = new Item();
                        this.item.setCdItem(rs.getInt("i.cdItem"));
                        this.item.setDsItem(rs.getString("i.dsItem"));
                        this.item.setDsTraducao(rs.getString("t.dsTraducao"));
                    }
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
                        smt.setInt(1, ((Grupo) traducao).getCdGrupo() );
                    }else{
                        sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.dsGrupo = ?";
                        smt = Conexao.prepared(sql);
                        smt.setString(1, ((Grupo)traducao).getDsGrupo() );
                    }
                }
                if(traducao instanceof Integer){
                    sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.cdGrupo = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, (Integer) traducao);
                }
                if(traducao instanceof String){
                    sql = "SELECT * FROM grupo g INNER JOIN traducao t ON g.cdGrupo = t.cdGrupo WHERE g.dsGrupo = ?";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, (String) traducao);
                }
                rs = smt.executeQuery();
                while(rs.next()){
                    grupo = new Grupo();
                    grupo.setCdGrupo(rs.getInt("g.cdGrupo"));
                    grupo.setDsGrupo(rs.getString("g.dsGrupo"));
                    grupo.setDsTraducao(rs.getString("t.dsTraducao"));
                }
                smt.close();
                rs.close();
            } catch (SQLException e) {
                Msg.ServidorErro("Erro ao buscar a tradução do grupo!!!", "selectGrupo(Object traducao)", getClass(), e);
            }finally {
                Conexao.desconect();
            }
        }
        return grupo;
    }


}
