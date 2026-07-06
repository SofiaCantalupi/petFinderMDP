package pet_finder.enums;

public enum MotivoRechazo {
    MANUAL("manual"),
    AUTO_POR_OTRA_APROBADA("auto");

    private final String valorFront;

    MotivoRechazo(String valorFront){
        this.valorFront = valorFront;
    }

    public String getValorFront(){
        return valorFront;
    }
}
