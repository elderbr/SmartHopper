package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Msg;
import org.checkerframework.checker.units.qual.C;

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

    public TraducaoDao() {
        createTable();
    }

    private void createTable() {
        sql = "CREATE TABLE IF NOT EXISTS traducao (" +
                "cdTraducao INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", dsTraducao VARCHAR(30) NOT NULL" +
                ", cdLang INTEGER" +
                ", cdItem INTEGER" +
                ", cdGrupo INTEGER)";
        try {
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela tradução", "createTable", getClass(), e);
        }
    }

    public int insert(Traducao traducao) {
        sql = "INSERT INTO traducao (dsTraducao, cdLang, cdItem) VALUES (?,?,?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, traducao.getDsTraducao());
            return smt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void selectItem(Object item) {
        this.item = null;
        if (item instanceof Item || item instanceof Integer || item instanceof String) {

            try {

                if (item instanceof Item) {
                    sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.cdItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, ((Item) item).getCdItem());
                } else if(item instanceof Integer) {
                    sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.cdItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setInt(1, ((Item) item).getCdItem());
                }else{
                    sql = "SELECT i.cdItem, i.dsItem, t.dsTraducao FROM item i INNER JOIN traducao t ON i.cdItem = t.cdItem WHERE i.dsItem = ?";
                    smt = Conexao.prepared(sql);
                    smt.setString(1, (String) item);
                }
                rs = smt.executeQuery();
                if(rs.isAfterLast()) {
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
            }finally {
                Conexao.desconect();
            }
        }

    }


}
