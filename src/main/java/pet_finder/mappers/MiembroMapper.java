package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.dtos.MiembroRequestDTO;
import pet_finder.enums.RolUsuario;
import pet_finder.models.Miembro;

import java.util.List;

@Component
public class MiembroMapper implements Mapper<MiembroRequestDTO, MiembroDetailDTO, Miembro> {

    @Override
    public Miembro aEntidad(MiembroRequestDTO request) {

        Miembro miembro = new Miembro();

        miembro.setNombre(request.getNombre());
        miembro.setApellido(request.getApellido());
        miembro.setEmail(request.getEmail());
        miembro.setContrasenia(request.getContrasenia());
        miembro.setRol(RolUsuario.MIEMBRO);
        miembro.setActivo(true);

        return miembro;
    }

    @Override
    public MiembroDetailDTO aDetail(Miembro miembro) {
        return new MiembroDetailDTO(miembro);
    }

    @Override
    public List<MiembroDetailDTO> deEntidadesAdetails(List<Miembro> miembros) {
        return miembros.stream()
                .map(this::aDetail)
                .toList();
    }

}
