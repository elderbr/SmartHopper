package mc.elderbr.smarthopper;

public enum CargoType {
    ADM(1, "Adminsitrador"), OPERADOR(2, "Operador");

    int cd;
    String cargo;

    CargoType(int cd, String cargo) {
        this.cd = cd;
        this.cargo = cargo;
    }

    public int codigo() {
        return cd;
    }

    public String cargo() {
        return cargo;
    }
}
