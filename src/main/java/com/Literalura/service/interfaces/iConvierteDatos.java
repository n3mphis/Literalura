package com.Literalura.service.interfaces;

public interface iConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
