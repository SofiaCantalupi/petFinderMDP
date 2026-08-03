package pet_finder.enums;

public enum MotivoRechazo {
    MANUAL("manual"),
    AUTO_POR_OTRA_APROBADA("auto_otra_aprobada"),
    AUTO_POR_PUBLICACION_ELIMINADA("auto_publicacion_eliminada"),
    AUTO_CAMBIO_ESTADO_MASCOTA("auto_cambio_estado_mascota"),
    AUTO_POR_BAJA_CUENTA("auto_baja_cuenta");

    private final String valorFront;

    MotivoRechazo(String valorFront){
        this.valorFront = valorFront;
    }

    public String getValorFront(){
        return valorFront;
    }
}
