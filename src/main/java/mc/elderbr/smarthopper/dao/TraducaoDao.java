package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.file.TraducaoConfig;
import mc.elderbr.smarthopper.model.Traducao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TraducaoDao {

    private PreparedStatement stm;
    private ResultSet rs;
    private String sql;

    TraducaoConfig traducaoConfig = new TraducaoConfig();

    public TraducaoDao() {
    }

    public void createBR(){
        for(Map.Entry<String, Object> keys : traducaoConfig.configBR().getValues(false).entrySet()){

        }
    }

    public int insert(Traducao traducao){
        try {
            stm = Conexao.repared("INSERT INTO traducao () VALUES ()");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
