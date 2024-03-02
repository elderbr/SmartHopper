package mc.elderbr.smarthopper.enums;

import mc.elderbr.smarthopper.utils.Msg;

public enum MessageType {

    NOT_PERMISSION("Você não tem permissão!"),
    ERROR("Ops, algo deu erro!"),
    RESTARTER("Smart Hopper foi reiniciado..."),
    RELOAD("Dados do Smart Hopper carregados..."),

    ADM_NEW("O jogador <player> é o novo administrador do Smart Hopper!"),
    ADM_REMOVE("O jogador <player> não é mais administrador do Smart Hopper!"),
    ADM_NOT("Ops, você não é adm do $2Smart Hopper$r!!!"),
    ADM_NOT_EXIST("O <player> não está na lista de administrador do Smart Hopper!!!"),

    ITEM_NOT_EXIST("O item <item> não existe!"),
    ITEM_INVALID("O item <item> não existe!"),
    ITEM_NAME_INVALID("O nome o item <item> é invalido!"),
    ITEM_NAME_ENTER("Digite o nome do item!"),
    ITEM_CODE_INVALID("O código <code> não é valido!"),
    ITEM_CODE_ENTER("Segure o item na mão ou digite o nome o ID do item!"),
    ITEM_UPDATE_ERROR("Erro ao atualizar o item!"),

    GROUP_NOT_EXIST("O grupo <group> não existe!"),
    GROUP_NAME_ENTER("Digite o nome do grupo!"),
    GROUP_ADD_ITEM("Adicione item no grupo para poder salvar!"),
    GROUP_CODE_INVALID("O código <code> do grupo é invalido!"),
    GROUP_CODE_ENTER("Segure o item na mão ou digite o nome ou o código do grupo!"),
    GROUP_NOT_EXIST_ITEM("Não existe grupo para o item <item>!"),
    GROUP_ADD("O jogador <player> adicionou novo grupo <group>!"),
    GROUP_ADD_SUCCESSFULLY("Grupo <group> adicionado com sucesso!"),
    GROUP_ADD_ERRO("Erro ao adicionar novo grupo!"),
    GROUP_UPDATE("Grupo <group> foi atualizado pelo o jogador <player>!"),
    GROUP_UPDATE_ERROR("Erro ao atualizar o grupo!"),
    GROUP_DELETE("O <group> foi removido pelo o jogador <player>!"),
    GROUP_DELETE_ERROR("Erro ao remover o grupo <group>!"),

    PLAYER_NAME_INVALID("Nome do player invalido!"),
    PLAYER_NAME_ERROR("Digite o nome do jogador!"),
    PLAYER_EXIST("O jogador <player> já está na lista de administradores do Smart Hopper!"),
    PLAYER_NOT_EXIST("O jogador <player> não está na lista de administradores do Smart Hopper!"),

    HOPPER_INVALID("O funil configurado com o nome <name> não é valido!"),
    HOPPER_LOCALIZATION("Hopper localizado no mundo"),

    TEXTURE_PERMISSION("Para entrar no nosso servidor precisa aceitar nossa textura!"),
    TEXTUR_ERROR("Ocorreu um ao baixar a textura, tente novamente!"),

    TRANSLATION_NAME_INVALID("A tradução não pode ser menor que 3 caracteres!"),
    TRANSLATION_NOT_PERMISSION("Você não tem permissão para adicionar a tradução ao item!"),
    TRANSLATION_ITEM_ADD("Tradução em <name> adicionada para o item <item>!"),
    TRANSLATION_GROUP_ADD("Tradução em <name> adicionada para o grupo <group>!"),
    TRANSLATION_ERRO("Erro ao adicionar a tradução!")
    ;

    String msg;

    MessageType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return Msg.Color(msg);
    }

    public String getCode(){
        return this.name().toLowerCase();
    }
}
