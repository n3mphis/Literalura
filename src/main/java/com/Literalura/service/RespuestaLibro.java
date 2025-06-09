package com.Literalura.service;

import com.Literalura.model.DatosLibro;

import java.util.List;

public record RespuestaLibro(
        int count,
        String next,
        String previous,
        List<DatosLibro> results
) {
}
