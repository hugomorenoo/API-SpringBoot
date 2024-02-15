package com.hugomoreno.API.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alumno")
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    @Column(name="nombre", nullable = false, length = 50)
    private String nombre;
    @Setter
    @Getter
    @Column(name="edad", nullable = false)
    private Integer edad;
    @Setter
    @Getter
    @Column(name="nota_final", nullable = false)
    private Integer notaFinal;
    @Setter
    @Getter
    @Column(name="imagen", nullable = false, length = 100)
    private String imagen;
    @Setter
    @Getter
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="foto", columnDefinition="longblob", nullable=true)
    private byte[] foto;
    @Column(name="created_at")
    private LocalDateTime created_at = LocalDateTime.now();
    @Column(name="updated_at")
    private LocalDateTime updated_at = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idClase")
    @JsonBackReference
    private Clase clase;

    public Alumno() {
    }

    public Alumno(String nombre, Integer edad, Integer nota_final, Clase clase) {
        this.nombre = nombre;
        this.edad = edad;
        this.notaFinal = nota_final;
        this.clase = clase;
    }


    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad + '\'' +
                ", nota final=" + notaFinal + '\''+
                ", clase=" + clase.getClase() +
                '}';
    }

}

