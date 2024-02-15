package com.hugomoreno.API.service;

import com.hugomoreno.API.exceptions.ClaseNotFoundException;
import com.hugomoreno.API.model.Clase;
import com.hugomoreno.API.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {
    @Autowired
    private ClaseRepository claseRepository;

    public List<Clase> getClases(){
        return claseRepository.findAll();
    }

    public Optional<Clase> getClaseById(Long idClase){
        return Optional.ofNullable(claseRepository.findById(idClase).orElseThrow(
                () -> new ClaseNotFoundException("No se ha encontrado la clase con id: " + idClase)
        ));
    }

    /* public Clase crearClase(Clase clase){
        return claseRepository.save(clase);
    } */
}
