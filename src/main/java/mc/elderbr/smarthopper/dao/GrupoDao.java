package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoDao {

    private Grupo grupo;
    private String sql;
    private PreparedStatement smt;
    private ResultSet rs;

    public GrupoDao() {

        sql = "CREATE TABLE IF NOT EXISTS grupo (" +
                "cdGrupo INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", dsGrupo VARCHAR(20) NOT NULL UNIQUE);";
        try {
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela grupo!", "GrupoDao", getClass(), e);
        }finally {
            Conexao.desconect();
        }

    }

    public int insert(Grupo grupo) {
        sql = "INSERT INTO grupo (dsGrupo) VALUES (?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getName());
            return smt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insert(Grupo grupo)", getClass());
        }finally {
            Conexao.desconect();
        }
        return 0;
    }

    public Grupo select(Grupo grupo) {
        this.grupo = null;
        if (grupo == null) {
            return null;
        }
        if (grupo.getID() > 0) {
            sql = "SELECT * FROM grupo WHERE cdGrupo = " + grupo.getID();
        } else {
            sql = "SELECT * FROM grupo WHERE dsGrupo = " + grupo.getName();
        }
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.grupo = new Grupo();
                this.grupo.setID(rs.getInt("cdGrupo"));
                this.grupo.setName(rs.getString("dsGrupo"));
            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "select(Grupo grupo)", getClass());
        }finally {
            Conexao.desconect();
        }
        return this.grupo;
    }

    public boolean delete(Grupo grupo) {
        if(grupo == null && grupo.getID()>0){
            return false;
        }
        sql = "DELETE FROM grupo WHERE cdGrupo = " + grupo.getID() + ";";
        try {
            smt = Conexao.prepared(sql);
            int retorno = smt.executeUpdate();
            if(retorno>0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Conexao.desconect();
        }
        return false;
    }

    public int update(Grupo grupo){
        if(grupo == null){
            return 0;
        }
        sql = "UPDATE TABLE grupo SET dsGrupo = ? WHERE cdGrupo = ?";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getName());
            smt.setInt(2, grupo.getID());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e,"update(Grupo grupo)", getClass());
        }finally {
            Conexao.desconect();
        }
        return 0;
    }


}
