package mc.elderbr.smarthopper.interfaces;

public interface Linguagem {

    // LANG
    Object setCdLang(int codigo);
    int getCdLang();
    Object setDsLang(String lang);
    String getDsLang();

    // TRADUCAO
    Object setCdTraducao(int codigo);
    int getCdTraducao();
    Object setDsTraducao(String traducao);
    String getDsTraducao();

}
