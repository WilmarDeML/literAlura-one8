package com.wilmardeml.literalura.servicios.serv;

import com.wilmardeml.literalura.modelos.Libro;

import java.util.List;

public interface LibroServ extends CommonServ<Libro>{
    List<Libro> buscarPorIdioma(String idioma);

    Libro buscarPorTitulo(String titulo);

    List<Libro> listarTop10();
}
