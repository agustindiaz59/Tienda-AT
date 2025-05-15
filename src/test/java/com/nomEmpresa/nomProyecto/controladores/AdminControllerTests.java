package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.servicio.AdministradorService;
import com.nomEmpresa.nomProyecto.servicio.GaleriaService;
import com.nomEmpresa.nomProyecto.servicio.MultimediaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTests {


    @Mock
    private GaleriaService galeriaService;

    @Mock
    private MultimediaService multimediaService;

    @Mock
    private AdministradorService administradorService;

    @InjectMocks
    private AdminController adminController;






    @Test
    public void probarLogin(){
        
    }
}
