package mc.elderbr.smarthopper.enums;

import mc.elderbr.smarthopper.interfaces.VGlobal;

public enum EConfig {

    AUTHOR("author", "ElderBR", "Configuração Separador inteligente SmartHopper"),
    DISCORD("discord", "ElderBR#5398", "Tem duvidas entre em contato pelo o Discord"),
    VERSION("version", VGlobal.VERSION, "Versão atual do plugin"),
    TUTORIAL("tutorial", "https://youtube/fBIeZ57ka1M?si=ZqE5TQRG2KjlaHdD", "Veja o tutorial de como usar o Smart Hopper"),
    TEXTURE("texture", "https://mediafilez.forgecdn.net/files/5616/128/SmartHopper.zip", "Para ter uma visualização melhor use a textura personalizada do Smart Hopper a textura traz imagens dos botões de navegação entre as páginas do grupo"),
    USE_TEXTURE("useTexture", "true", "Se for escolhido como verdadeiro o uso da textura se torna obrigatório"),
    ADM("adm", "ElderBR", "Administradores do Smart Hopper"),
    LAND("lang", "[]", "Linguagens disponível");

    String key, value, comment;

    EConfig(String key, String value, String comment) {
        this.key = key;
        this.value = value;
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }
}
