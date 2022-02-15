package mc.elderbr.smarthopper.dao;


import mc.elderbr.smarthopper.CargoType;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Cargo;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDao {


    private PreparedStatement smt;
    private ResultSet rs;
    private String sql;

    public CargoDao() {

        createTable();

        if (selectAll().isEmpty()) {
            for (CargoType type : CargoType.values()) {
                insert(type.name());
            }
        }
    }

    private void createTable() {
        try {
            //AUTOINCREMENT
            sql = "CREATE TABLE IF NOT EXISTS cargo (cdCargo INTEGER PRIMARY KEY AUTOINCREMENT, dsCargo NVARCHAR(20) NOT NULL UNIQUE);";
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela de cargo!!!", "createTable", getClass(), e);
        }
    }

    public void insert(String cargo) {
        try {
            sql = "INSERT INTO cargo (dsCargo) VALUES (?);";
            smt = Conexao.prepared(sql);
            smt.setString(1, cargo);
            smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar novo cargo!!!", "insert", getClass(), e);
        } finally {
            Conexao.desconect();
        }
    }

    public List<Cargo> selectAll() {
        List<Cargo> listCargo = new ArrayList<>();
        try {
            sql = "SELECT * FROM cargo;";
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setCdCargo(rs.getInt("cdCargo"));
                cargo.setDsCargo(rs.getString("dsCargo"));
                listCargo.add(cargo);
                VGlobal.CARGO_NOME_MAP.put(cargo.getCdCargo(), cargo.getDsCargo());
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar todos os cargos!!!", "selectAll", getClass(), e);
        }
        return listCargo;
    }

    public long delete(String cargo) {
        try {
            sql = "DELETE FROM cargo WHERE dsCargo = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, cargo);
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao deleta cargo!!!", "delete", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }


}
