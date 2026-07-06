package pet_finder.enums;

public enum EstadoMascota {
    // Se asocia un valor string necesario para el frontend: en masculino y minuscula
    PERDIDA("perdido"),
    ENCONTRADA("encontrado"),
    REENCONTRADA("reencontrado"),
    EN_ADOPCION("en_adopcion"),
    ADOPTADA("adoptado")
    ;

    private final String valorFront;

    EstadoMascota(String valorFront) {
        this.valorFront = valorFront;
    }

    public String getValorFront() {
        return valorFront;
    }
}
