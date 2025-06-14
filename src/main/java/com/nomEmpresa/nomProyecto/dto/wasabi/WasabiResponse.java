package com.nomEmpresa.nomProyecto.dto.wasabi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class WasabiResponse {

    @JsonProperty("objectSummaries")
    private List<BucketObject> archivos;

    @JsonProperty("bucketName")
    private String bucketContenedor;

    @JsonProperty("keyCount")
    private Integer cantidadDeElementos;
}
