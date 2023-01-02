package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.abstracts.Funil;

import java.util.ArrayList;
import java.util.List;

public class Item extends Funil {

    private int id = 0;
    private String name;
    private boolean blocked = false;

    private List<Grupo> listGrupo = new ArrayList<>();

    @Override
    public Funil setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Funil setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Funil setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    public Item addListGrupo(Grupo grupo) {
        listGrupo.add(grupo);
        return this;
    }

    public Item removeListGrupo(Grupo grupo) {
        listGrupo.remove(grupo);
        return this;
    }

    public List<Grupo> getListGrupo() {
        return listGrupo;
    }
}