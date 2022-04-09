package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Lang;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public static int INSERT(Item item) {
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

    public static int INSERT(Grupo grupo) {
        try {
            PreparedStatement stm = Conexao.repared("INSERT INTO traducao (cdLang, cdGrupo, dsTraducao) SELECT ?, ?, ? " +
                    "WHERE NOT EXISTS (SELECT 1 FROM traducao WHERE cdLang = ? AND cdGrupo = ?); ");
            stm.setInt(1, grupo.getCdLang());
            stm.setInt(2, grupo.getCdGrupo());
            stm.setString(3, grupo.getDsTraducao());

            stm.setInt(4, grupo.getCdLang());
            stm.setInt(5, grupo.getCdGrupo());
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar tradução!!!", "INSERT(Grupo grupo)", TraducaoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public static boolean UPDATE(Item item) {
        try {
            PreparedStatement stm = Conexao.repared("UPDATE traducao SET dsTraducao = ? WHERE cdTraducao = ?");
            stm.setString(1, item.getDsTraducao());
            stm.setInt(2, item.getCdTraducao());
            if (stm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao atualizar tradução!!!", " UPDATE(Item item)", TraducaoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public static List<Traducao> SELECT(Grupo grupo){
        List<Traducao> list = new ArrayList<>();
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM traducao t " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE cdGrupo = ?");
            stm.setInt(1, grupo.getCdGrupo());
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                Traducao traducao = new Traducao();

                traducao.setCdLang(rs.getInt("cdLang"));
                traducao.setDsLang(rs.getString("dsLang"));

                traducao.setCdTraducao(rs.getInt("cdTraducao"));
                traducao.setDsTraducao(rs.getString("dsTraducao"));

                list.add(traducao);
            }
        }catch (SQLException e){
            Msg.ServidorErro("Erro ao buscar pela a tradução do grupo!!!", "", TraducaoDao.class, e);
        }finally {
            Conexao.desconect();
        }
        return list;
    }

    public static void SELECT_ALL(){
        try{
           PreparedStatement stm = Conexao.repared("SELECT * FROM traducao t " +
                   "LEFT JOIN lang l ON l.cdLang = t.cdLang");
           ResultSet rs = stm.executeQuery();
           while(rs.next()){
               if(rs.getInt("cdItem")>0){

                   Traducao traducao = new Traducao();
                   traducao.setCdTraducao(rs.getInt("cdTraducao"));
                   traducao.setDsTraducao(rs.getString("dsTraducao"));

                   traducao.setCdLang(rs.getInt("cdLang"));
                   traducao.setDsLang(rs.getString("dsLang"));

                   VGlobal.TRADUCAO_MAP_ITEM_NAME.put(rs.getInt("cdItem"), traducao);
               }
               if(rs.getInt("cdGrupo") > 0){
                   Traducao traducao = new Traducao();
                   traducao.setCdTraducao(rs.getInt("cdTraducao"));
                   traducao.setDsTraducao(rs.getString("dsTraducao"));

                   traducao.setCdLang(rs.getInt("cdLang"));
                   traducao.setDsLang(rs.getString("dsLang"));

                   VGlobal.TRADUCAO_MAP_GRUPO_NAME.put(rs.getInt("cdGrupo"), traducao);
               }
           }
        }catch (SQLException e){

        }finally {
            Conexao.desconect();
        }
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
