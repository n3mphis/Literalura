package com.Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        int id,
        String title,
        List<DatosAutor> authors,
        List<String> languages,
        @JsonAlias("download_count") int downloadCount
) {
}
