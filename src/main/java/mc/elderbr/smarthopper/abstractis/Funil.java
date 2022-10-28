package mc.elderbr.smarthopper.abstractis;

import java.util.HashMap;
import java.util.Map;

public class Funil {
    private int codigo;
    private String name;

    private boolean bloquear;
    private Map<String, String> traducao = new HashMap<>();

    public Funil() {
    }

    public int getCodigo() {
        return codigo;
    }

    public Funil setCodigo(int codigo) {
        this.codigo = codigo;
        return this;
    }

    public String getName() {
        return name;
    }

    public Funil setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isBloquear() {
        return bloquear;
    }

    public void setBloquear(boolean bloquear) {
        this.bloquear = bloquear;
    }

    public Map<String, String> getTraducao() {
        return traducao;
    }

    public Funil addTraducao(String name, String traducao) {
        this.traducao.put(name, traducao);
        return this;
    }

    public Funil removeTraducao(String name){
        traducao.remove(name);
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
