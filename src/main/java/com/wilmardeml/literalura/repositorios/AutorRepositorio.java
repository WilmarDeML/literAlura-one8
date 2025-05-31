package com.wilmardeml.literalura.repositorios;

import com.wilmardeml.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {}
