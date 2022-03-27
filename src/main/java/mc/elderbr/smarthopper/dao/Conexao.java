package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.utils.Msg;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {
    private static Connection conexao;

    public Conexao() {
        // CRIANDO TODAS AS TABELAS
        CREATE_TABLES();
    }

    public static Connection connected() {

        String url = "jdbc:sqlite:escala.db";
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.setEncoding(SQLiteConfig.Encoding.UTF8);
            conexao = DriverManager.getConnection(url, config.toProperties());
            return conexao;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean desconect() {
        try {
            if (conexao.isClosed() == false) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static PreparedStatement repared(String sql) throws SQLException {
        connected();
        return conexao.prepareStatement(sql);
    }

    public static void CREATE_TABLES() {
        PreparedStatement stm = null;
        String[] query = new String[]{
                "CREATE TABLE IF NOT EXISTS lang (cdLang INTEGER PRIMARY KEY AUTOINCREMENT, dsLang TEXT NOT NULL UNIQUE);",
                "CREATE TABLE IF NOT EXISTS traducao (cdTraducao INTEGER PRIMARY KEY AUTOINCREMENT, dsTraducao TEXT NOT NULL UNIQUE);",
                "CREATE TABLE IF NOT EXISTS item (cdItem INTEGER PRIMARY KEY AUTOINCREMENT, dsItem TEXT NOT NULL UNIQUE);",
                "CREATE TABLE IF NOT EXISTS grupo (cdGrupo INTEGER PRIMARY KEY AUTOINCREMENT, dsGrupo TEXT NOT NULL UNIQUE);",
                "CREATE TABLE IF NOT EXISTS grupoItem (cdGpItem INTEGER PRIMARY KEY AUTOINCREMENT, cdGrupo INTEGER NOT NULL, cdItem INTEGER NOT NULL, cdTraducao INTEGER NOT NULL, cdLang INTEGER NOT NULL);"
        };
        for (String sql : query) {
            try {
                stm = repared(sql);
                stm.execute();
            } catch (SQLException ex) {
                Msg.ServidorErro("Erro ao criar as tabelas", "CREATE_TABLES", Conexao.class, ex);
            } finally {
                desconect();
                try {
                    if (stm != null) {
                        stm.close();
                    }
                } catch (SQLException e) {
                    Msg.ServidorErro("Erro ao criar as tabelas", "CREATE_TABLES", Conexao.class, e);
                }
            }
        }
    }
}
