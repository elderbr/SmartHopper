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
        if (item instanceof Item || item instanceof Integer) {
            sql = "SELECT * FROM item i INNER JOIN traducao t WHERE i.cdItem = t.cdItem WHERE i.cdItem = ?";
            try {
                smt = Conexao.prepared(sql);
                if (item instanceof Item) {
                    smt.setInt(1, ((Item) item).getCdItem());☺
                } else {
                    smt.setInt(1, (Integer) item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


}
