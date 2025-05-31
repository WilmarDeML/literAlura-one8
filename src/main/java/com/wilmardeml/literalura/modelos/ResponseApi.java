package com.wilmardeml.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseApi(
        @JsonProperty("results") List<DatosLibro> libros,
        @JsonProperty("count") Integer totalLibros
) {}
