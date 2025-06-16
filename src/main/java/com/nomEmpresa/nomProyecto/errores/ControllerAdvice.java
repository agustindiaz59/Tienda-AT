package com.nomEmpresa.nomProyecto.errores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> notFound(){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("-- ");
    }


}
