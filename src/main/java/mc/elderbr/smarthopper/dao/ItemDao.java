package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.Debugs;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDao {

    private Item item;
    private List<Item> listItem;

    private String sql;
    private PreparedStatement smt;
    private ResultSet rs;

    private Map<Where, String> paramenters = new HashMap<>();


    public ItemDao() {

        sql = "CREATE TABLE IF NOT EXISTS item (" +
                "cdItem INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dsItem VARCHAR(30) NOT NULL UNIQUE);";
        try {
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro(e, "ItemDao", getClass());
        } finally {
            Conexao.desconect();
        }

        // PARAMENTROS
        paramenters.put(Where.CODIGO, "WHERE i.cdItem = %d AND l.dsLang = %s");
        paramenters.put(Where.NAME, "WHERE lower(i.dsItem) = lower(%s) AND l.dsLang = %s");
        paramenters.put(Where.TRADUCAO, "WHERE lower(t.dsTraducao) = lower(%s) AND l.dsLang = %s");

    }

    public void create() {
        // Verifica se existe item na tabela do banco
        // Se a lista de material for maior que a lista da tabela item
        Debugs.escrever("Criando itens!!!");
        if (VGlobal.LIST_MATERIAL.size() > selectList().size()) {
            // Percorrendo a lista de materias do jogo
            for (Material m : VGlobal.LIST_MATERIAL) {
                Debugs.escrever("Criando item "+ Utils.ToMaterial(m));
                insert(m);// Adicionando ao banco
            }
        }
    }

    public Item select(Item item) {
        if( (this.item = selectTraducao(item) ) != null ){
            return this.item;
        }
        if( (this.item = selectCodigoTraducao(item))!=null) {
            return this.item;
        }
        if( (this.item = selectCodigo(item))!=null) {
            return this.item;
        }
        return selectName(item);
    }

    public Item selectCodigoTraducao(Item obj) {
        sql = "SELECT * FROM item i " +
                "LEFT JOIN traducao t ON i.cdItem = t.cdItem " +
                "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                "WHERE i.cdItem = ? AND l.dsLang = ?";
        try {
            smt = Conexao.prepared(sql);
            smt.setInt(1, obj.getCodigo());
            smt.setString(2, obj.getDsLang());
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCodigo(rs.getInt("cdItem"));
                this.item.setName(rs.getString("dsItem"));

                // LINGUAGEM
                this.item.setCdLang( rs.getInt("cdLang"));
                this.item.setDsLang(rs.getString("dsLang"));

                // TRADUÇÃO
                this.item.setCdTraducao(rs.getInt("cdTraducao"));
                this.item.setDsTraducao(rs.getString("dsTraducao"));

                return this.item;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o item pelo codigo!", "selectCodigo(Object codigo)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    public Item selectCodigo(Object codigo) {
        item = new Item();
        if (codigo instanceof Integer) {
            item.setCodigo((Integer) codigo);
        }
        if (codigo instanceof Item) {
            item = (Item) codigo;
        }
        sql = "SELECT * FROM item WHERE cdItem = " + item.getCodigo();
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCodigo(rs.getInt("cdItem"));
                this.item.setName(rs.getString("dsItem"));
                return this.item;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o item pelo codigo!", "selectCodigo(Object codigo)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    public Item selectName(Object codigo) {
        item = new Item();
        if (codigo instanceof Integer) {
            item.setCodigo((Integer) codigo);
        }
        if (codigo instanceof Item) {
            item = (Item) codigo;
        }
        sql = "SELECT * FROM item WHERE lower(dsItem) = lower(?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, item.getName());
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCodigo(rs.getInt("cdItem"));
                this.item.setName(rs.getString("dsItem"));
                return this.item;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o item pelo codigo!", "selectCodigo(Object codigo)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    public Item selectTraducao(Item item) {
        try {

            sql = "SELECT * FROM item i " +
                    "LEFT JOIN traducao t ON i.cdItem = t.cdItem " +
                    "LEFT JOIN lang l ON l.cdLang = t.cdLang " +
                    "WHERE lower(t.dsTraducao) = lower(?) AND l.dsLang = ?";
            smt = Conexao.prepared(sql);
            smt.setString(1, item.getName());
            smt.setString(2, item.getDsLang());
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCodigo(rs.getInt("cdItem"));
                this.item.setName(rs.getString("dsItem"));
                // LINGUAGEM
                this.item.setCdLang(rs.getInt("cdLang"));
                this.item.setDsLang(rs.getString("dsLang"));
                // TRADUÇÃO
                this.item.setCdTraducao(rs.getInt("cdTraducao"));
                this.item.setDsTraducao(rs.getString("dsTraducao"));
                return this.item;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o item!!!", "selectTraducao(Item item)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return null;
    }

    public List<Item> selectList() {
        listItem = new ArrayList<>();
        sql = "SELECT * FROM item ORDER BY dsItem;";
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCodigo(rs.getInt("cdItem"));
                this.item.setName(rs.getString("dsItem"));
                listItem.add(this.item);

                // ADICIONANDO NA VARIAVEL GLOBAL
                VGlobal.LIST_ITEM.add(this.item);// ADICIONANDO NA LISTA GLOBAL
                VGlobal.ITEM_ID_MAP.put(item.getCodigo(), item);
                VGlobal.ITEM_NAME_MAP.put(item.getName(), item);

                Msg.ServidorGreen("Lendo o item "+ this.item.getName());
            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "selectList", getClass());
        } finally {
            Conexao.desconect();
        }
        return listItem;
    }

    public int insert(Object item) {

        String name = null;

        if (item == null) {
            return 0;
        }

        if (item instanceof ItemStack) {
            name = Utils.toItem((ItemStack) item);
        }
        if (item instanceof Material) {
            name = Utils.ToMaterial((Material) item);
        }

        sql = "INSERT INTO item (dsItem) VALUES (?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, name);
            return smt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insert(Item item)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public int insertCodigo(Item item) {

        sql = "INSERT INTO item (cdItem, dsItem) VALUES (?,?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setInt(1, item.getCodigo());
            smt.setString(2, item.getName());
            return smt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insertCodigo(Item item)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public boolean delete(String name) {
        sql = "DELETE FROM item WHERE dsItem = ?;";
        int retorno = 0;
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, "stone");
            retorno = smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e, "delete", getClass());
        } finally {
            Conexao.desconect();
        }
        Msg.ServidorGreen("retorno delete item >> " + retorno);
        return true;
    }

    public int update(Item item) {
        if (item == null) {
            return 0;
        }
        sql = "UPDATE TABLE item SET dsItem = ? WHERE cdItem = ?;";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, item.getName());
            smt.setInt(2, item.getCodigo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e, "update(Item item)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getListItem() {
        return listItem;
    }

    public void setListItem(List<Item> listItem) {
        this.listItem = listItem;
    }

    private enum Where {
        CODIGO, NAME, TRADUCAO;
    }
}
