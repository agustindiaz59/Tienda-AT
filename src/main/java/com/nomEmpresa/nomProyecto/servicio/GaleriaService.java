package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Esta clase debe administrar la creacion de galerias y permitir
 * que a cada galeria se le agregen fotos y videos multimedia
 */
@Service
public class GaleriaService {

    @Autowired
    private IGaleriaRepository galeriaRepository;

    @Autowired
    private BucketService bucketService;
    @Autowired
    private IMultimediaRepository iMultimediaRepository;


    /**
     * Lista todas las galerias en el sistema
     *
     * @return Listado de galerias
     */
    public ResponseEntity<List<GaleriaDTO>> listarGalerias(Boolean archivos) {
        List<GaleriaDTO> galeriasDTO;

        if(!archivos){
            galeriasDTO = galeriaRepository.findAllWithDetails()
                    .stream()
                    .map(g -> GaleriaMapper.galeriaDTO(g, false))
                    .toList();
        }else {
            galeriasDTO = galeriaRepository
                .findAllWithDetails()
                .stream()
                .map(Galeria::getDTO)
                .toList();
        }


        return ResponseEntity.ofNullable(galeriasDTO);
    }





    /**
     * Lista todas las galerias en el sistema
     *
     * @return Listado de galerias
     */
    public ResponseEntity<List<GaleriaDTO>> listarGaleriasSinArchivos() {
        List<GaleriaDTO> galeriasDTO = galeriaRepository
                .findAllWithDetails()
                .stream()
                .map(Galeria::getDTO)
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
        if(imagenPerfil != null){
            Multimedia perfil = new Multimedia();
            String urlPerfil = bucketService
                    .uploadMulti(nuevo,imagenPerfil,request)
                    .getHeaders()
                    .getFirst("Location");
            perfil.setSrc(urlPerfil);
            perfil.setIdGaleria(nuevo);
            nuevo.setImgPerfil(perfil);
        }

        if (imagenBanner != null){
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
                    .body(nuevo.getDTO());
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
            return ResponseEntity.ok(nueva.get().getDTO());
        }

        return ResponseEntity.internalServerError().body(nueva.get().getDTO());
    }
















}
