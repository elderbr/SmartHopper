package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.exceptions.ConfigException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdmController implements VGlobal {

    private ConfigDao configDao = new ConfigDao();

    public AdmController() {
    }

    public boolean addAdm(String name){
        if(name == null || name.isBlank()){
            throw new ConfigException("Nome do adm é invalido!");
        }
        return configDao.addAdm(name);
    }

    public boolean addAdm(Player player, String[] args) throws Exception {
        if (!player.isOp()) {
            throw new RuntimeException("Ops, você não pode usar esse comando, apenas adm do servidor!!!");
        }
        if (args == null || args.length < 1) {
            throw new RuntimeException("Digite o nome do player!!!");
        }
        if (args.length > 1) {
            throw new RuntimeException("Digite apenas um nome!!!");
        }
        String name = args[0];
        if (configDao.containsAdm(name)) {
            throw new RuntimeException(String.format("O jogador %s já está na lista de administradores do Smart Hopper!!!", name));
        }
        configDao.addAdm(name);
        return configDao.containsAdm(name);
    }

    public List<String> getAdms() {
        List<String> adms = configDao.findFileByAdms();
        if(adms == null || adms.isEmpty()){
            configDao.saveAdm();
            return configDao.findFileByAdms();
        }
        return adms;
    }

    public boolean removeAdm(Player player, String[] args) {
        if (!player.isOp()) {
            throw new ConfigException("$r$lOps, você não pode usar esse comando, apenas adm do servidor!!!");
        }
        if (args == null || args.length < 1) {
            throw new ConfigException("$lDigite o nome do player!!!");
        }
        if (args.length > 1) {
            throw new ConfigException("$lDigite apenas um nome!!!");
        }
        String name = args[0];
        if (name.length() < 5) {
            throw new ConfigException("$lNome do player invalido!!!");
        }
        if (!configDao.containsAdm(name)) {
            throw new ConfigException(String.format("$r$lO jogador $e%s $r$lnão está na lista de administradores do $eSmart Hopper$r$l!!!", name));
        }
        configDao.removeAdm(name);
        return true;
    }

    public boolean containsAdm(@NotNull Player player) {
        return configDao.containsAdm(player.getName());
    }

    public static boolean ContainsAdm(Player player) {
        return new ConfigDao().containsAdm(player.getName());
    }
}
