package pet_finder.enums;

public enum EstadoMascota {
    // Se asocia un valor string necesario para el frontend: en masculino y minuscula
    PERDIDA("perdido"),
    ENCONTRADA("encontrado"),
    REENCONTRADA("reencontrado");

    private final String valorFront;

    EstadoMascota(String valorFront) {
        this.valorFront = valorFront;
    }

    public String getValorFront() {
        return valorFront;
    }
}
