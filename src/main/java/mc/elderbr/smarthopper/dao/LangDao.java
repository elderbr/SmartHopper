package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.model.Lang;
import mc.elderbr.smarthopper.utils.Msg;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LangDao {

    private static PreparedStatement stm = null;
    private static ResultSet rs = null;
    private static String sql;

    public LangDao() {
        insert("en_us");
    }

    public static int insert(@NotNull Object lang) {
        stm = null;
        try {
            if (lang instanceof String name) {
                sql = String.format("INSERT INTO lang (dsLang) VALUES (%s);", name);
                stm = Conexao.repared(sql);
            }
            if (lang instanceof Lang linguagem) {
                sql = String.format("INSERT INTO lang (dsLang) VALUES (%s);", linguagem.getDsLang());
                stm = Conexao.repared(sql);
            }
            if (stm == null) {
                return 0;
            }
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar nova linguagem!!!", "insert(@NotNull Object lang)", LangDao.class, e);
        } finally {
            Conexao.desconect();
            close();
        }
        return 0;
    }

    public static Lang select(@NotNull Object lang) {
        stm = null;
        try {
            if (lang instanceof Integer code) {
                sql = String.format("SELECT * FROM lang WHERE cdLang = %d;", code);
                stm = Conexao.repared(sql);
            }
            if (lang instanceof Lang languagem) {
                if (languagem.getCdLang() > 0) {
                    sql = String.format("SELECT * FROM lang WHERE cdLang = %d;", languagem.getCdLang());
                } else {
                    sql = String.format("SELECT * FROM lang WHERE dsLang = %s;", languagem.getDsLang());
                }
                stm = Conexao.repared(sql);
            }
            if (lang instanceof String nome) {
                sql = String.format("SELECT * FROM lang WHERE dsLang = %s;", nome);
                stm = Conexao.repared(sql);
            }
            if (stm == null) {
                return null;
            }
            rs = stm.executeQuery();
            if (rs.next()) {
                Lang languagem = new Lang();
                languagem.setCdLang(rs.getInt(1));
                languagem.setDsLang(rs.getString(2));
                return languagem;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar linguagem!!!", "select(@NotNull Object lang)", LangDao.class, e);
        } finally {
            Conexao.desconect();
            close();
        }
        return null;
    }

    private static void close() {
        try {
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao fechar o banco!!!", "", LangDao.class, e);
        }
    }

}
