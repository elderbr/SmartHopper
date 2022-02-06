package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrupoDao {

    private Grupo grupo;
    private List<Grupo> listGrupo;

    private List<String> listNames;
    private List<String> listItem;
    private int contato;

    private String sql;
    private PreparedStatement smt;
    private ResultSet rs;

    public GrupoDao() {

        sql = "CREATE TABLE IF NOT EXISTS grupo (" +
                "cdGrupo INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", dsGrupo VARCHAR(20) NOT NULL UNIQUE);";
        try {
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela grupo!", "GrupoDao", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        createGrupo();
    }

    public void createGrupo() {

        // NOMES DOS GRUPOS
        listNames = new ArrayList<>();
        // PERCORRENDO A LISTA DE MATERIAL PADÃO DO MINECRAFT
        for (Material m : Material.values()) {
            if(m.isItem() && !m.isAir()) {
                // CONVERTENDO O NOME DO MATERIAL REMOVENDO O ANDERLAINE
                // ARRAY DO NOME DO MATERIAL SEPARANDO PELO O ESPAÇO
                String[] itens = Utils.ToMaterial(m).split(" ");
                String name = null;

                // ADICIONA NA LISTA DE NOME SE O ITEM ESTIVER MAIS DE UM NOME E
                // NÃO ESTIVER NA LISTA NOME ADICIONA
                switch (itens.length) {
                    case 2:
                        name = itens[0];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[1];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        break;
                    case 3:
                        name = itens[0] + " " + itens[1];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[1] + " " + itens[2];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        break;
                    case 4:
                        name = itens[0] + " " + itens[1];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[1] + " " + itens[2];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[2] + " " + itens[3];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        break;
                    case 5:
                        name = itens[0] + " " + itens[1];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[1] + " " + itens[2];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[2] + " " + itens[3];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[0] + " " + itens[1] + " " + itens[2];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        name = itens[1] + " " + itens[2] + " " + itens[3];
                        if (!listNames.contains(name)) {
                            listNames.add(name);
                        }
                        break;
                }
            }
        }
        // NOME DO GRUPO É VALIDO SE EXISTIR MAIS DE UM ITEM COM O NOME
        contato = 0;
        listItem = new ArrayList<>();
        for(String names : listNames){
            for (Material m : Material.values()) {
                if(m.isItem()) {
                    if (Utils.ToMaterial(m).contains(names)) {
                        if (contato > 1 && !listItem.contains(names)) {
                            listItem.add(names);
                            break;
                        }
                        contato++;
                    }
                }
            }
            contato = 0;
        };

        // LISTA VALIDA DO NOME DO GRUPO
        listItem.forEach(names->{
            grupo = new Grupo();
            grupo.setDsGrupo(names);
            Msg.ServidorGreen("Criando o grupo "+ grupo.getDsGrupo());
            insert(grupo);
        });
    }

    public int insert(Grupo grupo) {
        sql = "INSERT INTO grupo (dsGrupo) VALUES (?);";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsGrupo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19)
                Msg.ServidorErro(e, "insert(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    private void insertName(String name) {

        grupo = new Grupo();
        grupo.setDsGrupo(name);
        if (insert(grupo) > 0) {
            Msg.ServidorGreen("grupo item >> " + name);
        }
    }

    public Grupo select(Grupo grupo) {
        this.grupo = null;
        if (grupo == null) {
            return null;
        }
        if (grupo.getCdGrupo() > 0) {
            sql = "SELECT * FROM grupo WHERE cdGrupo = " + grupo.getCdGrupo();
        } else {
            sql = "SELECT * FROM grupo WHERE dsGrupo = " + grupo.getDsGrupo();
        }
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.grupo = new Grupo();
                this.grupo.setCdGrupo(rs.getInt("cdGrupo"));
                this.grupo.setDsGrupo(rs.getString("dsGrupo"));
            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "select(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        return this.grupo;
    }

    public List<Grupo> selectAll() {
        listGrupo = new ArrayList<>();
        sql = "SELECT * FROM grupo";
        try {
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                this.grupo = new Grupo();
                this.grupo.setCdGrupo(rs.getInt("cdGrupo"));
                this.grupo.setDsGrupo(rs.getString("dsGrupo"));
                listGrupo.add(this.grupo);
            }
        } catch (SQLException e) {
            Msg.ServidorErro(e, "selectAll()", getClass());
        }
        return listGrupo;
    }

    public boolean delete(Grupo grupo) {
        if (grupo == null && grupo.getCdGrupo() > 0) {
            return false;
        }
        sql = "DELETE FROM grupo WHERE cdGrupo = " + grupo.getCdGrupo() + ";";
        try {
            smt = Conexao.prepared(sql);
            int retorno = smt.executeUpdate();
            if (retorno > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexao.desconect();
        }
        return false;
    }

    public int update(Grupo grupo) {
        if (grupo == null) {
            return 0;
        }
        sql = "UPDATE TABLE grupo SET dsGrupo = ? WHERE cdGrupo = ?";
        try {
            smt = Conexao.prepared(sql);
            smt.setString(1, grupo.getDsGrupo());
            smt.setInt(2, grupo.getCdGrupo());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro(e, "update(Grupo grupo)", getClass());
        } finally {
            Conexao.desconect();
        }
        return 0;
    }


}
