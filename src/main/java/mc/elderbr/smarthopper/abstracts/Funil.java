package mc.elderbr.smarthopper.abstracts;

import mc.elderbr.smarthopper.model.Traducao;
import org.bukkit.entity.Player;

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

    public List<Traducao> addTraducao(Traducao traducao){
        traducaoList.add(traducao);
        return traducaoList;
    }

    public List<Traducao> removeTraducao(Traducao traducao){
        traducaoList.remove(traducao);
        return traducaoList;
    }

    public abstract String toTraducao(Player player);

    public List<Traducao> getListTraducao(){
        return traducaoList;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }

}
