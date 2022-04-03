package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                Debug.WriteMsg("Adicionado o grupo " + grupo.getDsGrupo());
                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    grupo.setCdGrupo(rs.getInt(1));
                    INSERT_GRUPO_ITEM(grupo);
                }
            } catch (SQLException e) {
                if (e.getErrorCode() != 19)
                    Msg.ServidorErro("Erro ao adicionar grupo!!!", "CREATE_GRUPO", GrupoDao.class, e);
            } finally {
                Conexao.desconect();
            }
        }
        Debug.WriteMsg("Fim de adicionado o grupo no banco");
    }

    public static void INSERT_GRUPO_ITEM(Grupo grupo) {
        for (Item item : grupo.getListItem()) {
            try {
                PreparedStatement stm = Conexao.repared("INSERT INTO grupoItem (cdGrupo, cdItem) VALUES (?,?);");
                stm.setInt(1, grupo.getCdGrupo());
                stm.setInt(2, item.getCdItem());
                stm.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Conexao.desconect();
            }
        }
    }

    public static List<Grupo> SELECT_GRUPO_ITEM(Item item) {
        List<Grupo> list = new ArrayList<>();
        Grupo grupo = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM grupoItem gi " +
                    "LEFT JOIN grupo g ON g.cdGrupo= gi.cdGrupo " +
                    "LEFT JOIN traducao t ON t.cdGrupo = g.cdGrupo " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE gi.cdItem = ?");
            stm.setInt(1, item.getCdItem());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                // LANG
                grupo.setCdLang(rs.getInt("cdLang"));
                grupo.setDsLang(rs.getString("dsLang"));
                // Tradução
                grupo.setCdTraducao(rs.getInt("cdTraducao"));
                grupo.setDsTraducao(rs.getString("dsTraducao"));

                list.add(grupo);

            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao pesquisar por grupo que contém o item!!!", "", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return list;
    }

    public static void SELECT_ALL() {
        Grupo grupo = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT gi.cdGpItem, g.cdGrupo, g.dsGrupo, i.cdItem, i.dsItem, l.cdLang, l.dsLang, t.cdTraducao, t.dsTraducao FROM grupoItem gi " +
                    "LEFT JOIN grupo g ON g.cdGrupo = gi.cdGrupo " +
                    "LEFT JOIN item i ON i.cdItem = gi.cdItem " +
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

                    VGlobal.GRUPO_MAP_ID.put(grupo.getCdGrupo(), grupo);
                    VGlobal.GRUPO_MAP_NAME.put(grupo.getDsGrupo(), grupo);

                    grupo = new Grupo();
                    grupo.setCdGrupo(rs.getInt("cdGrupo"));
                    grupo.setDsGrupo(rs.getString("dsGrupo"));
                }

                // ADICIONANDO AS TRADUÇÕES DO GRUPO
                grupo.addTraducao(rs.getString("dsLang"), rs.getString("dsTraducao"));

                // ADICIONANDO ITEM NA LISTA DO GRUPO
                Item item = VGlobal.ITEM_MAP_ID.get(rs.getInt("cdItem"));
                if (!grupo.getListItem().contains(item)) {
                    grupo.addList(item);
                }


            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao selecionar todos os grupos!!!", "SELECT_ALL", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
    }
}
