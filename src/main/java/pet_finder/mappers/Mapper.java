package pet_finder.mappers;

import java.util.List;

/* Los metodos de la interfaz pueden retornar o recibir por parametro 3 tipos de genericos:
 R -> representa una clase RequestDTO
 E -> representa una clase que hace referencia a una Entidad
 D -> representa una clase DetailDTO
 Proporciona los metodos necesarios para el mapeo de entidad a dto, y viceversa.
 Es utilizado en controllers.
 */
public interface Mapper<R,D,E> {
    // De RequestDTO a Entidad
    E aEntidad(R request);
    D aDetail(E entidad);

    List<D> deEntidadesAdetails(List<E> entidades);

    E modificar(E entidad, R request); // modificar entidades que ya existen con los datos nuevos del request

}
