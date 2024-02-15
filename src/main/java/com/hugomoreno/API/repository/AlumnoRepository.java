package com.hugomoreno.API.repository;

import com.hugomoreno.API.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long>{

    List<Alumno> findByNombreContainingIgnoreCase(String nombre);

    List<Alumno> findByClaseId(Long claseId);

    List<Alumno> findAllByOrderByNotaFinalDesc();
}
