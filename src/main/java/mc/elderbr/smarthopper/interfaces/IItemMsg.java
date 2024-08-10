package mc.elderbr.smarthopper.interfaces;

public interface IItemMsg {
    String ITEM_INVALID = "Item invalido ou não existe!!!";
    String ITEM_ID_INVALID = "Código do item invalido ou não existe!!!";
    String ITEM_NAME_REQUIRED = "Digite o nome do item!!!";
    String ITEM_NAME_INVALID = "Nome do item invalido!!!";
    String ITEM_NOT_LIST = "O item %s não está na lista!!!";

    String ITEM_HOLD_HAND = "Segure o item na mão ou digite o nome ou código!!!";

    String ITEM_FILE_ERROR = "Erro ao criar o arquivo item.yml";

    String ITEM_SAVE_ERROR = "Erro ao salvar o item!!!";
    String ITEM_UPDATE_ERROR = "Erro ao atualizar o item!!!";
    String ITEM_DELETE_ERROR = "Erro ao apagar o item!!!";

}
