package com.nomEmpresa.nomProyecto.servicio;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase encargada de validaciones, abierta a cambios
 */
public abstract class Validador {


    /**
     * Formatos de imagen permitidos
     */
    private static Set<String> formatosPermitidos = Set.of(
            "image/heic",
            "image/heif",
            "image/png",
            "image/jpeg",
            "image/jpg"
    );




    public static Boolean validarFormatoMultimedia(MultipartFile archivo){
        return
                formatosPermitidos.contains(archivo.getContentType()) &&
                validarNombreMultimmedia(archivo);
    }



    public static Boolean validarNombreMultimmedia(MultipartFile archivo){

        //Condiciones para poder continuar
        if(archivo == null) return false;
        if(Objects.requireNonNull(archivo.getOriginalFilename()).isBlank()) return false;
        if(archivo.getOriginalFilename().isEmpty()) return false;
        if(archivo.getOriginalFilename().contains("..")) return false;

        //Evaluo el nombre del archivo
        Pattern patronPermitido = Pattern.compile("^[\\w\\.\\-\\/\\(\\)\\_'\" ]*$");
        Matcher matcher = patronPermitido.matcher(archivo.getOriginalFilename());

        return matcher.matches();
    }




}
