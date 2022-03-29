package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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

    public Item select(Item item) {
        try {
            stm = Conexao.repared("SELECT * FROM item i " +
                    "LEFT JOIN traducao t ON t.cdItem = i.cdItem " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE i.cdItem = ? AND dsLang = ?");
            stm.setInt(1, item.getCdItem());
            stm.setString(2, item.getDsLang());
            rs = stm.executeQuery();
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

        } finally {
            Conexao.desconect();
            close();
        }
        return null;
    }

    public static void selectAll() {
        Item item = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM item i " +
                    "LEFT JOIN traducao t ON t.cdItem = i.cdItem " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang");
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                item = new Item();
                item.setCdItem(rs.getInt("cdItem"));
                item.setDsItem(rs.getString("dsItem"));
            }

            while (rs.next()) {

                if (!item.getDsItem().equals(rs.getString("dsItem"))) {

                    // ADICIONANDO NO OBJETO GLOBAL
                    VGlobal.ITEM_MAP_ID.put(item.getCdItem(), item);
                    VGlobal.ITEM_MAP_NAME.put(item.getDsItem(), item);

                    item = new Item();
                    item.setCdItem(rs.getInt("cdItem"));
                    item.setDsItem(rs.getString("dsItem"));
                }

                // LANG
                if (rs.getString("dsLang") != null) {
                    item.addTraducao(rs.getString("dsLang"), rs.getString("dsTraducao"));
                }
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao carregar todos os itens do banco!!!", "", ItemDao.class, e);
        } finally {
            Conexao.desconect();
        }
    }

    public static int size() {
        try {
            PreparedStatement preparedStatement = Conexao.repared("SELECT count(cdItem) FROM item");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao verificar o tanho da lista de item!!!", "", ItemDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }


    public static void CreateDefault() {
        if (VGlobal.ITEM_NAME_LIST.size() > size() + 1) {
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
            Debug.Write("Tabela de item criadas");
        }
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
