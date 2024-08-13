package mc.elderbr.smarthopper.exceptions;

import mc.elderbr.smarthopper.utils.Msg;

public class GrupoException extends RuntimeException{

    private String msg;

    public GrupoException() {
    }

    public GrupoException(String message) {
        super(message);
        msg = message;
    }

    @Override
    public String getMessage() {
        return Msg.Color(msg);
    }
}
