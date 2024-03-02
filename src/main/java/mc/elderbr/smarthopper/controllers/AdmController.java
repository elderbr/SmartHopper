package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.enums.MessageType;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdmController implements VGlobal {

    private ConfigDao configDao = new ConfigDao();
    private MessageController msgController = new MessageController();

    public AdmController() {
        configDao.findByAll();
        if(ADM_LIST.size() < 1){
            configDao.addAdm("ElderBR");
        }
    }

    public boolean addAdm(Player player, String[] args) throws Exception {
        if(!player.isOp()){
            throw new Exception(msgController.select(MessageType.ADM_NOT));
        }
        if(args == null || args.length < 1){
            throw new Exception(msgController.select(MessageType.PLAYER_NAME_INVALID));
        }
        if(args.length > 1){
            throw new Exception(msgController.select(MessageType.PLAYER_NAME_ERROR));
        }
        String name = args[0];
        if(configDao.containsAdm(name)){
            throw new Exception(msgController.select(MessageType.PLAYER_EXIST, name));
        }
        configDao.addAdm(name);
        return configDao.containsAdm(name);
    }

    public boolean removeAdm(Player player, String[] args) throws Exception {
        if(!player.isOp()){
            throw new Exception(msgController.select(MessageType.ADM_NOT));
        }
        if(args == null || args.length < 1){
            throw new Exception(msgController.select(MessageType.PLAYER_NAME_ERROR));
        }
        if(args.length > 1){
            throw new Exception(msgController.select(MessageType.PLAYER_NAME_ERROR));
        }
        String name = args[0];
        if(name.length()<5){
            throw new Exception(msgController.select(MessageType.PLAYER_NAME_INVALID, name));
        }
        if(!configDao.containsAdm(name)){
            throw new Exception(msgController.select(MessageType.ADM_NOT_EXIST, name));
        }
        configDao.removeAdm(name);
        return true;
    }

    public boolean containsAdm(@NotNull Player player){
        return configDao.containsAdm(player.getName());
    }

    public static boolean ContainsAdm(Player player){
        return new ConfigDao().containsAdm(player.getName());
    }

    public static void Reset(){
        new ConfigDao().restart();
    }
}
