package com.wilmardeml.literalura.repositorios;

import com.wilmardeml.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {

    @EntityGraph(attributePaths = {"libros"})
    Autor findByNombre(String nombre);

    @EntityGraph(attributePaths = {"libros"})
    List<Autor> findAllBy();

    @EntityGraph(attributePaths = {"libros"})
    List<Autor> findByAnioMuerteGreaterThanAndAnioNacimientoLessThan(int anioMuerte, int anioNacimiento);

}
