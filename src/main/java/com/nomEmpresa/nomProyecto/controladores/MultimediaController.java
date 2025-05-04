package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.servicio.GaleriaService;
import com.nomEmpresa.nomProyecto.servicio.MultimediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(
        name = "Controlador de archivos y galerías",
        description = "Acciones públicas relacionadas con el uso del usuario en la plataforma"
)
@RestController
@RequestMapping("/multi")
public class MultimediaController {

    @Autowired
    private GaleriaService galeriaService;


    @Autowired
    private MultimediaService multimediaService;




    /**
     * Obtener un listado de todos los archivos multimedia guardados en una galeria especifica
     * a esta URL debe apuntar el codigo QR
     *
     * @param idGaleria Id de la galeria en cuestion
     * @return listado de archivos multimedia
     */
    @Operation(
            summary = "Obtener detalles de una galeria en particular",
            description = "Obtiene los detalles de una galeria a traves de su ID, el QR debe apuntar aqui",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operacion exitosa"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Galeria no encontrada en el sistema"
                    )
            }
    )
    @GetMapping("/galerias/{idGaleria}")
    @ResponseBody
    public ResponseEntity<GaleriaDTO> listarMulti(@PathVariable("idGaleria") String idGaleria){
        return multimediaService.listarMulti(idGaleria);
    }






    /**
     * Agregar una imagen o video en la galeria
     *
     * @param idGaleria galeria donde debe guardarse
     * @param archivo archivo en cuestion
     * @param request solicitud HTTP para generar respuesta
     * @return confirmacion de creacion y header Location para encontrarlo
     */
    @Operation(
            summary = "Subir una foto dentro de una galería",
            description = "Agrega una foto a la galería especificada",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Foto agregada correctamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Galeria no encontrada"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de parametros en la solicitud"
                    )
            }
    )
    @PostMapping("/archivos/upload/{idGaleria}")
    public ResponseEntity<String> uploadMulti(
            @PathVariable("idGaleria") String idGaleria,
            @RequestParam("imagen") MultipartFile archivo,
            HttpServletRequest request
    ){

        return multimediaService.agregarMultimedia(idGaleria, archivo, request);
    }








    /**
     * Obtiene un archivo multimedia en especifico
     *
     * @param urlMultimedia Url relativa al Bucket del recurso
     * @return Archivo solicitado
     */
    @Operation(
            summary = "Obtener un archivo desde wasabi",
            description = "Obtiene un archivo desde wasabi a través de su URL (ver header Location o Galeria.archivos[].src)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operacion exitosa"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Archivo inexistente"
                    )
            }
    )
    @GetMapping("/archivo")
    @ResponseBody
    public ResponseEntity<byte[]> traerMultimedia(
            @RequestParam("urlMultimedia") String urlMultimedia,
            @RequestParam(value = "comprimido", required = false, defaultValue = "false") Boolean comprimido
    ){
        if(comprimido){
            return multimediaService.getArchivoComprimido(urlMultimedia);
        }else{
            return multimediaService.getArchivoCompleto(urlMultimedia);
        }
    }

}

