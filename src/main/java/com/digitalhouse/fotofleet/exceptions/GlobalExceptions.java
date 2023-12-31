package com.digitalhouse.fotofleet.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptions {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptions.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseException> badRequestException(BadRequestException ex){
        logger.error(ex.getMessage());
        ResponseException responseException = new ResponseException(400, "Bad Request", ex.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseException> resourceNotFoundException(ResourceNotFoundException ex){
        logger.error(ex.getMessage());
        ResponseException responseException = new ResponseException(404, "Not Found", ex.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.NOT_FOUND);
    }
}
