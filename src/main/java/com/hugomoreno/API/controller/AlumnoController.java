package com.hugomoreno.API.controller;

import com.hugomoreno.API.model.Alumno;
import com.hugomoreno.API.model.Clase;
import com.hugomoreno.API.service.AlumnoService;
import com.hugomoreno.API.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AlumnoController {
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private ClaseService claseService;

    @Operation(summary = "Obtiene todos los alumnos", tags = {"alumnos"})
    @ApiResponse(responseCode = "200", description = "Listado de alumnos")
    @ApiResponse(responseCode = "404", description = "No hay alumnos")
    @GetMapping("/alumno")
    public List<Alumno> getAllAlumnos(){
        return alumnoService.getAllAlumnos();
    }

    @Operation(summary = "Obtiene un alumno", description = "Obtiene un alumno por su id", tags = {"alumnos"})
    @Parameter(name = "id", required = true, description = "ID del alumno", example = "1")
    @ApiResponse(responseCode = "200", description = "Alumno encontrado")
    @ApiResponse(responseCode = "404", description = "Alumno no encontrado")
    @GetMapping("/alumno/{id}")
    public ResponseEntity<Alumno>  getAlumnoById(@PathVariable Long id) {
        Optional<Alumno> optionalAlumno = alumnoService.getAlumnoById(id);
        return optionalAlumno.map(alumno -> new ResponseEntity<>(alumno, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @Operation(summary = "Obtiene un alumno", description = "Obtiene un alumno por su nombre", tags = {"alumnos"})
    @Parameter(name = "nombre", required = true, description = "Nombre del alumno", example = "Hugo Moreno")
    @ApiResponse(responseCode = "200", description = "Alumno encontrado")
    @ApiResponse(responseCode = "404", description = "Alumno no encontrado")
    @GetMapping("/alumno/nom")
    public ResponseEntity<List<Alumno>> getAlumnoPorNombre(@RequestParam String nombre){
        List<Alumno> alumnos = alumnoService.getAlumnosByNombre(nombre);
        if(!alumnos.isEmpty()){
            return new ResponseEntity<>(alumnos, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Muestra foto", description = "Obtiene foto de alumno dado el id", tags = {"alumnos"})
    @Parameter(name = "id", description = "ID del alumno", required = true, example = "8")
    @ApiResponse(responseCode = "200", description = "Foto del alumno")
    @ApiResponse(responseCode = "404", description = "Alumno no encontrado")
    @GetMapping(value = "/{id}/foto", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> descargarFoto(@PathVariable Long id) {
        byte[] foto = alumnoService.descargarFoto(id);
        if ( foto != null ) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(foto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Obtiene todos los alumnos", description = "Obtiene todos los alumnos de una clase", tags = {"alumnos"})
    @Parameter(name = "idClase", required = true, description = "Id de una clase", example = "1")
    @ApiResponse(responseCode = "200", description = "Alumnos encontrados")
    @ApiResponse(responseCode = "404", description = "No hay alumnos")
    @GetMapping(value = "/alumno/{idClase}")
    public ResponseEntity<List<Alumno>> getAlumnoByClase(@RequestParam Long idClase) {
        List<Alumno> alumnos = alumnoService.getAlumnosByClase(idClase);
        if(!alumnos.isEmpty()){
            return new ResponseEntity<>(alumnos, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtiene todos los alumnos ordenados por nota", tags = {"alumnos"})
    @ApiResponse(responseCode = "200", description = "Alumnos encontrados")
    @ApiResponse(responseCode = "404", description = "No hay alumnos")
    @GetMapping(value = "/alumnos/ordenados")
    public List<Alumno> getAlumnosOrderByNota() {
        return alumnoService.getAlumnosOrderByNota();
    }
    @Operation(summary = "Crea un alumno", tags = {"alumnos"})
    @ApiResponse(responseCode = "201", description = "Alumno creado")
    @ApiResponse(responseCode = "404", description = "Alumno no creado")
    @PostMapping(value = "/alumno", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Alumno> createAlumno(@RequestParam String nombre,
                                               @RequestParam Integer edad,
                                               @RequestParam Integer nota_final,
                                               @RequestParam Long idClase,
                                               @RequestPart (name="imagen", required=false)MultipartFile imagen) throws IOException {
        Optional<Clase> optionalClase = claseService.getClaseById(idClase);
        if (((Optional<?>) optionalClase).isPresent()){
            Clase clase = optionalClase.get();
            Alumno alumno = new Alumno(nombre, edad, nota_final, clase);
            Alumno nuevo_alumno =  alumnoService.createAlumno(alumno, imagen);
            return new ResponseEntity<>(nuevo_alumno, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Edita un alumno", tags = {"alumnos"})
    @ApiResponse(responseCode = "200", description = "Alumno editado")
    @ApiResponse(responseCode = "404", description = "Alumno no editado")
    @PutMapping(value = "/alumno/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Alumno> updateAlumno(@PathVariable Long id,
                                               @RequestParam String nombre,
                                               @RequestParam Integer edad,
                                               @RequestParam Integer nota_final,
                                               @RequestParam Long idClase,
                                               @RequestPart (name="imagen", required=false)MultipartFile imagen) throws IOException {
        Optional<Alumno> optionalAlumno = alumnoService.getAlumnoById(id);
        if (((Optional<?>) optionalAlumno).isPresent()) {
            Optional<Clase> optionalClase = claseService.getClaseById(idClase);
            if (((Optional<?>) optionalClase).isPresent()){
                Clase clase = optionalClase.get();
                Alumno existingAlumno = optionalAlumno.get();
                existingAlumno.setNombre(nombre);
                existingAlumno.setEdad(edad);
                existingAlumno.setNotaFinal(nota_final);
                existingAlumno.setClase(clase);
                Alumno updatedAlumno;
                if(imagen != null){
                    updatedAlumno = alumnoService.updateAlumnoFoto(existingAlumno, imagen);
                }else{
                    updatedAlumno = alumnoService.updateAlumno(existingAlumno);
                }
                return new ResponseEntity<>(updatedAlumno, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Elimina un alumno", tags = {"alumnos"})
    @Parameter(name = "id", required = true, description = "ID del alumno", example = "1")
    @ApiResponse(responseCode = "200", description = "Alumno eliminado")
    @ApiResponse(responseCode = "404", description = "Alumno no eliminado")
    @DeleteMapping("/alumno/{id}")
    public ResponseEntity<Void> deleteAlumno(@PathVariable Long id) {
        Optional<Alumno> optionalAlumno = alumnoService.getAlumnoById(id);

        if(optionalAlumno.isPresent()){
            alumnoService.deleteAlumnoById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtiene todas las clases", tags = {"clases"})
    @ApiResponse(responseCode = "200", description = "Listado de clases")
    @ApiResponse(responseCode = "404", description = "No hay clases")
    @GetMapping("/clase")
    public List<Clase> getAllClases(){
        return claseService.getClases();
    }

    @GetMapping("/alumnosView")
    public ModelAndView listado(){
        List<Clase> clases = getAllClases();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listarClases", clases);
        modelAndView.setViewName("listado.html");
        return modelAndView;
    }

}
