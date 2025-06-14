package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaPage;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Esta clase debe administrar la creacion de galerias y permitir
 * que a cada galeria se le agregen fotos y videos multimedia
 */
@Service
public class GaleriaService {


    private final IGaleriaRepository galeriaRepository;

    private final BucketService bucketService;







    @Autowired
    public GaleriaService(IGaleriaRepository galeriaRepository, BucketService bucketService) {
        this.galeriaRepository = galeriaRepository;
        this.bucketService = bucketService;
    }










    /**
     * Lista todas las galerias en el sistema, devuelve una lista de DTO
     *
     * @return Listado de galerias
     */
    @Deprecated
    public ResponseEntity<List<GaleriaDTO>> listarGalerias(Boolean archivos, Boolean notas) {
        List<GaleriaDTO> galeriasDTO;

        //Obtiene todas las galerias con las condiciones
        galeriasDTO = galeriaRepository.findAllWithDetails()
                    .stream()
                    .map(g -> DTOMapper.galeriaDTO(g, archivos, notas))
                    .toList();

        return ResponseEntity.ofNullable(galeriasDTO);
    }







    /**
     * Lista todas las galerias en el sistema, devuelve una Pagina de Galerias
     *
     * @return Listado de galerias
     */
    public ResponseEntity<GaleriaPage> listarGalerias(
            Boolean archivos,
            Boolean notas,
            Instant desde,
            Pageable paginaSolicitada
    ) {
        GaleriaPage galeriasPage = new GaleriaPage();

            Page<Galeria> aux = galeriaRepository.findByfechaDeCreacionAfter(desde,paginaSolicitada);
            galeriasPage.setGalerias(aux
                    .getContent()
                    .stream()
                    .map( g -> DTOMapper.galeriaDTO(g,archivos,notas))
                    .toList()
            );
            galeriasPage.setPaginaActual(aux.getNumber());
            galeriasPage.setTotalDePaginas(aux.getTotalPages());
            galeriasPage.setTamaño(aux.getSize());
            galeriasPage.setTotalDeElementos(aux.getTotalElements());


        return ResponseEntity.ofNullable(galeriasPage);
    }










    /**
     * Lista todas las galerias en el sistema
     *
     * @return Listado de galerias
     */
    @Deprecated(since = "10/06/2025")
    public ResponseEntity<List<GaleriaDTO>> listarGaleriasSinArchivos() {
        List<GaleriaDTO> galeriasDTO = galeriaRepository
                .findAllWithDetails()
                .stream()
                .map(galeria -> DTOMapper.galeriaDTO(galeria,false,false))
                .toList();

        return ResponseEntity.ofNullable(galeriasDTO);
    }







//    @Deprecated
//    /**
//     * Crea una nueva galeria en el sistema
//     *
//     * @param nombre Nombre de la galeria
//     * @param detalles Detalles o descripción de la galeria
//     * @return Confirmacion de creación
//     */
//    public ResponseEntity<GaleriaDTO> crearGaleria(String nombre, String detalles){
//        Galeria nuevo = galeriaRepository.save(new Galeria(nombre, detalles));
//
//        if(galeriaRepository.findById(nuevo.getIdGaleria()).isPresent()){
//            return ResponseEntity.ok(nuevo.getDTO());
//        }
//
//        return ResponseEntity.internalServerError().build();
//    }






//    /**
//     * Crea una nueva galeria en el sistema
//     *
//     * @param nombre Nombre de la galeria
//     * @return Confirmacion de creación
//     */
//    @Deprecated
//    public ResponseEntity<GaleriaDTO> crearGaleria(String nombre){
//        Galeria aux = new Galeria();
//        aux.setNombre(nombre);
//        Galeria nuevo = galeriaRepository.save(aux);
//
//        if(galeriaRepository.findById(nuevo.getIdGaleria()).isPresent()){
//            return ResponseEntity.ok(nuevo.getDTO());
//        }
//
//        return ResponseEntity.internalServerError().build();
//    }





    /**
     * Crea una nueva galeria en el sistema
     *
     * @param nombre Nombre de la galeria
     * @return Confirmacion de creación
     */
    @Transactional
    public ResponseEntity<GaleriaDTO> crearGaleria(
            String nombre,
            MultipartFile imagenPerfil,
            MultipartFile imagenBanner,
            HttpServletRequest request
    ){
        //Crear una nueva galería
        Galeria nuevo = galeriaRepository.save(new Galeria());
        nuevo.setNombre(nombre);

        //Verifico la existencia de las imagenes antes de nada
        if(imagenPerfil != null && Validador.validarFormatoMultimedia(imagenPerfil)){
            Multimedia perfil = new Multimedia();
            String urlPerfil = bucketService
                    .uploadMulti(nuevo,imagenPerfil,request)
                    .getHeaders()
                    .getFirst("Location");
            perfil.setSrc(urlPerfil);
            perfil.setIdGaleria(nuevo);
            nuevo.setImgPerfil(perfil);
        }

        if (imagenBanner != null && Validador.validarFormatoMultimedia(imagenBanner)){
            Multimedia banner = new Multimedia();

            String urlBanner = bucketService
                    .uploadMulti(nuevo,imagenBanner,request)
                    .getHeaders()
                    .getFirst("Location");

            //Establezco las url de las imagenes en Wasabi
            banner.setSrc(urlBanner);

            //Relaciono las imagenes con la galería
            banner.setIdGaleria(nuevo);

            //Estalezco las imagenes como foto de perfil y de banner
            nuevo.setImgBanner(banner);
        }



        //Guardar la galería con los cambios anteriores
        galeriaRepository.save(nuevo);

        if(galeriaRepository.findById(nuevo.getIdGaleria()).isPresent()){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(DTOMapper.galeriaDTO(nuevo,true,true)); //TODO Verificar repercuciones en el metodo
        }

        return ResponseEntity.internalServerError().build();
    }
























    /**
     * Elimina una galería del sistema y de Wasabi
     *
     * @param idGaleria Id de la galeria en cuestión
     * @return Confirmacion de eliminacion
     */
    @Transactional
    public ResponseEntity<String> deleteGaleria(String idGaleria) {
        //Eliminar la galeria e imagenes asociadas de BBDD

        //Eliminar la carpeta de Wasabi
        return bucketService.deleteGaleria(idGaleria);
    }









    /**
     * Actualiza los datos de una galería existente
     *
     * @param idGaleria Id de la galería en cuestion
     * @param nombre Nuevo nombre para la galería
     * @return Confirmación de modificación
     */
    @Transactional
    public ResponseEntity<GaleriaDTO> updateGaleria(
            String idGaleria,
            String nombre,
            MultipartFile imagenPerfil,
            MultipartFile imagenBanner,
            HttpServletRequest request
    ){
        //Busco el registro existente
        Optional<Galeria> vieja = galeriaRepository.findById(idGaleria);

        if(vieja.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        //Verifico que campos se enviaron y cuales no
        Galeria aux = vieja.get();
        boolean hayNombre = (nombre != null);
        boolean hayBanner = (imagenBanner != null);
        boolean hayPerfil = (imagenPerfil != null);

        if(!hayNombre && !hayBanner && !hayPerfil){
            return ResponseEntity.badRequest().build();
        }

        if(hayPerfil){
            Multimedia pefil = new Multimedia();
            pefil.setSrc(bucketService
                    .uploadMulti(aux, imagenPerfil, request)
                    .getHeaders().getFirst("location")
            );
            pefil.setIdGaleria(aux);
            aux.setImgPerfil(pefil);
        }

        if(hayBanner){
            Multimedia banner = new Multimedia();
            banner.setSrc(bucketService
                    .uploadMulti(aux, imagenBanner, request)
                    .getHeaders().getFirst("location")
            );
            banner.setIdGaleria(aux);
            aux.setImgBanner(banner);
        }

        if(hayNombre){
            aux.setNombre(nombre);
        }


        //Actualizo el registro manteniendo el ID y los datos ya existentes
        Optional<Galeria> nueva = Optional.of(galeriaRepository.save(aux));

        if(nueva.isPresent()){
            return ResponseEntity.ok(DTOMapper.galeriaDTO(nueva.get(),true,true));
        }

        return ResponseEntity.internalServerError().body(DTOMapper.galeriaDTO(nueva.get(),true,true));
    }
















}
