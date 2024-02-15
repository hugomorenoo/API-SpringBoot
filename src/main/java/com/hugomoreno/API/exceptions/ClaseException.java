package com.hugomoreno.API.exceptions;

import com.hugomoreno.API.model.Clase;

public class ClaseException extends RuntimeException{
    public ClaseException(String mensaje){
        super(mensaje);
    }
}
