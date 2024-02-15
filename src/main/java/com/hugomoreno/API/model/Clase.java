package com.hugomoreno.API.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "clases")
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    @Column(name = "clase", nullable = false)
    private String clase;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Alumno> alumnos;

    public Clase() {
    }

    public Clase(String clase){
        this.clase = clase;
    }



    @Override
    public String toString() {
        return "Clase{" +
                "id: " + this.id + '\'' +
                ",clase: " + this.clase;
    }

}

