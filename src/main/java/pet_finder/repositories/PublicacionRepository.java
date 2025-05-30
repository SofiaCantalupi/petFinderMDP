package com.UTN.pet_finder_mdp.repositories;

import com.UTN.pet_finder_mdp.models.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

}
