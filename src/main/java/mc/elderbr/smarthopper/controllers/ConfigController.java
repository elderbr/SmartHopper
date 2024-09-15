package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.exceptions.ConfigException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigController implements VGlobal {

    private static ConfigDao dao = new ConfigDao();

    public ConfigController() {
    }

    public boolean isAdm(Player player) {
        if (player == null) {
            throw new ConfigException("Não existe um player para comparar!");
        }
        return dao.containsAdm(player);
    }

    public static boolean IsAdm(Player player){
        dao = new ConfigDao();
        if(player == null){
            throw new ConfigException("Não existe um player para comparar!");
        }
        return dao.containsAdm(player);
    }

    public int getVersion() {
        if (dao.findByVersion() < VERSION_INT) {
            dao.saveVersion();
        }
        return dao.findByVersion();
    }

    public String getTutorial() {
        String tutorial = dao.findByTutorial();
        if (tutorial == null || tutorial.isBlank()) {
            dao.saveTutorial();
        }
        return dao.findByTutorial();
    }

    public boolean saveUseTexture(Object isUseTexture) {
        try {
            if (isUseTexture instanceof Boolean useTexture) {
                dao.saveUseTexture(useTexture);
                return true;
            }
            if (isUseTexture instanceof String useTexture) {
                boolean use = true;
                if (useTexture.equalsIgnoreCase("true") || useTexture.equalsIgnoreCase("false")) {
                    use = Boolean.getBoolean(useTexture);
                }
                dao.saveUseTexture(use);
                return true;
            }
        } catch (Exception e) {
            throw new ConfigException(e.getMessage());
        }
        return false;
    }

    public String getTexture() {
        try {
            return dao.findByTexture();
        } catch (ConfigException e) {
            dao.saveTexture();
        }
        return dao.findByTexture();
    }

    public boolean getUseTexture() {
        try {
            return dao.findByUseTexture();
        } catch (ConfigException e) {
            dao.saveUseTexture();
        }
        return dao.findByUseTexture();
    }

    public void reset() {
        dao.saveAuthor();
        dao.saveDiscord();
        dao.saveVersion();
        dao.saveTexture();
        dao.saveUseTexture();
        dao.saveAdm();
    }

    public static void RESET() {
        ConfigController ctrl = new ConfigController();
        ConfigDao dao = new ConfigDao();
        AdmController admCtrl = new AdmController();
        try {
            if (dao.findByVersion() < VERSION_INT) {
                dao.saveAuthor();
                dao.saveDiscord();
                dao.saveVersion();
                dao.saveTutorial();
                dao.saveTexture();
                ctrl.getUseTexture();
                for (String adm : admCtrl.getAdms()) {
                    admCtrl.addAdm(adm);
                }
            }
        } catch (ConfigException e) {
            Msg.ServidorGold(e.getMessage());
        }
    }
}
