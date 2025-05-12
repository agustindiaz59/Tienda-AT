package com.nomEmpresa.nomProyecto.servicio;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Clase encargada de validaciones, abierta a cambios
 */
@Component
public abstract class Validador {


    /**
     * Formatos de imagen permitidos
     */
    private static Set<String> formatosPermitidos = Set.of(
            "image/heic",
            "image/png",
            "image/jpeg",
            "image/jpg"
    );



    public static Boolean validarFormatoMultimedia(MultipartFile archivo){
        return formatosPermitidos.contains(archivo.getContentType());
    }


}
