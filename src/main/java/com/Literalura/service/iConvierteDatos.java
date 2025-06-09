package com.Literalura.service;

public interface iConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
