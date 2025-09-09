package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.respuestas.DetallesGaleriaPage;
import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.modelos.Nota;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
import com.nomEmpresa.nomProyecto.repositorio.INotaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
public class MultimediaService {


    private final IGaleriaRepository galeriaRepository;

    private final IMultimediaRepository multimediaRepository;

    private final BucketService bucketService;

    private final INotaRepository notaRepository;







    @Autowired
    public MultimediaService(IGaleriaRepository galeriaRepository, IMultimediaRepository multimediaRepository, BucketService bucketService, INotaRepository notaRepository) {
        this.galeriaRepository = galeriaRepository;
        this.multimediaRepository = multimediaRepository;
        this.bucketService = bucketService;
        this.notaRepository = notaRepository;
    }











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
    public ResponseEntity<DetallesGaleriaPage> listarMulti(
            String idGaleria,
            Instant multiDesde,
            Instant notasDesde,
            Pageable multimediaPageSolicitada,
            Pageable notasPageSolicitada
    ) {

        //Verifico que la galeria exista
        Optional<Galeria> galeria = galeriaRepository.findById(idGaleria);
        if(galeria.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        //Traigo las fotos relacionadas a la galeria
        Page<Multimedia> paginaMulti = multimediaRepository
                .findByIdGaleriaAndFechaModificadoAfter(
                        new Galeria(
                                galeria.get().getIdGaleria(),
                                galeria.get().getNombre()
                        ),
                        multiDesde,
                        multimediaPageSolicitada
                );

        //Traigo las notas relacionadas a esa galeria
        Page<Nota> notas = notaRepository.findByGaleriaAndHoraAfter(
                new Galeria(
                galeria.get().getIdGaleria(),
                galeria.get().getNombre()
                ),
                notasDesde,
                notasPageSolicitada
        );


        //Armo la respuesta
        DetallesGaleriaPage respuesta = new DetallesGaleriaPage(galeria.get(), paginaMulti, notas);
        return ResponseEntity.ofNullable(respuesta);
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
            return ResponseEntity
                    .badRequest()
                    .build();
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
                String formatoDeSalida = reader.getFormatName(); // Ej: "JPEG", "PNG"
                int width = originalImage.getWidth() / porcion;
                int height = originalImage.getHeight() / porcion;


                //Reduzco la imagen
                BufferedImage resizedImage = new BufferedImage(width, originalImage.getHeight() / 10, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, width, height, null);
                g.dispose();


                //Devuelvo la imagen a bytes
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, formatoDeSalida, outputStream);

                //Libreria que detecta el tipo MIME del archivo
                Tika tika = new Tika();
                //Arma la respuesta HTTP
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(tika.detect(outputStream.toByteArray())))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline") //; filename=\"" + filename + "\"") // usar en caso de querer mantener el nombre de la imagen entre solicitudes, falta resolver el nombre
                        .body(outputStream.toByteArray());

            } else {
                System.out.println("-- No se pudo determinar el formato de imagen.");
                return ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE)
                        .body(null);
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


    /**
     *
     * @param idGaleria
     * @param nota
     * @return
     */
    public ResponseEntity<GaleriaDTO> agregarNota(String idGaleria, String nota) {
        Optional<Galeria> galeria = galeriaRepository.findById(idGaleria);

        //Verifico la existencia
        if(galeria.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        //Agrego la nota
        Galeria editado = galeria.get();
        Nota nuevaNota = new Nota(nota, Instant.now());
        nuevaNota.setGaleria(editado);
        editado.getNotas().add(nuevaNota);

        //Guardo en la BBDD
        galeriaRepository.save(editado);

        return ResponseEntity
                .ok(DTOMapper.galeriaDTO(editado,true,true));
    }


    @Transactional
    public ResponseEntity<String> deleteNota(String idGaleria, String contenidoNota) {
        Optional<Galeria> optionalGaleria = galeriaRepository.findById(idGaleria);

        if (optionalGaleria.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("-- Galeria con id: " + idGaleria + " no existente en el sistema");
        }

        Optional<Nota> existe = notaRepository.findByContenidoIgnoreCaseAndGaleria(contenidoNota, optionalGaleria.get());

        if(existe.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("-- Nota con contenido: " + contenidoNota + " no encontrada en la galeria " + idGaleria);        }
        else
            notaRepository.deleteByContenidoAndGaleria(contenidoNota, optionalGaleria.get());

        return ResponseEntity
                .ok("-- Nota eliminada correctamente de la galeria " + idGaleria + "\n-- Nota: " + contenidoNota);
    }




        /*
        //Verifico que la galeria exista
        Optional<Galeria> galeria = galeriaRepository.findById(idGaleria);

        if (galeria.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("-- Galeria con id: " + idGaleria + " no existente en el sistema");
        }


        //Obtengo todas las notas de esa galeria
        List<Nota> notas = galeria.get().getNotas();

        //Elimino la nota de la galeria
        for(Nota n : notas){
            if(n.getContenido().equalsIgnoreCase(contenidoNota)){
                notas.remove(n);
                n.setGaleria(null);
            }
        }

        //Guardo los cambios en la galeria
        Galeria editada = galeria.get();
        editada.setNotas(notas);
        galeriaRepository.save(editada);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("-- Nota eliminada correctamente de la galeria " + idGaleria + "\n-- Nota: " + contenidoNota);

//        //Verifico que la nota exista en la galeria
//        if(notas.contains(buscado)){
//
//            //Elimino la nota de la galeria
//            notas.remove(buscado);
//
//            //Guardo los cambios en la galeria
//            Galeria editada = galeria.get();
//            editada.setNotas(notas);
//            galeriaRepository.save(editada);
//
//            return ResponseEntity
//                    .status(HttpStatus.NO_CONTENT)
//                    .body("-- Nota eliminada correctamente de la galeria " + idGaleria + "\n-- Nota: " + contenidoNota);
//        }else {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body("-- Nota no existente en la galeria " + idGaleria + "\n-- Nota: " + contenidoNota);
//        }

*/


}
