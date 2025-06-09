package com.Literalura.principal;

import com.Literalura.model.DatosAutor;
import com.Literalura.model.DatosLibro;
import com.Literalura.model.Libro;
import com.Literalura.repository.LibroRepository;
import com.Literalura.service.ConsumoAPI;
import com.Literalura.service.ConvertirDatos;
import com.Literalura.service.RespuestaLibro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvertirDatos conversor = new ConvertirDatos();
    private List<DatosLibro> datosLibro = new ArrayList<>();
    private List<DatosAutor> datosAutor = new ArrayList<>();
    private Optional<Libro> libroBuscado;
    private LibroRepository repository;

    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ----------
                    Elija la opción a través de su número:
                    1) Buscar libro por título
                    2) Listar libros registrados
                    3) Listar autores registrados
                    4) Listar autores vivos en un determinado año
                    5) Listar libros por idioma
                    0) Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarPorTitulo();
                    break;
            }
        }
    }

    private void buscarPorTitulo() {
        System.out.println("Escribe el título del libro que desea buscar:");
        String tituloBusqueda = teclado.nextLine();
        String urlBusqueda = URL_BASE + "?search=" + tituloBusqueda.replace(" ", "+");
        String jsonResultado = consumoAPI.obtenerDatos(urlBusqueda);
        RespuestaLibro respuesta = conversor.obtenerDatos(jsonResultado, RespuestaLibro.class);

        if (respuesta != null && respuesta.count() > 0 && respuesta.results() != null) {
            respuesta.results().forEach(l -> {
                String autor = l.authors() != null && !l.authors().isEmpty() ? l.authors().get(0).name() : "Autor desconocido";
                String idioma = l.languages() != null && !l.languages().isEmpty() ? l.languages().get(0) : "Idioma desconocido";

                Libro libro = new Libro(
                        l.title(),
                        autor,
                        idioma,
                        l.downloadCount()
                );

                Optional<Libro> libroExistente = repository.findByTituloIgnoreCase(libro.getTitulo());
                if (libroExistente.isEmpty()) {
                    repository.save(libro);
                    System.out.println("Libro guardado en la base de datos: " + libro);
                } else {
                    System.out.println("El libro con título: " + libro.getTitulo() + " ya esta en la base de datos");
                }
            });
        } else {
            System.out.println("No se encontraron libros con el título: " + tituloBusqueda + " en Gutendex.");
        }
    }
}
