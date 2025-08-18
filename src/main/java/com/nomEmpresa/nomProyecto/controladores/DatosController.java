package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.modelos.DatosAuxiliaresDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.ServicioDto;
import com.nomEmpresa.nomProyecto.servicio.DatosAuxiliaresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Controlador de datos de negocio",
        description = "Acciones relacionadas con obtener y manipular los datos e informacion del negocio"
)
@RestController
@RequestMapping("/datos")
public class DatosController {

    private final DatosAuxiliaresService datosAuxiliaresService;





    @Autowired
    public DatosController(DatosAuxiliaresService datosAuxiliaresService) {
        this.datosAuxiliaresService = datosAuxiliaresService;
    }







    @Operation(
            summary = "Obtiene los datos del negocio"
    )
    @GetMapping
    public ResponseEntity<DatosAuxiliaresDTO> getDatosAuxiliares(){
        return datosAuxiliaresService.traer();
    }






    @Operation(
            summary = "Actualiza los datos del negocio",
            description = "Los datos enviados se guardarán tal cual"
    )
    @PutMapping
    public ResponseEntity<DatosAuxiliaresDTO> setDatosAuxiliares(
            @RequestParam(required = false) String presentacion,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String celular,
            @RequestParam(required = false) String horario,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String facebook,
            @RequestParam(required = false) String instagram,
            @RequestParam(required = false) String whatsapp
    ){
        return datosAuxiliaresService.editar(new DatosAuxiliaresDTO(
                presentacion,
                direccion,
                celular,
                horario,
                email,
                facebook,
                instagram,
                whatsapp,
                null
        ));
    }






    @Operation(
            summary = "Agrega un servicio a los disponibles"
    )
    @PostMapping("/servicios")
    public ResponseEntity<DatosAuxiliaresDTO> agregarServicio(ServicioDto servicioDto){
        return datosAuxiliaresService.agregarServicio(servicioDto);
    }




    @Operation(
            summary = "Elimina un servicio disponible por su título"
    )
    @DeleteMapping("/servicios")
    public ResponseEntity<DatosAuxiliaresDTO> eliminarServicio(
            @RequestParam("Titulo") String titulo
    ){
        return datosAuxiliaresService.eliminarServicio(titulo);
    }
}
