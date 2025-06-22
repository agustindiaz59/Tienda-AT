package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.dto.respuestas.GaleriaPage;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class AdminControllerTest {

    //Mocks
    @MockitoBean
    private IGaleriaRepository galeriaRepository;

    @MockitoBean
    private IMultimediaRepository multimediaRepository;

    //Controlador a probar
    @Autowired
    private AdminController adminController;





    @BeforeEach
    void setUp() {
        //Instancia 1: Galería vacía
        Galeria galeria1 = new Galeria();
        galeria1.setIdGaleria("AT-GAL001");
        galeria1.setNombre("Galería Vacía");
        galeria1.setFechaDeCreacion(Instant.now().minus(10, ChronoUnit.DAYS));

        // Instancia 2: Galería con notas y sin multimedia
        Galeria galeria2 = new Galeria();
        galeria2.setIdGaleria("AT-GAL002");
        galeria2.setNombre("Galería con notas");
        galeria2.setFechaDeCreacion(Instant.now().minus(5, ChronoUnit.DAYS));

        //Instancia 3: Galería con multimedia asociada (OneToMany) y banner
        Multimedia media1 = new Multimedia();
        media1.setId(1L);
        media1.setSrc("http://ejemplo.com/img1.jpg");
        media1.setFechaModificado(Instant.now().minus(3, ChronoUnit.DAYS));

        Multimedia media2 = new Multimedia();
        media2.setId(2L);
        media2.setSrc("http://ejemplo.com/img2.jpg");
        media2.setFechaModificado(Instant.now().minus(2, ChronoUnit.DAYS));

        Galeria galeria3 = new Galeria();
        galeria3.setIdGaleria("AT-GAL003");
        galeria3.setNombre("Galería con multimedia");
        galeria3.setFechaDeCreacion(Instant.now().minus(7, ChronoUnit.DAYS));

        media1.setIdGaleria(galeria3);
        media2.setIdGaleria(galeria3);
        Set<Multimedia> medias = new HashSet<>();
        medias.add(media1);
        medias.add(media2);

        galeria3.setMultimedias(medias);
        galeria3.setImgBanner(media1); // Reutilizamos una imagen como banner

        Multimedia perfil = new Multimedia();
        perfil.setId(3L);
        perfil.setSrc("http://ejemplo.com/perfil.jpg");
        perfil.setFechaModificado(Instant.now().minus(1, ChronoUnit.DAYS));

        Multimedia banner = new Multimedia();
        banner.setId(4L);
        banner.setSrc("http://ejemplo.com/banner.jpg");
        banner.setFechaModificado(Instant.now().minus(1, ChronoUnit.DAYS));

        //Instancia 4: Galería con imagen de perfil y banner distintos
        Galeria galeria4 = new Galeria();
        galeria4.setIdGaleria("AT-GAL004");
        galeria4.setNombre("Galería destacada");
        galeria4.setFechaDeCreacion(Instant.now().minus(1, ChronoUnit.DAYS));
        galeria4.setImgPerfil(perfil);
        galeria4.setImgBanner(banner);


        Mockito
                .when(galeriaRepository.findAllWithDetails())
                .thenReturn(List.of(galeria1,galeria2,galeria3,galeria4));
        Mockito
                .when(galeriaRepository.findByfechaDeCreacionAfter(Instant.parse("2025-05-01T00:00:00.000Z"), PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(galeria2, galeria3, galeria4)));
        Mockito
                .when(galeriaRepository.save(new Galeria()))
                .thenReturn(galeria3);
        Mockito
                .when(galeriaRepository.existsById("AT-GAL001"))
                .thenReturn(true);
    }


    @Test
    void listarGalerias() {
        ResponseEntity<GaleriaPage> response = adminController.listarGalerias(true,true,Instant.parse("2025-05-01T00:00:00.000Z"),0,10,"DESC");

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

    }

    @Test
    void crearGaleria() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "archivo",                    // nombre del parámetro del formulario
                "imagen.jpg",                       // nombre original del archivo
                "image/jpeg",                       // tipo MIME
                "contenido del archivo".getBytes()  // contenido binario
        );


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/subida");
        request.addHeader("Authorization", "Bearer token_ejemplo");
        request.setParameter("idGaleria", "AT-GAL001");

        ResponseEntity response = adminController.crearGaleria("nueva", multipartFile,multipartFile,request);

        Assertions.assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    void deleteMulti() {
        ResponseEntity response = adminController.deleteMulti("AT-GAL003/http://ejemplo.com/img2.jpg");
    }

    @Test
    void deleteGaleria() {
        ResponseEntity response = adminController.deleteGaleria("AT-GAL001");

        Assertions.assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void updateGaleria() {
        MockMultipartFile multipartFile1 = new MockMultipartFile(
                "archivo",                    // nombre del parámetro del formulario
                "imagen.jpg",                       // nombre original del archivo
                "image/jpeg",                       // tipo MIME
                "contenido del archivo".getBytes()  // contenido binario
        );
        MockMultipartFile multipartFile2 = new MockMultipartFile(
                "archivo",                    // nombre del parámetro del formulario
                "imagen.jpg",                       // nombre original del archivo
                "image/jpeg",                       // tipo MIME
                "contenido del archivo".getBytes()  // contenido binario
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/subida");
        request.addHeader("Authorization", "Bearer token_ejemplo");
        request.setParameter("idGaleria", "AT-GAL001");


        ResponseEntity response = adminController.updateGaleria("AT-GAL001","otro nombre",multipartFile1, multipartFile2,request);

        Assertions.assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
    }
}