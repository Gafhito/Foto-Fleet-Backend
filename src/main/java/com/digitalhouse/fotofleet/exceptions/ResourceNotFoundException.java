package com.digitalhouse.fotofleet.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String mensaje){
        super(mensaje);
    }
}
