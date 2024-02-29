package mc.elderbr.smarthopper.model;

import org.bukkit.ChatColor;

public class Message {
    private String code;
    private String msg;
    private String comments;

    public Message() {
    }

    public Message(String code, String msg, String comments) {
        this.code = code;
        this.msg = msg;
        this.comments = comments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return ChatColor.translateAlternateColorCodes('$', msg);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
