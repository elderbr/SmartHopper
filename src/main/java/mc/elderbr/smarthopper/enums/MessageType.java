package mc.elderbr.smarthopper.enums;

public enum MessageType {

    Not_Permission("Você não tem permissão!"),
    RESTARTER("Smart Hopper foi reiniciado..."),
    RELOAD("Dados do Smart Hopper carregados..."),
    ADM_NEW("O jogador %s é o novo administrador do Smart Hopper!"),
    ADM_REMOVE("O jogador %s não é mais administrador do Smart Hopper!"),
    ITEM_NOT_EXIST("O item %s não existe!"),
    ITEM_INVALID("O item %s não existe!"),
    ITEM_NAME_INVALID("O nome o item %s é invalido!"),
    ITEM_NAME_ENTER("Digite o nome do item!"),
    ITEM_CODE_INVALID("O código %s não é valido!"),
    ITEM_CODE_ENTER("Segure o item na mão ou digite o nome o ID do item!"),
    ITEM_UPDATE_ERROR("Erro ao atualizar o item!"),

    GROUP_NOT_EXIST("O grupo %s não existe!"),
    GROUP_NAME_ENTER("Digite o nome do grupo!"),
    GROUP_ADD_ITEM("Adicione item no grupo para poder salvar!"),
    GROUP_CODE_INVALID("O código %s do grupo é invalido!"),
    GROUP_CODE_ENTER("Segure o item na mão ou digite o nome ou o código do grupo!"),
    GROUP_NOT_EXIST_ITEM("Não existe grupo para o item %s!"),
    GROUP_ADD_SUCCESSFULLY("Grupo %s adicionado com sucesso!"),
    GROUP_ADD_ERRO("Erro ao adicionar novo grupo!"),
    GROUP_UPDATE_ERROR("Erro ao atualizar o grupo!"),
    GROUP_DELETE_ERROR("Erro ao remover o grupo %s!"),

    PLAYER_NAME_INVALID("Nome do player invalido!"),
    PLAYER_NAME_ERROR("Digite o nome do player!"),
    PLAYER_EXIST("O jogador %s já está na lista de administradores do Smart Hopper!"),
    PLAYER_NOT_EXIST("O jogador %s não está na lista de administradores do Smart Hopper!"),

    HOPPER_INVALID("O funil configurado com o nome %s não é valido!"),

    TEXTURE_PERMISSION("Para entrar no nosso servidor precisa aceitar nossa textura!"),
    TEXTUR_ERROR("Ocorreu um ao baixar a textura, tente novamente!"),

    TRANSLATION_NAME_INVALID("A tradução não pode ser menor que 3 caracteres!"),
    TRANSLATION_NOT_PERMISSION("Você não tem permissão para adicionar a tradução ao item!")
    ;

    String msg;

    MessageType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode(){
        return this.name().toLowerCase();
    }
}
