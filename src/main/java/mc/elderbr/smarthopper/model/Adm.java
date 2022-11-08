package mc.elderbr.smarthopper.model;

import mc.elderbr.smarthopper.enums.AdmType;
import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.entity.Player;

public class Adm implements Jogador {

    private int codigo;
    private String nome;
    private String uuid;
    private AdmType type = AdmType.ADMINISTRADOR;

    public Adm() {
    }

    public Adm(Player player) {
        nome = player.getName();
        uuid = player.getUniqueId().toString();
    }

    @Override
    public Jogador setCdJogador(int codigo) {
        this.codigo = codigo;
        return this;
    }

    @Override
    public int getCdJogador() {
        return codigo;
    }

    @Override
    public Jogador setDsJogador(Player player) {
        nome = player.getName();
        return this;
    }

    @Override
    public Jogador setDsJogador(String player) {
        nome = player;
        return this;
    }

    @Override
    public String getDsJogador() {
        return nome;
    }

    @Override
    public Jogador setUUID(Player player) {
        uuid = player.getUniqueId().toString();
        return this;
    }

    @Override
    public Jogador setUUID(String player) {
        uuid = player;
        return this;
    }

    @Override
    public String getUUID() {
        return uuid.toString();
    }

    @Override
    public Jogador setLang(Player player) {
        return this;
    }

    @Override
    public AdmType getType() {
        return type;
    }

    @Override
    public String toType(){
        return type.toString();
    }

}