package com.wilmardeml.literalura.repositorios;

import com.wilmardeml.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {

    @EntityGraph(attributePaths = {"libros"})
    Autor findByNombre(String nombre);

    @EntityGraph(attributePaths = {"libros"})
    List<Autor> findAllByOrderByNombre();

    @EntityGraph(attributePaths = {"libros"})
    List<Autor> findByAnioMuerteGreaterThanAndAnioNacimientoLessThan(int anioMuerte, int anioNacimiento);

    @Query("SELECT a FROM Autor a JOIN FETCH a.libros WHERE a.nombre ILIKE %:nombreAutor%")
    List<Autor> buscarPorNombre(String nombreAutor);
}
