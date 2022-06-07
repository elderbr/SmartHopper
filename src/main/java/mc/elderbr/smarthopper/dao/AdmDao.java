package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.Config;
import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdmDao {

    public static int STATUS = 0;

    public AdmDao() {
    }

    public static int INSERT(Jogador jogador) {
        try {
            PreparedStatement stm = Conexao.repared("INSERT INTO adm (dsAdm, uuid, type) VALUES (?,?,?)");
            stm.setString(1, jogador.getDsJogador());
            stm.setString(2, jogador.getUUID());
            stm.setInt(3, jogador.getType().getCode());
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                STATUS = 1;
                VGlobal.ADM_LIST.add(jogador.getDsJogador());
                Config.ADD_ADM();// ADICIONA ADMINISTRADOR NA LISTA E ESCREVE NO CONFIG
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                STATUS = 2;
            }
            if (e.getErrorCode() != 19) {
                STATUS = 0;
                Msg.ServidorErro("Erro ao adicionar novo Adm!!!", "INSERT(Jogador jogador)", AdmDao.class, e);
            }
        } finally {
            Conexao.desconect();
        }
        return 0;
    }

    public static Jogador SELECT(Player player) {
        Jogador jogador = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM adm WHERE uuid = ?");
            stm.setString(1, player.getUniqueId().toString());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {

                jogador = new Adm();
                jogador.setCdJogador(rs.getInt("cdAdm"));
                jogador.setDsJogador(rs.getString("dsAdm"));
                jogador.setUUID(rs.getString("uuid"));
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o jogador!!!", "SELECT(Player player)", AdmDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return jogador;
    }

    public static Jogador SELECT(String player) {
        Jogador jogador = null;
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM adm WHERE dsAdm = ?");
            stm.setString(1, player);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {

                jogador = new Adm();
                jogador.setCdJogador(rs.getInt("cdAdm"));
                jogador.setDsJogador(rs.getString("dsAdm"));
                jogador.setUUID(rs.getString("uuid"));
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar o jogador!!!", "SELECT(Player player)", AdmDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return jogador;
    }

    public static List<Jogador> SELECT_ALL() {
        List<Jogador> list = new ArrayList<>();
        try {
            PreparedStatement stm = Conexao.repared("SELECT * FROM adm");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                Jogador jogador = new Adm();
                jogador.setCdJogador(rs.getInt("cdAdm"));
                jogador.setDsJogador(rs.getString("dsAdm"));
                jogador.setUUID(rs.getString("uuid"));
                list.add(jogador);

                // ADICIONANDO NA LISTA DE JOGADOR GLOBAL
                VGlobal.JOGADOR_LIST.add(jogador);
                VGlobal.ADM_LIST.add(jogador.getDsJogador());
            }
            Config.ADD_ADM();// ADICIONA ADMINISTRADOR NA LISTA E ESCREVE NO CONFIG
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao buscar todos os adm!!!", "getListJogador", AdmDao.class, e);
        } finally {
            Conexao.desconect();
        }
        return list;
    }

    public static boolean DELETE(Jogador jogador) {
        try {
            PreparedStatement stm = Conexao.repared("DELETE FROM adm WHERE uuid = ?");
            stm.setString(1, jogador.getUUID());
            if (stm.executeUpdate() > 0) {
                STATUS = 1;
                VGlobal.ADM_LIST.remove(jogador.getDsJogador());
                Config.REMOVER_ADM();// ADICIONA ADMINISTRADOR NA LISTA E ESCREVE NO CONFIG
                return true;
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao remover Adm!!!", "DELETE(Jogador jogador)", AdmDao.class, e);
        } finally {
            Conexao.desconect();
        }
        STATUS = 0;
        return false;
    }
}