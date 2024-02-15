package com.hugomoreno.API.service;

import com.hugomoreno.API.exceptions.AlumnoBadRequestException;
import com.hugomoreno.API.exceptions.AlumnoNotFoundException;
import com.hugomoreno.API.repository.AlumnoRepository;
import com.hugomoreno.API.model.Alumno;
import com.hugomoreno.API.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/*import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;*/
import java.util.List;
import java.io.IOException;
import java.util.Optional;



@Service
public class AlumnoService {
    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Alumno> getAllAlumnos(){
        return alumnoRepository.findAll();
    }

    public Alumno createAlumno(Alumno alumno, MultipartFile file) throws IOException {
        comprobarAlumnoIntroducido(alumno);
        guardarImagen(alumno, file);
        return alumnoRepository.save(alumno);
    }

    public Optional<Alumno> getAlumnoById(Long id){
        return Optional.ofNullable(alumnoRepository.findById(id).orElseThrow(
                () -> new AlumnoNotFoundException("No se ha encontrado el alumno con id: " + id)
        ));
    }

    public Alumno updateAlumnoFoto(Alumno alumno, MultipartFile file) throws IOException {
        comprobarAlumnoIntroducido(alumno);
        guardarImagen(alumno, file);
        return alumnoRepository.save(alumno);
    }

    public Alumno updateAlumno(Alumno alumno) {
        comprobarAlumnoIntroducido(alumno);
        return alumnoRepository.save(alumno);
    }

    public void deleteAlumnoById(Long id){
        alumnoRepository.deleteById(id);
    }

    private void comprobarAlumnoIntroducido(Alumno alumno){
        if(alumno.getNombre() == null || alumno.getNombre().isEmpty()) {
            throw new AlumnoBadRequestException("Debe introducirse el nombre");
        }
        if(alumno.getNotaFinal() == null) {
            throw new AlumnoBadRequestException("Debe introducirse la nota final");
        }
        if(alumno.getEdad() == null || alumno.getEdad() <= 0)
            throw new AlumnoBadRequestException("Debe introducirse la edad y no puede ser menor o igual a 0");
    }

    private void guardarImagen(Alumno alumno, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            alumno.setFoto(ImageUtils.compressImage(file.getBytes()));
            alumno.setImagen(file.getOriginalFilename());
            /*Path dirImg = Paths.get("C:\Users\atrib\Downloads\API\API\src\main\resources\static\img");
            if (!Files.exists(dirImg)) {
                Files.createDirectories(dirImg);
            }
            try {
                byte[] bytesImg = file.getBytes();
                Path rutaCompleta = dirImg.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                Files.write(rutaCompleta, bytesImg);
            } catch (IOException e) {
                throw new AlumnoException("Error de escritura" + "e: " + e);
            }*/
        }
        else
            throw new AlumnoBadRequestException("Debe introducirse el fichero imagen");
    }

    public byte[] descargarFoto(Long id){
        Alumno alumnoEncontrado = alumnoRepository.findById(id).orElse(null);
        if (alumnoEncontrado != null){
            return ImageUtils.decompressImage(alumnoEncontrado.getFoto());
        }else{
            return null;
        }
    }

    public List<Alumno> getAlumnosByNombre(String nombre){
        return alumnoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Alumno> getAlumnosByClase(Long idClase){
        return alumnoRepository.findByClaseId(idClase);
    }

    public List<Alumno> getAlumnosOrderByNota(){
        return alumnoRepository.findAllByOrderByNotaFinalDesc();
    }
}
