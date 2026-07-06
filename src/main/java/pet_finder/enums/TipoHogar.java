package pet_finder.enums;

public enum TipoHogar {
    CASA("casa"),
    DEPARTAMENTO("departamento");

    private final String valorFront;

    TipoHogar(String valorFront){
        this.valorFront = valorFront;
    }

    public String getValorFront(){
        return valorFront;
    }
}
