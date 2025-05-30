package com.UTN.pet_finder_mdp.controllers;

import com.UTN.pet_finder_mdp.services.PublicacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    private PublicacionService publicacionService;

    public PublicacionController (PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

}
