package com.Literalura.repository;

import com.Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloIgnoreCase(String nombreLibro);

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE l.autor IS NOT NULL AND l.autor <> '' ORDER BY l.autor")
    List<String> findAllDistinctAutores();

    List<Libro> findByAutorIgnoreCase(String autor);

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE l.añoNacimientoAutor <= :añoConsulta AND (l.añoFallecimientoAutor IS NULL OR l.añoFallecimientoAutor >= :añoConsulta) ORDER BY l.autor")
    List<String> findAutoresVivosEnAño(int añoConsulta);

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE l.idioma = :idiomaConsulta ORDER BY l.autor")
    List<String> findIdiomas(String idiomaConsulta);
}
