package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
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
    public ResponseEntity<byte[]> getArchivoCompleto(String urlMultimedia) {
        return bucketService.getArchivo(urlMultimedia);
    }






    /**
     * Obtiene un archivo multimedia a través de su path relativo al bucket
     *
     * @param urlMultimedia Url relativa al bucket
     * @return Archivo multimedia solicitado
     */
    public ResponseEntity<byte[]> getArchivoComprimido(String urlMultimedia, Integer porcion){

        //Obtengo la imagen en crudo
        byte[] crudo = bucketService.getArchivo(urlMultimedia).getBody();

        if(crudo == null){
            System.out.println("-- El arreglo de bytes esta vacio. Imagen no encontrada");
            return ResponseEntity.badRequest().build();
        }

        //Obtengo la extension del archivo
        try (ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(crudo))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {

                //Objetos que leen los bytes como una imagen
                ByteArrayInputStream inputStream = new ByteArrayInputStream(crudo);
                BufferedImage originalImage = ImageIO.read(inputStream);
                ImageReader reader = readers.next();

                //Obtiene una decima parte de la imagen
                String format = reader.getFormatName(); // Ej: "JPEG", "PNG"
                int width = originalImage.getWidth() / porcion;
                int height = originalImage.getHeight() / porcion;


                //Reduzco la imagen
                BufferedImage resizedImage = new BufferedImage(width, originalImage.getHeight() / 10, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, width, height, null);
                g.dispose();


                //Devuelvo la imagen a bytes
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, format, outputStream);

                //Libreria que detecta el tipo MIME del archivo
                Tika tika = new Tika();
                //Arma la respuesta HTTP
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(tika.detect(outputStream.toByteArray())))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline") //; filename=\"" + filename + "\"") // usar en caso de querer mantener el nombre de la imagen entre solicitudes, falta resolver el nombre
                        .body(outputStream.toByteArray());

            } else {
                System.out.println("-- No se pudo determinar el formato de imagen.");
                return ResponseEntity.internalServerError().build();
            }
        }catch (IOException exception){
            System.out.println("-- Error en la conversion de bytes de la imagen");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

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
