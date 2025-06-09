package com.Literalura.principal;

import com.Literalura.model.DatosAutor;
import com.Literalura.model.DatosLibro;
import com.Literalura.model.Libro;
import com.Literalura.repository.LibroRepository;
import com.Literalura.service.ConsumoAPI;
import com.Literalura.service.ConvertirDatos;
import com.Literalura.service.RespuestaLibro;
import org.hibernate.mapping.Set;

import java.util.*;

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
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Adiós");
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
            var libroResultado = respuesta.results().get(0);

            String autor = libroResultado.authors() != null && !libroResultado.authors().isEmpty() ? libroResultado.authors().get(0).name() : "Autor desconocido";
            String idioma = libroResultado.languages() != null && !libroResultado.languages().isEmpty() ? libroResultado.languages().get(0) : "Idioma desconocido";

            Integer añoNacimiento = libroResultado.authors() != null && !libroResultado.authors().isEmpty() && libroResultado.authors().get(0).birth_year() != null ? libroResultado.authors().get(0).birth_year() : null;
            Integer añoFallecimiento = libroResultado.authors() != null && !libroResultado.authors().isEmpty() && libroResultado.authors().get(0).death_year() != null ? libroResultado.authors().get(0).death_year() : null;
            Libro libro = new Libro(
                    libroResultado.title(),
                    autor,
                    idioma,
                    libroResultado.downloadCount(),
                    añoNacimiento,
                    añoFallecimiento
            );
            Optional<Libro> libroExistente = repository.findByTituloIgnoreCase(libro.getTitulo());
            if (libroExistente.isEmpty()) {
                repository.save(libro);
                System.out.println("Libro guardado en la base de datos");
            } else {
                System.out.println("El libro con título: " + libro.getTitulo() + " ya existe en la base datos");
            }
        } else {
            System.out.println("No se encontraron libros con el título: " + tituloBusqueda + " en Gutendex.");
        }
    }

    private void listarLibrosRegistrados() {
        System.out.println("Libros en la base de datos");
        List<Libro> libros = repository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            libros.forEach(l -> System.out.println(l));
        }
        System.out.println("--------------------------------------------");
    }

    private void listarAutoresRegistrados() {
        System.out.println("Autores en la base de datos");
        List<String> autores = repository.findAllDistinctAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            for (String autor : autores) {
                List<Libro> librosDelAutor = repository.findByAutorIgnoreCase(autor);
                System.out.println("Autor: " + autor);
                Integer añoNacimiento = null;
                Integer añoFallecimiento = null;
                if (!librosDelAutor.isEmpty()) {
                    añoNacimiento = librosDelAutor.get(0).getAñoNacimientoAutor();
                    añoFallecimiento = librosDelAutor.get(0).getAñoFallecimientoAutor();
                }
                System.out.println("Fecha de nacimiento: " + (añoNacimiento != null ? añoNacimiento : "N/A"));
                System.out.println("Fecha de fallecimiento: " + (añoFallecimiento != null ? añoFallecimiento : "N/A"));
                System.out.println("Libros: ");
                if (librosDelAutor.isEmpty()) {
                    System.out.println("\tNo hay libros registrados para este autor.");
                } else {
                    for (Libro libro : librosDelAutor) {
                        System.out.println("\t- " + libro.getTitulo());
                    }
                }
                System.out.println("--------------------------------------------");
            }
        }
        System.out.println("--------------------------------------------");
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar");
        int añoConsulta = teclado.nextInt();
        teclado.nextLine();

        System.out.println("--- Autores vivos en el año " + añoConsulta + " ---");
        List<String> autoresVivos = repository.findAutoresVivosEnAño(añoConsulta);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + añoConsulta);
        } else {
            for (String autor : autoresVivos) {
                System.out.println("- " + autor);
            }
        }
        System.out.println("--------------------------------------------------");
    }

    private void listarPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);

        String idiomaConsulta = teclado.nextLine();
        System.out.println("--- Libros disponibles en " + idiomaConsulta + " ---");
        List<String> idiomasDisponibles = repository.findIdiomas(idiomaConsulta);

        if (idiomasDisponibles.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma elegido");
        } else {
            for (String idioma : idiomasDisponibles) {
                System.out.println("- " + idioma);
            }
        }
    }
}
