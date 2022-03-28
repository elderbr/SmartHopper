package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TraducaoDao {

    private PreparedStatement stm;
    private ResultSet rs;
    private String sql;

    private TraducaoConfig traducaoConfig = new TraducaoConfig();
    private Traducao traducao;
    private Item item;

    public TraducaoDao() {
    }

    public int insert(Object value) {
        try {
            if (value instanceof Item item) {
                stm = Conexao.repared("INSERT INTO traducao (cdLang, cdItem, dsTraducao) SELECT ?, ?, ? " +
                        "WHERE NOT EXISTS (SELECT 1 FROM traducao WHERE cdLang = ? AND cdItem = ?); ");
                stm.setInt(1, item.getCdLang());
                stm.setInt(2, item.getCdItem());
                stm.setString(3, item.getDsTraducao());

                stm.setInt(4, item.getCdLang());
                stm.setInt(5, item.getCdItem());
                stm.execute();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            if (value instanceof Grupo grupo) {
                stm = Conexao.repared("INSERT INTO traducao (cdLang, cdGrupo, dsTraducao) SELECT ?, ?, ? " +
                        "WHERE NOT EXISTS (SELECT 1 FROM traducao WHERE cdLang = ? AND cdGrupo = ?); ");
                stm.setInt(1, item.getCdLang());
                stm.setInt(2, item.getCdItem());
                stm.setString(3, item.getDsTraducao());

                stm.setInt(4, item.getCdLang());
                stm.setInt(5, item.getCdItem());
                stm.execute();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar tradução!!!", "insert(Object value)", TraducaoDao.class, e);
        } finally {
            Conexao.desconect();
            close();
        }
        return 0;
    }

    private static int INSERT(Item item) {
        try {
            PreparedStatement stm = Conexao.repared("INSERT INTO traducao (cdLang, cdItem, dsTraducao) SELECT ?, ?, ? " +
                    "WHERE NOT EXISTS (SELECT 1 FROM traducao WHERE cdLang = ? AND cdItem = ?); ");
            stm.setInt(1, item.getCdLang());
            stm.setInt(2, item.getCdItem());
            stm.setString(3, item.getDsTraducao());

            stm.setInt(4, item.getCdLang());
            stm.setInt(5, item.getCdItem());
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar tradução!!!", "INSERT(Item item)", TraducaoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public static void createBR() {
        Debug.WriteMsg("Criando tradução português do Brasil");
        TraducaoConfig traducaoConfig = new TraducaoConfig();
        for (Map.Entry<String, Object> values : traducaoConfig.configBR().getValues(false).entrySet()) {
            Item item = VGlobal.ITEM_MAP_NAME.get(values.getKey());
            if (item != null) {
                item.setCdLang(2);
                item.setDsTraducao(values.getValue().toString());
                if (INSERT(item) > 0) {
                    Debug.WriteMsg("Criando traducao para o item " + item.getDsItem() + " tradução: " + item.getDsTraducao());
                }
            }
        }
        Debug.WriteMsg("Fim de criar tradução português do Brasil");
    }

    public static void createPT() {
        Debug.WriteMsg("Criando tradução português de Portugal");
        TraducaoConfig traducaoConfig = new TraducaoConfig();
        for (Map.Entry<String, Object> values : traducaoConfig.configPT().getValues(false).entrySet()) {
            Item item = VGlobal.ITEM_MAP_NAME.get(values.getKey());
            if (item != null) {
                item.setCdLang(3);
                item.setDsTraducao(values.getValue().toString());
                if (INSERT(item) > 0) {
                    Debug.WriteMsg("Criando traducao para o item " + item.getDsItem() + " tradução: " + item.getDsTraducao());
                }
            }
        }
        Debug.WriteMsg("Fim de criar tradução português de Portugal");
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
            Msg.ServidorErro("Erro ao fechar a conexão!!!", "", TraducaoDao.class, e);
        }
    }
}
