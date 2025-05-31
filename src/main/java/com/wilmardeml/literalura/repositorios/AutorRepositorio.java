package com.wilmardeml.literalura.repositorios;

import com.wilmardeml.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {

    @EntityGraph(attributePaths = {"libros"})
    Autor findByNombre(String nombre);

}
