package mc.elderbr.smarthopper.abstracts;

import mc.elderbr.smarthopper.model.Traducao;

import java.util.ArrayList;
import java.util.List;

public abstract class Funil {

    private List<Traducao> traducaoList = new ArrayList<>();

    public abstract Funil setId(int id);
    public abstract int getId();

    public abstract Funil setName(String name);
    public abstract String getName();

    public abstract Funil setBlocked(boolean blocked);
    public abstract boolean isBlocked();

    public List<Traducao> addLinguagem(Traducao traducao){
        traducaoList.add(traducao);
        return traducaoList;
    }

    public List<Traducao> removeLinguagem(Traducao traducao){
        traducaoList.remove(traducao);
        return traducaoList;
    }

    public List<Traducao> getLangList(){
        return traducaoList;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}
