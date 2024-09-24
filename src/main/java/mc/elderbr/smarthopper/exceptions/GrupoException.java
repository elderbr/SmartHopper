package mc.elderbr.smarthopper.exceptions;

import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.utils.Msg;

public class GrupoException extends RuntimeException{

    private String msg;

    public GrupoException() {
    }

    public GrupoException(String message) {
        super(message);
        msg = message;
    }

    public GrupoException(String message, Grupo grupo) {
        super(message);
        if(message.contains("%grupoName")){
            msg = message.replaceAll("%grupoName", grupo.getName());
            return;
        }
        msg = message;
    }

    @Override
    public String getMessage() {
        return Msg.Color(msg);
    }
}
