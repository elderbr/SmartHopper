package mc.elderbr.smarthopper.model;

public class Velocity {

    private int id;
    private String name;
    private int type;
    public final static int VELOCITY_IRON = 8;
    public final static int VELOCITY_GOLD = 16;
    public final static int VELOCITY_EMERALD = 32;
    public final static int VELOCITY_DIAMOND = 64;

    public Velocity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
