package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.repositorio.IAdministradorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;


@SpringBootTest
class LoginControllerTest {
    //Mocks
    @MockitoBean
    private IAdministradorRepository administradorRepository;

    //Dependencias
    @Autowired
    private PasswordEncoder passwordEncoder;


    //Controlador a probar
    @Autowired
    private LoginController loginController;






    @BeforeEach
    void setUp() {
        Mockito
                .when(administradorRepository.findByNombre("admin"))
                .thenReturn(Optional.of(new Administrador("admin", passwordEncoder.encode("qwerty"))));
        Mockito
                .when(administradorRepository.existsByNombre("admin"))
                .thenReturn(true);
    }



    @Test
    void modificarAdmin() {
        ResponseEntity response = loginController.modificarAdmin(new AdministradorDTO("admin","qwerty"));

        Assertions.assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
    }



    @Test
    void crearAdmin() {
        ResponseEntity response = loginController.crearAdmin(new AdministradorDTO("admin","qwerty"));

        Assertions.assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
    }



    @Test
    void eliminarAdmin() {
        ResponseEntity response = loginController.eliminarAdmin("admin");

        Assertions.assertEquals(HttpStatusCode.valueOf(202),response.getStatusCode());
    }
}