package pet_finder.mappers;

import java.util.List;

// R -> request D -> detail E -> entidad
public interface Mapper<R,D,E> {
        /*
       CREAR de Request -> Mascota -> Detail

       ACTUALIZAR de Request -> Mascota -> Detail

       LISTAR de List<Entidad> -> List<Detail>

       OBTENER de Mascota -> Detail
     */

    E aEntidad(R request);
    D aDetail(E entidad);

    List<D> deEntidadesAdetails(List<E> entidades);

    E modificar(E entidad, R request); // modificar entidades que ya existen con los datos nuevos del request

}
