package com.nomEmpresa.nomProyecto.servicio;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomEmpresa.nomProyecto.dto.wasabi.WasabiResponse;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.repositorio.IGaleriaRepository;
import com.nomEmpresa.nomProyecto.repositorio.IMultimediaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class BucketService {

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private ObjectMapper mapper;

    @Value("${bucket}")
    private String nombreBucket;

    @Autowired
    private IMultimediaRepository multimediaRepository;

    @Autowired
    private IGaleriaRepository galeriaRepository;



    public ResponseEntity<String> uploadMulti(
            Galeria galeria,
            MultipartFile crudo,
            HttpServletRequest request
    ){

        //Verifico la existencia de la galeria
        Optional<Galeria> galeriaBuscada = galeriaRepository.findById(galeria.getIdGaleria());
        if(galeriaBuscada.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        //Wasabi espera un archivo del tipo File
        File archivo = multipartFileToFile(crudo);

        //Formateo el nombre del archivo en wasabi y lo guardo
        //idGaleria + / + nombreArchivo
        s3.putObject(nombreBucket,galeria.getIdGaleria() + "/" +archivo.getName(),archivo);

        //Guardo el registro en la base de datos
        Multimedia nuevoArchivo = new Multimedia();
        nuevoArchivo.setSrc(getUrlDeRecurso(galeria.getIdGaleria(), request,archivo));
        nuevoArchivo.setIdGaleria(galeria);
        multimediaRepository.save(nuevoArchivo);

        return ResponseEntity.created(URI.create(nuevoArchivo.getSrc())).build();
    }





    /**
     * Formatea la URL que se devolverá en la solicitud en el header Location
     *
     * @param idGaleria Id de la galeria a la cual pertenece el archivo
     * @param request Solicitud para obtener el dominio del sitio
     * @param archivo Archivo en cuestión para formatear
     * @return String con la URL que debe encontrarse el archivo
     */
    public String getUrlDeRecurso(String idGaleria,HttpServletRequest request,File archivo){
        String urlRecurso = request.getScheme() +
                "://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                "/" +
                "multi/archivo?urlMultimedia=" + //Cambiar en caso de alterar la url /media
                idGaleria +
                "/" +
                archivo.getName();

        return urlRecurso;
    }







    /**
     * Lista todos los archivos en un bucket
     * @return Respuesta HTTP con
     */
    @Deprecated
    public ResponseEntity<WasabiResponse> getAll() {
        ListObjectsV2Result list = s3.listObjectsV2("galerias");

        WasabiResponse response = mapper.convertValue(list, WasabiResponse.class);

        return ResponseEntity.ofNullable(response);

    }










    public File multipartFileToFile(MultipartFile file){
        if (file == null) {
            return null; // O lanza una excepción
        }
        try {

            //Obtengo el nombre original del archivo y su extension
            String nombre;
            if(file.getOriginalFilename() == null){
                nombre = "unnamed";
            }else {
                nombre = file.getOriginalFilename().replace(" ", "-");
            }
            String extension = "";


            int b = nombre.lastIndexOf("\\");
            int i = nombre.lastIndexOf('.');

            extension = nombre.substring(i).toLowerCase();
            nombre = nombre.substring(b+1,i);

            Path tempFile = Files.createTempFile(nombre,extension);
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            return tempFile.toFile();
        }catch (IOException e){
            System.out.println("--Error convirtiendo el MultipartFile a archivo" + e.getMessage());
            return null;
        }
    }






    /**
     * Elimina un archivo multimedia a través de su URL de Wasabi
     *
     * @param urlMultimedia Url del archivo en cuestión
     * @return Confirmacion y estado de la solicitud
     */
    public ResponseEntity<String> deleteMulti(String urlMultimedia) {
        try {
            s3.deleteObject(nombreBucket, urlMultimedia);
            return ResponseEntity.noContent().build();
        } catch (SdkClientException e) {
            System.out.println("--Error eliminando el archivo");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }





    /**
     * Obtiene un archivo desde el almacenamiento de Wasabi a través de su URL
     *
     * @param urlMultimedia URL del archivo en cuestion
     * @return
     */
    public ResponseEntity<byte[]> getArchivo(String urlMultimedia) {


        try {
            //Trae el archivo desde wasabi y verifica que se obtenga
            S3Object archivo = s3.getObject(nombreBucket, urlMultimedia);
            S3ObjectInputStream inputStream = archivo.getObjectContent();


            //Convierte el archivo en una cadena de bytes para enviar
            byte[] content = inputStream.readAllBytes();
            String contentType = archivo.getObjectMetadata().getContentType();
            String filename = urlMultimedia.substring(urlMultimedia.lastIndexOf("/") + 1);

            // Validaciones básicas
            if (content == null || content.length == 0) {
                return ResponseEntity.notFound().build();
            }
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream"; // Valor por defecto
            }

            //Arma la respuesta HTTP
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(content);

        }

        catch(SdkClientException e){
            System.out.println("-- Error trayendo el archivo: " + urlMultimedia);
            return ResponseEntity.notFound().build();
        }

        catch (IOException e){
            System.out.println("-- Error convirtiendo el archivo: " + urlMultimedia);
            return ResponseEntity.internalServerError().build();
        }
    }










    public ResponseEntity<String> deleteGaleria(String idGaleria) {
        
        if(!galeriaRepository.existsById(idGaleria)){
            return ResponseEntity.notFound().build();
        }

        try {
            s3.deleteObject(nombreBucket, idGaleria);
            galeriaRepository.deleteById(idGaleria);
            return ResponseEntity.noContent().build();
        }catch (SdkClientException e){
            System.out.println("-- Error eliminando la galería: " + idGaleria);
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


}
