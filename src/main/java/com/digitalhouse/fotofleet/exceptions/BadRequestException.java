package com.digitalhouse.fotofleet.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String mensaje){
        super(mensaje);
    }
}
