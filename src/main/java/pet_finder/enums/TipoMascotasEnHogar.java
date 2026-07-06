package pet_finder.enums;

public enum TipoMascotasEnHogar {
    PERRO("perro"),
    GATO("gato"),
    PERRO_Y_GATO("perro_y_gato");

    private final String valorFront;

    TipoMascotasEnHogar(String valorFront){
        this.valorFront = valorFront;
    }

    public String getValorFront(){
        return valorFront;
    }
}
