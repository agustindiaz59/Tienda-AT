package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.respuestas.DetallesGaleriaPage;
import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.servicio.GaleriaService;
import com.nomEmpresa.nomProyecto.servicio.MultimediaService;
import com.nomEmpresa.nomProyecto.servicio.Validador;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;


@Tag(
        name = "Controlador de archivos y galerías",
        description = "Acciones públicas relacionadas con el uso del usuario en la plataforma"
)
@RestController
@RequestMapping("/multi")
public class MultimediaController {


    private GaleriaService galeriaService;

    private MultimediaService multimediaService;





    @Autowired
    public MultimediaController(GaleriaService galeriaService, MultimediaService multimediaService) {
        this.galeriaService = galeriaService;
        this.multimediaService = multimediaService;
    }




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
                            description = "Galeria no encontrada en el sistema, O error en la fecha, usar formato ISO-8601 UTC (YYYY-MM-DD'T'HH:mm:ss.SSX'Z')"
                    )
            }
    )
    @GetMapping("/galerias/{idGaleria}")
    @ResponseBody
    public ResponseEntity<DetallesGaleriaPage> listarMulti(
            @PathVariable("idGaleria") String idGaleria,
            @RequestParam(value = "ultimaFecha", required = false, defaultValue = "2000-01-01T00:00:00Z") Instant ultimaFecha,
            @RequestParam(value = "paginaSolicitada", required = false, defaultValue = "0") int paginaSolicitada,
            @RequestParam(value = "elementosPorPagina", required = false, defaultValue = "10") int elementosPorPagina

    ){

        return multimediaService.listarMulti(
                idGaleria,
                ultimaFecha,
                PageRequest.of(paginaSolicitada, elementosPorPagina));
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

        if(Validador.validarFormatoMultimedia(archivo)){
            return multimediaService.agregarMultimedia(idGaleria, archivo, request);
        }else {
            return ResponseEntity
                    .badRequest()
                    .body(
                            """
                            -- Error de solicitud, posibles errores:
                            -- Formato de imagenes incorrecto, utilizar jpg, jpeg, png o heic
                            -- Nombre de archivo invalido, utilizar SOLO letras mayusculas o minusculas, numeros, o estos simbolos .-_/()
                            -- No utilizar expresiones de cambio de directorio ../ ni ..
                            """
                    );
        }
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
            @RequestParam(value = "comprimido", required = false, defaultValue = "false") Boolean comprimido,
            @RequestParam(value = "tazaCompresion", required = false, defaultValue = "3") Integer tazaCompresion

    ){
        if(comprimido){
            return multimediaService.getArchivoComprimido(urlMultimedia, tazaCompresion);
        }else{
            return multimediaService.getArchivoCompleto(urlMultimedia);
        }
    }







    @Operation(
            summary = "Agrega una nota en una galeria",
            description = "Agrega una nota en la galeria y se guarda en la BBDD",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operacion exitosa"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en el ID o nota vacia"
                    )
            }
    )
    @PostMapping("/notas/upload/{idGaleria}")
    @ResponseBody
    public ResponseEntity<GaleriaDTO> subirNota(
            @PathVariable("idGaleria") String idGaleria,
            @RequestParam(value = "nota", required = false) String nota
    ){
        if(nota == null || nota.isEmpty() || nota.equals(" ")){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        return multimediaService.agregarNota(idGaleria, nota);
    }

}

