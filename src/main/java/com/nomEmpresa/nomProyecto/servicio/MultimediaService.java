package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class MultimediaService {

    @Autowired
    private IGaleriaRepository galeriaRepository;

    @Autowired
    private BucketService bucketService;


    /**
     * Agrega un archivo multimedia a una galeria existente
     *
     * @param idGaleria Galeria a la cual pertenece
     * @param crudo Archivo en cuestión
     * @param request Solicitud para formular el header location
     * @return Confirmación de guardado
     */
    public ResponseEntity<String> agregarMultimedia(
            String idGaleria,
            MultipartFile crudo,
            HttpServletRequest request
    ){
        Optional<Galeria> galeria = galeriaRepository.findById(idGaleria);
        //Agregar el archivo a una entrada en BBDD como mutimedia

        if(galeria.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return bucketService.uploadMulti(galeria.get(), crudo, request);
    }



    /**
     * Lista los archivos multimedia que se encuentran en una galeria especifica
     *
     * @param idGaleria Id de la galeria en cuestión
     * @return Galeria con archivos e información colateral
     */
    public ResponseEntity<GaleriaDTO> listarMulti(String idGaleria) {
        Optional<Galeria> galeria = galeriaRepository.findById(idGaleria);

        if(galeria.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ofNullable(galeria.get().getDTO());
    }


    /**
     * Obtiene un archivo multimedia a través de su path relativo al bucket
     *
     * @param urlMultimedia Url relativa al bucket
     * @return Archivo multimedia solicitado
     */
    public ResponseEntity<byte[]> getArchivo(String urlMultimedia) {
        return bucketService.getArchivo(urlMultimedia);
    }






    /**
     * Elimina un archivo de la Base de Datos y de Wasabi
     *
     * @param rutaArchivo Ruta del archivo relativa al Bucket
     * @return Confirmacion de eliminacion
     */
    public ResponseEntity<String> deleteArchivo(String rutaArchivo) {
        return bucketService.deleteMulti(rutaArchivo);
    }
}
