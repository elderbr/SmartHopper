package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoDao {

    public GrupoDao() {
    }

    public static void CREATE_GRUPO() {
        Debug.WriteMsg("Adicionado o grupo no banco");
        for (Grupo grupo : VGlobal.GRUPO_LIST) {
            try {
                PreparedStatement stm = Conexao.repared("INSERT INTO grupo (dsGrupo) VALUES (?)");
                stm.setString(1, grupo.getDsGrupo());
                stm.execute();
                Debug.WriteMsg("Adicionado o grupo "+ grupo.getDsGrupo());
                ResultSet rs = stm.getGeneratedKeys();
                if(rs.next()){
                    grupo.setCdGrupo(rs.getInt(1));
                    INSERT_GRUPO_ITEM(grupo);
                }
            } catch (SQLException e) {
                if(e.getErrorCode()!=19)
                    Msg.ServidorErro("Erro ao adicionar grupo!!!", "CREATE_GRUPO", GrupoDao.class, e);
            }finally {
                Conexao.desconect();
            }
        }
        Debug.WriteMsg("Fim de adicionado o grupo no banco");
    }

    public static void INSERT_GRUPO_ITEM(Grupo grupo){
        for(Item item : grupo.getListItem()){
            try {
                PreparedStatement stm = Conexao.repared("INSERT INTO grupoItem (cdGrupo, cdItem) VALUES (?,?);");
                stm.setInt(1, grupo.getCdGrupo());
                stm.setInt(2, item.getCdItem());
                stm.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                Conexao.desconect();
            }
        }
    }

    public static void SELECT_ALL() {
        Grupo grupo = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM grupo g " +
                    "LEFT JOIN traducao t ON t.cdGrupo = g.cdGrupo " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang");
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
            }

            while (rs.next()) {
                if (!grupo.getDsGrupo().equals(rs.getString("dsGrupo"))) {
                    grupo = new Grupo();
                    grupo.setCdGrupo(rs.getInt("cdGrupo"));
                    grupo.setDsGrupo(rs.getString("dsGrupo"));
                }
                grupo.addTraducao(rs.getString("dsLang"), rs.getString("dsTraducao"));
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao selecionar todos os grupos!!!", "SELECT_ALL", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
    }
}
