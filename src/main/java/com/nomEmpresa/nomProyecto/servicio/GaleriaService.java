package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.respuestas.PaginaPersonalizada;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
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
import java.util.Optional;


/**
 * Esta clase debe administrar la creacion de galerias y permitir
 * que a cada galeria se le agregen fotos y videos multimedia
 */
@Service
public class GaleriaService {


    private final IGaleriaRepository galeriaRepository;

    private final IMultimediaRepository multimediaRepository;

    private final BucketService bucketService;







    @Autowired
    public GaleriaService(IGaleriaRepository galeriaRepository, BucketService bucketService, IMultimediaRepository multimediaRepository) {
        this.galeriaRepository = galeriaRepository;
        this.bucketService = bucketService;
        this.multimediaRepository = multimediaRepository;
    }
















    /**
     * Lista todas las galerias en el sistema, devuelve una Pagina de Galerias
     *
     * @return Listado de galerias
     */
    public ResponseEntity<PaginaPersonalizada<GaleriaDTO>> listarGalerias(
            Boolean archivos,
            Boolean notas,
            Instant archivosDesde,
            Instant notasDesde,
            Pageable paginaSolicitada
    ) {
        //Busco todas las galerias luego de cierta fecha
        Page<Galeria> aux = galeriaRepository.findByfechaDeCreacionAfter(archivosDesde,paginaSolicitada);

        //Armo la respuesta
        PaginaPersonalizada<GaleriaDTO> galeriaPage = PaginaPersonalizada
                .<GaleriaDTO>builder()
                .contenido(
                        aux
                            .getContent()
                            .stream()
                            .map( g -> DTOMapper.galeriaDTO(g,archivos,notas,archivosDesde,notasDesde))
                            .toList()
                )
                .paginaActual(aux.getNumber())
                .tamaño(aux.getSize())
                .totalDePaginas(aux.getTotalPages())
                .totalDeElementos(aux.getTotalElements())
                .build();
        return ResponseEntity.ofNullable(galeriaPage);
    }































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
                    .body(DTOMapper.galeriaDTO(nuevo,true,true));
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
