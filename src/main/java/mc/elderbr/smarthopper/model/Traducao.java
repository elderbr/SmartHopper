package mc.elderbr.smarthopper.model;

public class Traducao {

    private String lang;
    private String traducao;
    private Item item;
    private Grupo grupo;

    public Traducao() {
    }

    public Traducao(String lang, String traducao, Item item, Grupo grupo) {
        this.lang = lang;
        this.traducao = traducao;
        this.item = item;
        this.grupo = grupo;
    }

    public String getLang() {
        return lang;
    }

    public Traducao setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getTraducao() {
        return traducao;
    }

    public Traducao setTraducao(String traducao) {
        this.traducao = traducao;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public Traducao setItem(Item item) {
        this.item = item;
        return this;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public Traducao setGrupo(Grupo grupo) {
        this.grupo = grupo;
        return this;
    }
}
