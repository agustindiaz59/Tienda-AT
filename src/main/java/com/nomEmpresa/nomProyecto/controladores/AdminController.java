package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaPage;
import com.nomEmpresa.nomProyecto.servicio.AdministradorService;
import com.nomEmpresa.nomProyecto.servicio.GaleriaService;
import com.nomEmpresa.nomProyecto.servicio.MultimediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Tag(
        name = "Controlador de dministrador",
        description = "Acciones restringidas solo para el administrador logeado con usuario y contraseña"
)
@RestController
@RequestMapping("/admin")
public class AdminController {

    private GaleriaService galeriaService;

    private MultimediaService multimediaService;

    private AdministradorService administradorService;




    @Autowired
    public AdminController(GaleriaService galeriaService, MultimediaService multimediaService, AdministradorService administradorService) {
        this.galeriaService = galeriaService;
        this.multimediaService = multimediaService;
        this.administradorService = administradorService;
    }







    /**
     * Obtener un listado de todas las galerias registradas
     *
     * @return listado de galerias
     */
    @Operation(
            summary = "Lista las galerias disponibles en el sistema",
            description = "Lista todas las galerias en la Base de datos, las cuales corresponden a carpetas en Wasabi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operacion exitosa"
                    ),
                    @ApiResponse(
                            responseCode = "501",
                            description = "Error trayendo las galerias de la base de datos"
                    )
            }
    )
    @GetMapping("/galerias/listar")
    public ResponseEntity<GaleriaPage> listarGalerias(
            @RequestParam(value = "archivos",required = false, defaultValue = "false") Boolean archivos,
            @RequestParam(value = "notas",required = false, defaultValue = "false") Boolean notas,
            //@RequestParam(required = false) Pageable pagina,
            @RequestParam(value = "ultimaFecha", required = false, defaultValue = "2000-01-01T00:00:00.000Z") Instant ultimaFecha,
            @RequestParam(value = "paginaSolicitada", required = false, defaultValue = "0") int paginaSolicitada,
            @RequestParam(value = "elementosPorPagina", required = false, defaultValue = "10") int elementosPorPagina
            ){

        if(elementosPorPagina < 1){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(400))
                    .build();
        }

        if(paginaSolicitada < 0){
            return ResponseEntity
                    .badRequest()
                    .build();
        }


        return galeriaService.listarGalerias(
                archivos,
                notas,
                ultimaFecha,
                PageRequest.of(paginaSolicitada, elementosPorPagina)
        );
    }







    /**
     * Crear una nueva galeria
     *
     * @param nombre nombre de la galeria
     * @return confirmacion de creacion
     */
    @Operation(
            summary = "Crear una nueva galería",
            description = "Crea una nueva galería en base a un nombre y descripción en la base de datos, se refleja cómo una carpeta en Wasabi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Galería creada con éxito, Carpeta en Wasabi creada con éxito"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error en la creacion de la galería y/o en la carpeta en Wasabi"
                    ),
            }
    )
    @PostMapping("/galeria/nuevo")
    public ResponseEntity<GaleriaDTO> crearGaleria(
            @RequestParam(value = "nombre", required = true) String nombre,
            @RequestParam(value = "imagen-perfil", required = false) MultipartFile imagenPerfil,
            @RequestParam(value = "imagen-banner", required = false) MultipartFile imagenBanner,
            HttpServletRequest request
    ){

        return galeriaService.crearGaleria(nombre, imagenPerfil, imagenBanner, request);

    }

















    /**
     * Elimina un archivo de Wasabi y de la Base de Datos
     *
     * @param rutaArchivo Ruta relativa al bucket del archivo en cuestión
     * @return Confirmacion de eliminacion
     */
    @Operation(
            summary = "Eliminar un archivo",
            description = "Elimina un archivo tanto de la BBDD como de Wasabi",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Archivo eliminado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Error eliminando el archivo de la BBDD y/o de Wasabi"
                    )
            }
    )
    @DeleteMapping("/archivo/delete")
    public ResponseEntity<String> deleteMulti(@RequestParam("srcArchivo") String rutaArchivo){
        return multimediaService.deleteArchivo(rutaArchivo);
    }







    @Operation(
            summary = "Elimina una nota de una galeria",
            description = "Elimina la nota de la galeria mediante su contenido y el id de la galeria que lo contiene",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Galeria no encontrada en el sistema"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Nota eliminada exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nota no existente en el sistema"
                    )
            }

    )
    @DeleteMapping("/nota/delete")
    public ResponseEntity<String> deleteNota(
            @RequestParam("idGaleria") String idGaleria,
            @RequestParam("contenidoNota") String contenidoNota
    ){
        return multimediaService.deleteNota(idGaleria, contenidoNota);
    }







    /**
     * Elimina un archivo de Wasabi y de la Base de Datos
     *
     * @param idGaleria Ruta relativa al bucket del archivo en cuestión
     * @return Confirmacion de eliminacion
     */
    @Operation(
            summary = "Eliminar una galeria",
            description = "Elimina una galeria tanto de la BBDD como de Wasabi",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Galeria eliminada exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Error eliminando el archivo de la BBDD y/o de Wasabi"
                    )
            }
    )
    @DeleteMapping("/galeria/delete")
    public ResponseEntity<String> deleteGaleria(@RequestParam("idGaleria") String idGaleria){
        return galeriaService.deleteGaleria(idGaleria);
    }







    /**
     * Actualiza los datos de una galería existente
     *
     * @param idGaleria Id de la galería en cuestion
     * @param nombre Nuevo nombre para la galería
     * @return Confirmación de modificación
     */
    @Operation(
            summary = "Actualizar una galeria existente",
            description = "Actualiza una galería existente identificandola por su id",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Id de galeria no encontrado en la base de datos"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parametros invalidos"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error de desconocido de servidor o de conexion con wasabi"
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Proceso de actualizacion exitoso"
                    )
            }
    )
    @PutMapping("/galeria/actualizar/{idGaleria}")
    public ResponseEntity<GaleriaDTO> updateGaleria(
            @PathVariable(value = "idGaleria", required = true) String idGaleria,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "imagen-perfil", required = false) MultipartFile imagenPerfil,
            @RequestParam(value = "imagen-banner", required = false) MultipartFile imagenBanner,
            HttpServletRequest request
    ){


        return galeriaService.updateGaleria(
                idGaleria,
                nombre,
                imagenPerfil,
                imagenBanner,
                request
        );
    }

}
