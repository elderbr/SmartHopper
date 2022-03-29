package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoDao {

    public GrupoDao() {
    }

    public static void CREATE_GRUPO(){
        for(Grupo grupo : VGlobal.GRUPO_LIST){
            Msg.ServidorGold("Grupo nome >>> "+ grupo.getDsGrupo()+"\nLista de item \n"+ grupo.getListItem().toString());
            Msg.PularLinha(GrupoDao.class);
        }
    }

    public static void SELECT_ALL(){
        Grupo grupo = null;
        try {
           PreparedStatement stm = Conexao.repared("SELECT * FROM grupo g " +
                   "LEFT JOIN traducao t ON t.cdGrupo = g.cdGrupo " +
                   "LEFT JOIN lang l ON l.cdLang = t.cdLang");
            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                grupo = new Grupo();
                grupo.setCdGrupo(rs.getInt("cdGrupo"));
                grupo.setDsGrupo(rs.getString("dsGrupo"));
            }

            while(rs.next()){
                if(!grupo.getDsGrupo().equals(rs.getString("dsGrupo"))){
                    grupo = new Grupo();
                    grupo.setCdGrupo(rs.getInt("cdGrupo"));
                    grupo.setDsGrupo(rs.getString("dsGrupo"));
                }
                grupo.addTraducao(rs.getString("dsLang"), rs.getString("dsTraducao"));
            }
        }catch (SQLException e){
            Msg.ServidorErro("Erro ao selecionar todos os grupos!!!", "SELECT_ALL", GrupoDao.class, e);
        }finally {
            Conexao.desconect();
        }
    }
}
