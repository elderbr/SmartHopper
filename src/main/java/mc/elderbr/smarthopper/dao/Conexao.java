package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.utils.Msg;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {

    public static Connection conexao;

    public static Connection connected() {

        String url = "jdbc:sqlite:plugins/SmartHopper/smarthopper.db";
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.setEncoding(SQLiteConfig.Encoding.UTF8);
            conexao = DriverManager.getConnection(url, config.toProperties());
            return conexao;
        } catch (SQLException e) {
            e.printStackTrace();
            Msg.ServidorErro(e, "connected", Conexao.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Msg.ServidorErro(e, "connected", Conexao.class);

        }
        return null;
    }

    public static PreparedStatement prepared(String sql) throws SQLException {
        connected();
        return conexao.prepareStatement(sql);
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

    public static boolean create(String sql) throws SQLException {
        connected();
        PreparedStatement smt = conexao.prepareStatement(sql);
        return smt.execute();
    }

}
