package mc.elderbr.smarthopper.enums;

public enum AdmType {

    ADMINISTRADOR(1, "Adm"), OPERADOR(2, "Operador");

    int code;
    String name;

    AdmType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public AdmType setCode(int code){
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
