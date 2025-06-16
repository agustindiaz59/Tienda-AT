package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.servicio.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {


    private AdministradorService administradorService;


    public LoginController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }



    /**
     * Crea un nuevo usuario en el sistema
     *
     * @param dto Objeto que representa un usuario con nombre y contrasenia
     * @return datos del usuario guardado
     */
    @Operation(
            summary = "Cambiar contrasenia",
            description = "Cambia la contrasenia de un usuario existente identificandolo por su nombre",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario actualizado"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales invalidas"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de sintaxis de la solicitud"
                    )
            }
    )
    @PutMapping("/modificar/contrasenia")
    public ResponseEntity<AdministradorDTO> modificarAdmin(
            @RequestBody
            AdministradorDTO dto
    ){
        return administradorService.cambiarContraseniaAdmin(dto);
    }



    @PostMapping("/verificar")
    public ResponseEntity<AdministradorDTO> verificar(
            @RequestBody
            AdministradorDTO dto
    ){
        return administradorService.verificarUsuario(dto);
    }




    /**
     * Crea un nuevo usuario en el sistema
     *
     * @param dto Objeto que representa un usuario con nombre y contrasenia
     * @return datos del usuario guardado
     */
    @Operation(
            summary = "Crear un nuevo Administrador en el sistema",
            description = "Recibe un usuario y contrasenia, si el usuario ya existe en el sistema lo actualiza, sino lo crea",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario creado"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales invalidas"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de sintaxis de la solicitud"
                    )
            }
    )

    @PostMapping("/crear")
    public ResponseEntity<AdministradorDTO> crearAdmin(
            @RequestBody
            @Validated
            AdministradorDTO dto
    ){
        return administradorService.crearAdmin(dto);
    }





    @DeleteMapping("/eliminar")
    public ResponseEntity<AdministradorDTO> eliminarAdmin(
            @RequestParam("nombre") String nombreUsuario
    ){
        return administradorService.eliminarAdministrador(nombreUsuario);
    }
}
