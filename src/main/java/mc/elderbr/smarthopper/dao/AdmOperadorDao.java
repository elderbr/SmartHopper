package mc.elderbr.smarthopper.dao;

import com.google.gson.JsonArray;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdmOperadorDao {

    private Adm adm;

    private PreparedStatement smt;
    private ResultSet rs;
    private String sql;

    private Player jogador;


    public AdmOperadorDao() {
        createTable();
        if(selectAll().isEmpty()){
            adm = new Adm("ElderBR");
            adm.setDsUuid("5e3e3142-7bff-4050-8809-e6944c697bd8");
            insert(adm);
        }
    }

    public long insert(@NotNull Object player) {
        adm = new Adm();
        if(player instanceof Player){
            jogador = ((Player) player);
            adm.setDsAdm(jogador.getName());
            adm.setDsUuid(jogador.getUniqueId().toString());
        }else
        if(player instanceof Adm){
            adm = (Adm) player;
        }else{
            return 0;
        }
        try {
            sql = "INSERT INTO adm (dsAdm, dsUuid) VALUES (?,?);";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsAdm());
            smt.setString(2, adm.getDsUuid());
            smt.executeUpdate();
            rs = smt.getGeneratedKeys();
            while(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao adicionar novo ADM!!!", "insert(Object player)", getClass(), e);
        }finally {
            Conexao.desconect();
        }
        return 0;
    }

    public Adm select(Object player){
        adm = new Adm();
        if(player instanceof Player){
            jogador = ((Player) player);
            adm.setDsAdm(jogador.getName());
            adm.setDsUuid(jogador.getUniqueId().toString());
        }else
        if(player instanceof Adm){
            adm = (Adm) player;
        }else{
            return null;
        }
        try{
            sql = "SELECT * FROM adm WHERE dsUuid = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsUuid());
            whileAdm();
            return adm;
        }catch (SQLException e){
            Msg.ServidorErro("Erro ao buscar ADM!!!", "select(Object player)", getClass(), e);
        }
        return null;
    }

    private List<Adm> selectAll() {
        try {
            sql = "SELECT * FROM adm;";
            smt = Conexao.prepared(sql);
            rs = smt.executeQuery();
            while(rs.next()){
                adm = new Adm();
                adm.setCdAdm(rs.getInt("cdAdm"));
                adm.setDsAdm(rs.getString("dsAdm"));
                adm.setDsUuid(rs.getString("dsUuid"));
                VGlobal.ADM_LIST.add(adm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Conexao.desconect();
        }
        return VGlobal.ADM_LIST;
    }

    public long delete(Object player){
        adm = new Adm();
        if(player instanceof Player){
            jogador = ((Player) player);
            adm.setDsAdm(jogador.getName());
            adm.setDsUuid(jogador.getUniqueId().toString());
        }else
        if(player instanceof Adm){
            adm = (Adm) player;
        }else{
            return 0;
        }
        try{
            sql = "DELETE FROM adm WHERE dsUuid = ?;";
            smt = Conexao.prepared(sql);
            smt.setString(1, adm.getDsUuid());
            return smt.executeUpdate();
        }catch (SQLException e){
            Msg.ServidorErro("Erro ao buscar ADM!!!", "select(Object player)", getClass(), e);
        }
        return 0;
    }

    public long update(@NotNull Object player) {
        adm = new Adm();
        if(player instanceof Player){
            jogador = ((Player) player);
            adm.setDsAdm(jogador.getName());
            adm.setDsUuid(jogador.getUniqueId().toString());
        }else
        if(player instanceof Adm){
            adm = (Adm) player;
        }else{
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
        }finally {
            Conexao.desconect();
        }
        return 0;
    }

    private void createTable() {
        try {
            sql = "CREATE TABLE IF NOT EXISTS adm (cdAdm INTEGER PRIMARY KEY AUTOINCREMENT, dsAdm NVARCHAR(20) NOT NULL UNIQUE, dsUuid TEXT NOT NULL UNIQUE);";
            Conexao.create(sql);
        } catch (SQLException e) {
            Msg.ServidorErro("Erro ao criar a tabela ADM!!!", "createTable", getClass(), e);
        }
    }

    private void whileAdm() throws SQLException {
        while(rs.next()){
            adm = new Adm();
            adm.setCdAdm(rs.getInt("cdAdm"));
            adm.setDsAdm(rs.getString("dsAdm"));
            adm.setDsUuid(rs.getString("dsUuid"));
        }
    }


}
