package pet_finder.dtos.norma;

import pet_finder.models.NormaComunidad;

public record NormaComunidadDetailDTO (Long id, String texto) {
    public NormaComunidadDetailDTO (NormaComunidad norma){
        this(norma.getId(), norma.getTexto());
    }
}
