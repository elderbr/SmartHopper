package mc.elderbr.smarthopper.dao;

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
import java.util.List;

public class ItemDao {

    private Item item;
    private List<Item> listItem;

    private String sql;
    private PreparedStatement smt;
    private ResultSet rs;


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

        // Verifica se existe item na tabela do banco
        // Se a lista de material for maior que a lista da tabela item
        if (Material.values().length > selectList().size()) {
            // Percorrendo a lista de materias do jogo
            for (Material m : Material.values()) {
                if (m.isItem() && !m.isAir()) {
                    Msg.ServidorGreen("Criando item " + Utils.ToMaterial(m));
                    insert(m);// Adicionando ao banco
                }
            }
        }
    }

    public Item select(Item item) {
        if (item == null) {
            return null;
        }
        if (item.getCdItem() > 0) {
            sql = "SELECT * FROM item WHERE cdItem = " + item.getCdItem();
        } else {
            sql = "SELECT * FROM item WHERE dsItem = " + item.getDsItem();
        }
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCdItem(rs.getInt("cdItem"));
                this.item.setDsItem(rs.getString("dsItem"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexao.desconect();
        }
        return this.item;
    }

    public List<Item> selectList() {
        listItem = new ArrayList<>();
        sql = "SELECT * FROM item ORDER BY dsItem;";
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.item = new Item();
                this.item.setCdItem(rs.getInt("cdItem"));
                this.item.setDsItem(rs.getString("dsItem"));
                listItem.add(this.item);
                VGlobal.LIST_ITEM.add(this.item);// ADICIONANDO NA LISTA GLOBAL
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

    public boolean delete(String name) {
        sql = "DELETE FROM item WHERE dsItem = ?;";
        int retorno = 0;
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, "stone");
            retorno = smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e, "delete", getClass());
        }finally {
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
            smt.setString(1, item.getDsItem());
            smt.setInt(2, item.getCdItem());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e, "update(Item item)", getClass());
        }finally {
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
}
