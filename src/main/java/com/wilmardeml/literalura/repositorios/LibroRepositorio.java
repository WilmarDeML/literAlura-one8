package com.wilmardeml.literalura.repositorios;

import com.wilmardeml.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {

    @EntityGraph(attributePaths = {"autor"})
    Libro findByTitulo(String titulo);

}
