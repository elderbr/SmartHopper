package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TraducaoDao {

    private PreparedStatement stm;
    private ResultSet rs;
    private String sql;

    TraducaoConfig traducaoConfig = new TraducaoConfig();

    public TraducaoDao() {
    }

    public void createBR() {
        for (Map.Entry<String, Object> keys : traducaoConfig.configBR().getValues(false).entrySet()) {

        }
    }

    public int insert(Object value) {
        try {
            if (value instanceof Item item) {
                stm = Conexao.repared("INSERT INTO traducao (cdLang, cdItem, dsTraducao) VALUES (?,?,?)");
                stm.setInt(1, item.getCdLang());
                stm.setInt(2, item.getCdItem());
                stm.setString(3, item.getDsTraducao());
                stm.execute();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            if (value instanceof Grupo grupo) {
                stm = Conexao.repared("INSERT INTO traducao (cdLang, cdGrupo, dsTraducao) VALUES (?,?,?)");
                stm.setInt(1, grupo.getCdLang());
                stm.setInt(2, grupo.getCdGrupo());
                stm.setString(3, grupo.getDsTraducao());
                stm.execute();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar tradução!!!", "insert(Object value)", getClass(), e);
        }
        return 0;
    }

    private void close() {
        try {
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao fechar a conexão!!!", "", getClass(), e);
        }
    }
}
