package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
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
        // VERIFICA SE O TAMANHO DA LISTA DE GRUPO É MAIOR QUE A LISTA DO BANCO DE DADOS
        if(VGlobal.GRUPO_LIST.size() > VGlobal.GRUPO_MAP_NAME.size()) {
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
            Config.SetUpdateGrupo(true);
            Debug.WriteMsg("Fim de adicionado o grupo no banco");
        }
    }

    public static boolean INSERT(Grupo grupo) {
        try {
            PreparedStatement stm = Conexao.repared("INSERT INTO grupo (dsGrupo) VALUES (?)");
            stm.setString(1, grupo.getDsGrupo());
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                grupo.setCdGrupo(rs.getInt(1));
                int codeTraduca = TraducaoDao.INSERT(grupo);
                INSERT_GRUPO_ITEM(grupo);

                // ADICIONANDO A VARIAL GLOBAL
                VGlobal.GRUPO_MAP_ID.put(grupo.getCdGrupo(), grupo);// ADICIONANDO A BUSCA PELO ID
                VGlobal.GRUPO_MAP_NAME.put(grupo.getDsGrupo(), grupo);// ADICIONANDO A BUSCA PELO NOME
                VGlobal.GRUPO_NAME_LIST.add(grupo.getDsGrupo());// ADICIONANDO NA LISTA DE NOMES DO GRUPO
                VGlobal.GRUPO_MAP.put(grupo.getDsGrupo(), grupo.getDsGrupo());// ADICIONANDO NA LISTA DE LANG TRADUZIDO

                VGlobal.TRADUCAO_GRUPO_LIST.put(grupo.getDsGrupo(), grupo);

                Traducao traducao = new Traducao();
                traducao.setCdTraducao(codeTraduca);
                traducao.setDsTraducao(grupo.getDsTraducao());
                traducao.setCdLang(grupo.getCdLang());
                VGlobal.TRADUCAO_MAP_GRUPO_NAME.put(grupo.getCdGrupo(), traducao);

                return true;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar grupo!!!", "INSERT(Grupo grupo)", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public static boolean INSERT_ID(Grupo grupo) {
        try {
            PreparedStatement stm = Conexao.repared("INSERT INTO grupo (cdGrupo, dsGrupo) VALUES (?,?)");
            stm.setInt(1, grupo.getCdGrupo());
            stm.setString(2, grupo.getDsGrupo());
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {

                grupo.setCdGrupo(rs.getInt(1));

                // Salvando os itens do grupo
                INSERT_GRUPO_ITEM(grupo);

                // ADICIONANDO A VARIAL GLOBAL
                VGlobal.GRUPO_MAP_ID.put(grupo.getCdGrupo(), grupo);// ADICIONANDO A BUSCA PELO ID
                VGlobal.GRUPO_MAP_NAME.put(grupo.getDsGrupo(), grupo);// ADICIONANDO A BUSCA PELO NOME
                VGlobal.GRUPO_NAME_LIST.add(grupo.getDsGrupo());// ADICIONANDO NA LISTA DE NOMES DO GRUPO
                VGlobal.GRUPO_MAP.put(grupo.getDsGrupo(), grupo.getDsGrupo());// ADICIONANDO NA LISTA DE LANG TRADUZIDO

                return true;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar grupo!!!", "INSERT_ID(Grupo grupo)", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public static boolean DELETE(Grupo grupo) {
        try {
            PreparedStatement stm = Conexao.repared("DELETE FROM grupo WHERE cdGrupo = ?");
            stm.setInt(1, grupo.getCdGrupo());
            if (stm.executeUpdate() > 0) {
                DELETE_GRUPO_ITEM(grupo);

                VGlobal.GRUPO_MAP_ID.remove(grupo.getCdGrupo());
                VGlobal.GRUPO_MAP_NAME.remove(grupo.getDsGrupo());
                VGlobal.GRUPO_NAME_LIST.remove(grupo.getDsGrupo());

                return true;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao deleta o grupo!!!", "DELETE(Grupo grupo)", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public static boolean DELETE_GRUPO_ITEM(Grupo grupo) {
        try {
            PreparedStatement stm = Conexao.repared("DELETE FROM grupoItem WHERE cdGrupo = ?");
            stm.setInt(1, grupo.getCdGrupo());
            if (stm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao deleta os item do grupo!!!", "DELETE_GRUPO_ITEM(Grupo grupo)", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public static void INSERT_GRUPO_ITEM(Grupo grupo) {
        for (Item item : grupo.getListItem()) {
            try {
                PreparedStatement stm = Conexao.repared("INSERT INTO grupoItem (cdGrupo, cdItem) VALUES (?,?);");
                stm.setInt(1, grupo.getCdGrupo());
                stm.setInt(2, item.getCdItem());
                stm.execute();
            } catch (SQLException e) {
                Msg.ServidorErro("Erro ao adicionar item do grupo!!!", "", GrupoDao.class, e);
            } finally {
                Conexao.desconect();
            }
        }
    }

    public static List<Grupo> SELECT_CONTEM(String grupoName) {
        List<Grupo> list = new ArrayList<>();
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM grupo g " +
                    "LEFT JOIN traducao t ON t.cdGrupo = g.cdGrupo " +
                    "WHERE lower(g.dsGrupo) LIKE lower(?) OR lower(t.dsTraducao) LIKE lower(?)");
            stm.setString(1, "%" + grupoName + "%");
            stm.setString(2, "%" + grupoName + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                Grupo grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
                // Tradução
                for (Traducao traducao : TraducaoDao.SELECT(grupo)) {
                    grupo.addTraducao(traducao);
                }
                list.add(grupo);

            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao pesquisar por grupo que contém o nome do grupo ou a tradução!!!", "SELECT_CONTEM(Grupo grupo)", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return list;
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
            PreparedStatement stm = Conexao.repared("SELECT * FROM grupo");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));

                VGlobal.TRADUCAO_GRUPO_LIST.put(grupo.getDsGrupo().toLowerCase(), grupo);

                // ADICIONANDO OS ITENS DO GRUPO
                for (Item item : SELECT_GRUPO_ITEM(grupo)) {
                    grupo.addList(item);
                }

                // ADICIONANDO A TRADUÇÃO DO GRUPO
                for (Traducao traducao : TraducaoDao.SELECT(grupo)) {
                    grupo.addTraducao(traducao);
                    VGlobal.TRADUCAO_GRUPO_LIST.put(traducao.getDsTraducao().toLowerCase(), grupo);
                }

                VGlobal.GRUPO_NAME_LIST.add(grupo.getDsGrupo());
                VGlobal.GRUPO_LIST.add(grupo);
                VGlobal.GRUPO_MAP_ID.put(grupo.getCdGrupo(), grupo);
                VGlobal.GRUPO_MAP_NAME.put(grupo.getDsGrupo(), grupo);

            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao selecionar todos os grupos!!!", "SELECT_ALL", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
    }

    public static List<Item> SELECT_GRUPO_ITEM(Grupo grupo) {
        List<Item> list = new ArrayList<>();
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM grupoItem gi " +
                    "LEFT JOIN item i ON i.cdItem = gi.cdItem " +
                    "WHERE cdGrupo = ?");
            stm.setInt(1, grupo.getCdGrupo());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setCdItem(rs.getInt("cdItem"));
                item.setDsItem(rs.getString("dsItem"));
                list.add(item);
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar os item do grupo!!!", "", GrupoDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return list;
    }
}