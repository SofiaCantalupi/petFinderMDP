package pet_finder.enums;

public enum EstadoSolicitud {
    PENDIENTE("pendiente"),
    RECHAZADA("rechazada"),
    APROBADA("aprobada"),
    CANCELADA("cancelada");

    private final String valorFront;

    EstadoSolicitud(String valorFront) {
        this.valorFront = valorFront;
    }

    public String getValorFront(){
        return valorFront;
    }
}
