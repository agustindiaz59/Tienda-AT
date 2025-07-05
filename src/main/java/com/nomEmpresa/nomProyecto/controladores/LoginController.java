package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.servicio.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(
        name = "Controlador de login",
        description = "Métodos relacionados a la administracion de usuarios"
)
@RestController
@RequestMapping("/login")
public class LoginController {


    private final AdministradorService administradorService;


    @Autowired
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



    @Operation(
            summary = "Verifica las credenciales del usuario",
            description = "Método público, verifica las credenciales del usuario (usuario y contraseña)",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Las credenciales son válidas, és un Administrador registrado en el sistema, pueden utilizarce en solicitudes HTTP basic"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Las credenciales son invalidas (usuario y/o contraseña), no representa un administrador registrado en el sistema, tambien verificar header Content-Type=application/json"
                    )

            }
    )
    @PostMapping("/verificar")
    public ResponseEntity<AdministradorDTO> verificar(
            @RequestBody
            AdministradorDTO dto
    ){
        Optional<Administrador> administrador = Optional.of(administradorService.consultarAdministrador(dto.nombre()));

        //No existe en la base de datos
        if(administrador.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(dto);
        }


        boolean existe = administradorService.compararContrasenias(dto.contrasenia(), administrador.get().getPassword());

        if (existe){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        }else{
            //No coincide la contraseña
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(dto);
        }
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





    @Operation(
            summary = "Elimina un usuario",
            description = "Recibe el nombre del usuario y lo elimina del sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "203",
                            description = "Usuario eliminado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado en el sistema"
                    )
            }
    )
    @DeleteMapping("/eliminar")
    public ResponseEntity<AdministradorDTO> eliminarAdmin(
            @RequestParam("nombre") String nombreUsuario
    ){
        return administradorService.eliminarAdministrador(nombreUsuario);
    }
}
