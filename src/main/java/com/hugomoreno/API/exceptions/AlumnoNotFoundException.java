package com.hugomoreno.API.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlumnoNotFoundException extends AlumnoException{

    public AlumnoNotFoundException(String mensaje) {
        super(mensaje);
    }
}

