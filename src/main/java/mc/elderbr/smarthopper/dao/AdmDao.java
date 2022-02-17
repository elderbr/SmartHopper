package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdmDao {

    private Adm adm;

    private PreparedStatement smt;
    private ResultSet rs;
    private String sql;

    private Player jogador;


    public AdmDao() {
        createTable();
        if (selectAll().isEmpty()) {
            adm = new Adm("ElderBR");
            adm.setDsUuid("209f5422-e9b9-4540-b630-dff2ff44116b");
            adm.setCdCargo(1);
            insert(adm);
        }
    }

    public long insert(@NotNull Adm adm) {
        try {
            sql = "INSERT INTO adm (dsAdm, dsUuid, cdCargo) VALUES (?,?,?);";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsAdm());
            smt.setString(2, adm.getDsUuid());
            smt.setInt(3, adm.getCdCargo());
            smt.executeUpdate();
            rs = smt.getGeneratedKeys();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar novo ADM!!!", "insert(Object player)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public Adm select(@NotNull Adm adm) {
        try {
            sql = "SSELECT * FROM adm a LEFT JOIN cargo c ON c.cdCargo = a.cdCargo WHERE dsUuid = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsUuid());
            while (rs.next()) {
                this.adm = new Adm();
                this.adm.setCdAdm(rs.getInt("cdAdm"));
                this.adm.setDsAdm(rs.getString("dsAdm"));
                this.adm.setDsUuid(rs.getString("dsUuid"));
                this.adm.setCdCargo(rs.getInt("cdCargo"));
                this.adm.setDsCargo(rs.getString("dsCargo"));
            }
            return this.adm;
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar ADM!!!", "select(@NotNull Adm adm)", getClass(), e);
        }
        return null;
    }

    private List<Adm> selectAll() {
        try {
            sql = "SELECT * FROM adm a LEFT JOIN cargo c ON c.cdCargo = a.cdCargo;";
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while (rs.next()) {
                adm = new Adm();
                adm.setCdAdm(rs.getInt("cdAdm"));
                adm.setDsAdm(rs.getString("dsAdm"));
                adm.setDsUuid(rs.getString("dsUuid"));
                adm.setCdCargo(rs.getInt("cdCargo"));
                adm.setDsCargo(rs.getString("dsCargo"));
                // VARIAVEIS GLOBAIS
                VGlobal.ADM_LIST.add(adm);
                VGlobal.ADM_UUID.add(adm.getDsUuid());
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao listar todos ADM!!!", "selectAll", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return VGlobal.ADM_LIST;
    }

    public long delete(Object player) {
        adm = new Adm();
        if (player instanceof Player) {
            jogador = ((Player) player);
            adm.setDsAdm(jogador.getName());
            adm.setDsUuid(jogador.getUniqueId().toString());
        } else if (player instanceof Adm) {
            adm = (Adm) player;
        } else {
            return 0;
        }
        try {
            sql = "DELETE FROM adm WHERE dsUuid = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsUuid());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar ADM!!!", "select(Object player)", getClass(), e);
        }
        return 0;
    }

    public long update(@NotNull Object player) {
        adm = new Adm();
        if (player instanceof Player) {
            jogador = ((Player) player);
            adm.setDsAdm(jogador.getName());
            adm.setDsUuid(jogador.getUniqueId().toString());
        } else if (player instanceof Adm) {
            adm = (Adm) player;
        } else {
            return 0;
        }
        try {
            sql = "UPDATE TABLE adm dsAdm = ?, dsUuid = ? WHERE ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsAdm());
            smt.setString(2, adm.getDsUuid());
            smt.setInt(3, adm.getCdAdm());
            return smt.executeUpdate();
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao atualizar ADM!!!", "update(Object player)", getClass(), e);
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    private void createTable() {
        try {
            sql = "CREATE TABLE IF NOT EXISTS adm (cdAdm INTEGER PRIMARY KEY AUTOINCREMENT, dsAdm NVARCHAR(20) NOT NULL UNIQUE, dsUuid TEXT NOT NULL UNIQUE, cdCargo INTEGER NOT NULL);";
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela ADM!!!", "createTable", getClass(), e);
        }
    }


}
