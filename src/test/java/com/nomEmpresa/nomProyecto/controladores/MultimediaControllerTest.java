package com.nomEmpresa.nomProyecto.controladores;

import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
import com.nomEmpresa.nomProyecto.servicio.BucketService;
import com.nomEmpresa.nomProyecto.servicio.GaleriaService;
import com.nomEmpresa.nomProyecto.servicio.MultimediaService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MultimediaControllerTest {

    //Mocks

    private final IGaleriaRepository galeriaRepository = Mockito.mock(IGaleriaRepository.class);

    private final BucketService bucketService = Mockito.mock(BucketService.class);

    private final IMultimediaRepository iMultimediaRepository = Mockito.mock(IMultimediaRepository.class);
    //


    private GaleriaService galeriaService;

    private MultimediaService multimediaService;

    private MultimediaController multimediaController;



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
        galeria2.setNotas(List.of("Nota 1", "Nota 2", "Nota 3"));

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


        //Dada una entrada el Mock devuelve una salida
        Mockito
                .when(galeriaRepository.findAllWithDetails())
                .thenReturn(List.of(galeria1, galeria2, galeria3, galeria4));
        Mockito
                .when(galeriaRepository.findByfechaDeCreacionAfter(Instant.parse("2024-10-01T00:00:00Z"), PageRequest.of(0,10)))
                .thenReturn(new PageImpl<Galeria>(List.of(galeria1, galeria2, galeria3, galeria4),PageRequest.of(0,10),4));
        Mockito
                .when(galeriaRepository.findById("AT-GAL001"))
                .thenReturn(Optional.of(galeria1));
        Mockito
                .when(iMultimediaRepository.findByIdGaleriaAndFechaModificadoAfter(galeria1,Instant.parse("2024-10-01T00:00:00Z"), PageRequest.of(0,10)))
                .thenReturn(new PageImpl<Multimedia>(medias.stream().toList()));


        //Inyeccion de dependencias configuradas
        galeriaService = new GaleriaService(galeriaRepository, bucketService, iMultimediaRepository);
        multimediaService = new MultimediaService(galeriaRepository, iMultimediaRepository, bucketService);
        multimediaController = new MultimediaController(galeriaService, multimediaService);
    }


    @Test
    void listarMulti() {
        ResponseEntity respuesta = multimediaController.listarMulti("AT-GAL001",Instant.parse("2024-10-01T00:00:00Z"),0,10);

        Assertions.assertEquals(respuesta.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void uploadMulti() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "archivo",                       // nombre del parámetro del formulario
                "imagen.jpg",                    // nombre original del archivo
                "image/jpeg",                    // tipo MIME
                "contenido del archivo".getBytes() // contenido binario
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/subida");
        request.addHeader("Authorization", "Bearer token_ejemplo");
        request.setParameter("idGaleria", "AT-GAL001");

        ResponseEntity respuesta = multimediaController.uploadMulti("AT-GAL001",multipartFile,request);

        //TODO simular el comportamiento de wasabi
        assertEquals(respuesta.getStatusCode(), HttpStatusCode.valueOf(201));
    }

    @Test
    void traerMultimedia() {

        //TODO simular bucket service
        ResponseEntity<byte[]> respuesta = multimediaController.traerMultimedia("http://ejemplo.com/banner.jpg",false,1);

        Assertions.assertEquals(respuesta.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void subirNota() {
        ResponseEntity response = multimediaController.subirNota("AT-GAL001", "nota de eejemplo");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}