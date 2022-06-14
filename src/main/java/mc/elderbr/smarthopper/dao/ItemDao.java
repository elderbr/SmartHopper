package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ItemDao {

    private PreparedStatement stm;
    private ResultSet rs;
    private String sql;

    public ItemDao() {
    }

    public int insert(Object item) {
        stm = null;
        try {
            if (item instanceof String name) {
                sql = String.format("INSERT INTO item (dsItem) VALUES (%s)", name);
                stm = Conexao.repared(sql);
            }
            if (item instanceof Item material) {
                sql = String.format("INSERT INTO item (dsItem) VALUES (%s)", material.getDsItem());
                stm = Conexao.repared(sql);
            }
            if (stm == null) return 0;
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar item!!!", "insert(Object item)", getClass(), e);
        } finally {
            Conexao.desconect();
            close();
        }
        return 0;
    }

    public static int INSERT(Item item) {
        try {
            PreparedStatement stm = Conexao.repared("INSERT INTO item (cdItem, dsItem) VALUES (?,?)");
            stm.setInt(1, item.getCdItem());
            stm.setString(2, item.getDsItem());
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                int code = rs.getInt(1);
                for (Map.Entry<String, String> values : item.getTraducao().entrySet()) {
                    item.setCdLang(VGlobal.LANG_MAP.get(values.getKey()).getCdLang());
                    item.setDsTraducao(values.getValue());
                    TraducaoDao.INSERT(item);
                }
                return code;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro("Erro ao adicionar novo item", "INSERT(Item item)", ItemDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public static Item SELECT(Item item) {
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM item i " +
                    "LEFT JOIN traducao t ON t.cdItem = i.cdItem " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE i.cdItem = ? AND dsLang = ?");
            stm.setInt(1, item.getCdItem());
            stm.setString(2, item.getDsLang());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Item it = new Item();
                it.setCdItem(rs.getInt("cdItem"));
                it.setDsItem(rs.getString("dsItem"));
                // LANG
                it.setCdLang(rs.getInt("cdLang"));
                it.setDsLang(rs.getString("dsLang"));
                // TRADUÇÃO
                it.setCdTraducao(rs.getInt("cdTraducao"));
                it.setDsTraducao(rs.getString("dsTraducao"));
                return it;
            }
            stm = Conexao.repared("SELECT * FROM item WHERE cdItem = ?");
            stm.setInt(1, item.getCdItem());
            rs = stm.executeQuery();
            if (rs.next()) {
                Item it = new Item();
                it.setCdItem(rs.getInt("cdItem"));
                it.setDsItem(rs.getString("dsItem"));
                return it;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o item!!!", "SELECT(Item item)", ItemDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    public static void selectAll() {
        Item item = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM item");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                item = new Item();
                item.setCdItem(rs.getInt("cdItem"));
                item.setDsItem(rs.getString("dsItem"));

                VGlobal.TRADUCAO_ITEM_LIST.put(item.getDsItem().toLowerCase(), item);

                for (Traducao traducao : TraducaoDao.SELECT(item)) {
                    item.addTraducao(traducao.getDsLang(), traducao.getDsTraducao());
                    VGlobal.TRADUCAO_ITEM_LIST.put(traducao.getDsTraducao().toLowerCase(), item);
                }

                // ADICIONANDO NO OBJETO GLOBAL
                VGlobal.ITEM_MAP_ID.put(item.getCdItem(), item);
                VGlobal.ITEM_MAP_NAME.put(item.getDsItem(), item);
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao carregar todos os itens do banco!!!", "", ItemDao.class, e);
        } finally {
            Conexao.desconect();
        }
    }

    public static void CreateDefault() {
        // VERIFICA SE EXISTE MAIS ITENS DO JOGO DO QUE NO BANCO
        Debug.Write("Criando a tabela de item");
        for (String item : VGlobal.ITEM_NAME_LIST) {
            try {
                Debug.WriteMsg("Criando o item " + item);
                PreparedStatement stm = Conexao.repared("INSERT INTO item (dsItem) VALUES (?)");
                stm.setString(1, item);
                stm.execute();
            } catch (SQLException e) {
                if (e.getErrorCode() != 19)
                    Msg.ServidorErro("Erro ao criar item padrão!!!", "", ItemDao.class, e);
            } finally {
                Conexao.desconect();
            }
        }
        Config.SetUpdateItem(true);// ALTERA A ATUALIZAÇÃO DO ITEM PARA VERDADEIRO
        Debug.Write("Tabela de item criadas");
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