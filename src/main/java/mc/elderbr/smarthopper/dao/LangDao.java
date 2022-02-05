package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.model.Lang;
import mc.elderbr.smarthopper.utils.Msg;
import org.checkerframework.checker.units.qual.A;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LangDao {

    private Lang lang;
    private List<Lang> listLang;

    private Connection conexao;
    private PreparedStatement smt;
    private ResultSet rs;
    private String sql;

    public LangDao() {
        createTable();
    }

    private void createTable() {
        sql = "CREATE TABLE IF NOT EXISTS lang (cdLang INTEGER PRIMARY KEY AUTOINCREMENT, dsLang NVARCHAR(30) UNIQUE NOT NULL)";
        try {
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela lang", "createTable", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        // SE NÃO EXISTIR LANG CRIA PORTUGUÊS BRASIL
        if(selectAll().size()==0){
            lang = new Lang();
            lang.setDsLang("eng");
            insert(lang);
            lang = new Lang();
            lang.setDsLang("pt-br");
            insert(lang);
        }
    }

    public long insert(Lang lang) {
        try {
            sql = "INSERT INTO lang (dsLang) VALUES (?);";
            smt = Conexao.prepared(sql);
            smt.setString(1, lang.getDsLang());
            return smt.executeUpdate();
        } catch (SQLException e) {
            if(e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar Lang!!!", "insert(Lang lang)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public List<Lang> selectAll() {
        listLang = new ArrayList<>();
        try {
            sql = "SELECT * FROM lang;";
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.lang = new Lang();
                this.lang.setCdLang(rs.getInt(1));
                this.lang.setDsLang(rs.getString(2));
                listLang.add(this.lang);
            }
            smt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexao.desconect();
        }
        return listLang;
    }

    public Lang select(Object lang) {
        this.lang = null;
        if (lang instanceof Integer || lang instanceof String || lang instanceof Lang) {
            try {

                if (lang instanceof Integer) {
                    sql = "SELECT * FROM lang WHERE cdLang = " + (Integer) lang;
                } else if (lang instanceof String) {
                    sql = "SELECT * FROM lang WHERE dsLang = '" + (String) lang + "';";
                } else {
                    this.lang = (Lang) lang;
                    if (this.lang.getCdLang() > 0) {
                        sql = "SELECT * FROM lang WHERE cdLang = " + this.lang.getCdLang();
                    } else {
                        sql = "SELECT * FROM lang WHERE dsLang = '" + this.lang + "';";
                    }
                }

                smt = Conexao.prepared(sql);
                rs = smt.executeQuery();
                while (rs.next()) {
                    this.lang = new Lang();
                    this.lang.setCdLang(rs.getInt(0));
                    this.lang.setDsLang(rs.getString(1));
                }
                smt.close();
                rs.close();
            } catch (SQLException e) {
                Msg.ServidorErro("Erro ao buscar o lang!!!", "select(Object lang)", getClass(), e);
            } finally {
                Conexao.desconect();
            }
        }
        return this.lang;
    }

    public long update(Lang lang){
        try {
            sql = "UPDATE TABLE lang SET dsLong = ? WHERE cdLong = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, lang.getDsLang());
            smt.setInt(2, lang.getCdLang());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao atualizar o lang!!!", "update(Lang lang)", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        return 0;
    }
    public long delete(Lang lang){
        try {
            sql = "DELETE TABLE lang WHERE cdLong = ?;";
            smt = Conexao.prepared(sql);
            smt.setInt(1, lang.getCdLang());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao apagar o lang!!!", "delete(Lang lang)", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        return 0;
    }
}
