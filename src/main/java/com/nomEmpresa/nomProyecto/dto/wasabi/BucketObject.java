package com.nomEmpresa.nomProyecto.dto.wasabi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class BucketObject {

    @JsonProperty("bucketName")
    private String bucketContenedor;

    @JsonProperty("key")
    private String nombre;

    @JsonProperty("size")
    private String tamanio;

    @JsonProperty("lastModified")
    private String ultimaModificacion;

    @JsonProperty("storageClass")
    private String storageClass;

    @JsonProperty("etag")
    private String etag;
}
