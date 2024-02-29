package mc.elderbr.smarthopper.controllers;

import mc.elderbr.smarthopper.dao.ConfigDao;
import mc.elderbr.smarthopper.dao.MessageDao;
import mc.elderbr.smarthopper.exceptions.MessageException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Message;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MessageController implements VGlobal {

    private Message message;

    private MessageDao dao = new MessageDao();

    public MessageController() {
    }

    public String select(@NotNull String code) {
        if (code.isBlank()) {
            throw new MessageException("Digite o nome do mensagem!");
        }
        String message = dao.findByMessage(code);
        if (message == null || message.isBlank()) {
            throw new MessageException("A mensagem não existe!");
        }
        return message;
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
}
