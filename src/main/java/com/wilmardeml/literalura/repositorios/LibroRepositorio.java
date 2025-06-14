package com.wilmardeml.literalura.repositorios;

import com.wilmardeml.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {

    @EntityGraph(attributePaths = {"autor"})
    Libro findByTitulo(String titulo);

    // @Query("SELECT l FROM Libro l JOIN FETCH l.autor") // Hace lo mismo que la anotación @EntityGraph
    @EntityGraph(attributePaths = {"autor"})
    List<Libro> findAllByOrderByTitulo();

    @EntityGraph(attributePaths = {"autor"})
    List<Libro> findAllByIdioma(String idioma);

    @EntityGraph(attributePaths = {"autor"})
    List<Libro> findFirst10ByOrderByNumeroDescargasDesc();
}
