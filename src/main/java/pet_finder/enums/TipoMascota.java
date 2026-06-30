package pet_finder.enums;

public enum TipoMascota {
    // Se asocia un valor string necesario para el frontend: en miniscula
    PERRO("perro"),
    GATO("gato");

    private final String valorFront;

    TipoMascota(String valorFront){
        this.valorFront = valorFront;
    }

    public String getValorFront(){
        return valorFront;
    }
}
