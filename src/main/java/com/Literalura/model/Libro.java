package com.Literalura.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idioma;
    private double cantidadDescargas;

    public Libro(){}

    public Libro(String titulo, String autor, String idioma, double cantidadDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.cantidadDescargas = cantidadDescargas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public double getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(double cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    @Override
    public String toString() {
        return "----- Libro -----\n" +
                "ID: " + id + "\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + autor + "\n" +
                "Idioma: " + idioma + "\n" +
                "Número de descargas: " + cantidadDescargas;
    }
}
