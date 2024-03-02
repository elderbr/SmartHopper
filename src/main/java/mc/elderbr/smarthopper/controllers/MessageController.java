package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.MessageDao;
import mc.elderbr.smarthopper.enums.MessageType;
import mc.elderbr.smarthopper.exceptions.MessageException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MessageController implements VGlobal {

    private MessageDao dao = new MessageDao();

    public MessageController() {
    }

    public String select(@NotNull MessageType type) {
        String message = dao.findByMessage(type.getCode());
        if (message == null || message.isBlank()) {
            return type.getMsg();
        }
        return message.replaceAll("%s", "").replaceAll("\s\s", " ");
    }

    public String select(@NotNull MessageType type, Object... obj) {
        String message = dao.findByMessage(type.getCode());
        if (message == null || message.isBlank()) {
            message = type.getMsg();
        }
        return convert(message, obj);
    }

    public boolean update(@NotNull Player player, @NotNull Message message) {
        if (!player.isOp() || !AdmController.ContainsAdm(player)) {
            throw new MessageException("Você não tem permissão!");
        }
        if (message.getCode().isBlank()) {
            throw new MessageException("Digite o nome do mensagem!");
        }
        if (message.getMsg().isBlank()) {
            throw new MessageException("Digite a mensagem!");
        }
        if (dao.findByMessage(message.getCode()) == null) {
            throw new MessageException("A mensagem não existe!");
        }
        try {
            dao.update(message);
        } catch (IOException e) {
            throw new MessageException("Erro ao atualizar a mensagem!");
        }
        return true;
    }

    private String convert(String msg, Object... obj) {
        String txt = msg;
        if (obj == null || obj.length < 1) {
            return msg;
        }
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] instanceof Player player) {
                txt = txt.replaceAll("<player>", player.getName());
            }
            if (obj[i] instanceof Grupo grupo) {
                txt = txt.replaceAll("<group>", grupo.getName());
            }
            if (obj[i] instanceof Item item) {
                txt = txt.replaceAll("<item>", item.getName());
            }
            if (obj[i] instanceof Integer code) {
                txt = txt.replaceAll("<code>", String.valueOf(code));
            }
            if (obj[i] instanceof String name) {
                txt = txt.replaceAll("<name>", name);
            }
        }
        return txt;
    }
}
